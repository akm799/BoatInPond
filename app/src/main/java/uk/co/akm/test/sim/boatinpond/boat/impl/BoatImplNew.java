package uk.co.akm.test.sim.boatinpond.boat.impl;


import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;
import uk.co.akm.test.sim.boatinpond.math.MathConstants;
import uk.co.akm.test.sim.boatinpond.math.TrigValues;
import uk.co.akm.test.sim.boatinpond.phys.Body;
import uk.co.akm.test.sim.boatinpond.phys.State;

/**
 * Simulates a propulsion-less boat gliding in a pond after some initial push. The simulation assumes
 * that the boat is pushed from the centre of the pond with some initial speed along some heading.
 * Before this initial push, the boat rudder is fixed at some angle. Due to the water resistance, the
 * boat is expected to, eventually, come to a standstill at some point in the pond, with some final
 * heading.
 *
 * Please note that the mass of the boat as well as its moment of inertia are both assumed to be 1.
 *
 * Created by Thanos Mavroidis on 02/12/2017.
 */
public final class BoatImplNew extends Body implements Boat {
    // Resistance coefficient across the axis of the boat heading.
    private final double kLon;
    // Resistance coefficient perpendicular to the axis of the boat heading.
    private final double kLat;

    // Rudder deflection coefficient.
    private final double kRud;

    // The boat total mass.
    private final double mass;

    // The boat moment of inertia.
    private final double moi;

    // The distance of the centre of gravity from the stern.
    private final double cgFromStern;

    // The distance of the front from the centre of gravity divided by two.
    private final double rFront;

    // The distance of the centre of gravity from the stern divided by two.
    private final double rBack;

    // rFront + rBack
    private final double rSum;

    // rSum/2
    private final double rAvg;

    private final Rudder rudder = new PowerRudder(Math.PI/4, 2);

    // Variables for the update steps, held here for optimization purposes.
    private double cosa; // The cosine of the heading angle.
    private double sina; // The sine of the heading angle.

    private double vLonSq; // The component of the velocity vector along the boat axis, squared.
    private double vLatSq; // The component of the velocity vector along the boat axis, squared.

    private double sinRa; // The sine of the rudder angle.
    private double rdAngTrans; // PI/2 - 2*rudderAngle

    /**
     * Boat constructor specifying the boat characteristics and the initial conditions.
     *
     * @param constants the boat performance constants
     * @param hdn0 the heading along which the boat is pushed
     * @param v0 the speed at which the boat is initially pushed
     */
    public BoatImplNew(BoatConstants constants, double hdn0, double v0) {
        super(0, 0, 0, hdn0, 0, 0, v0*Math.cos(hdn0), v0*Math.sin(hdn0), 0, 0, 0, 0);

        this.kLon = constants.getkLon();
        this.kLat = constants.getkLat();
        this.kRud = constants.getkRud();
        this.mass = constants.getMass();
        this.moi = constants.getMomentOfInertia();

        cgFromStern = constants.getCentreOfMassFromStern();
        rFront = (constants.getLength() - cgFromStern)/2;
        rBack = cgFromStern/2;
        rSum = rFront + rBack;
        rAvg = rSum/2;
    }

    @Override
    protected void initUpdate(State start, double dt) {
        updateControls(dt);
        computeVariables(start);
    }

    private void updateControls(double dt) {
        rudder.update(dt);
    }

    private void computeVariables(State start) {
        final TrigValues hdn = start.hdnTrig();
        cosa = hdn.cos();
        sina = hdn.sin();

        final double vx = start.vx();
        final double vy = start.vy();
        final double vLon = vx*cosa + vy*sina;
        final double vLat = -vx*sina + vy*cosa;
        vLonSq = vLon*vLon;
        vLatSq = vLat*vLat;

        final double ra = rudder.getRudderAngle();
        sinRa = Math.sin(ra);
        rdAngTrans = MathConstants.PI_OVER_TWO - 2*ra;
    }

    @Override
    protected void updateAngularAcceleration(State start, double dt) {
        final double cs = Math.cos(rdAngTrans);
        final double fRud = kRud*vLonSq*sinRa*cs*cs;
        final double tRud = fRud*cgFromStern;

        final double omg = Math.abs(omgHdn());
        final double vFront = omg*rFront;
        final double vBack = omg*rBack;
        final double vCombined = (vFront*rFront + vBack*rBack)/rSum;
        final double fTurnResistance = kLat*vCombined*vCombined;
        final double tTurnResistance = fTurnResistance*rAvg;

        final double totalTorque = (tRud >= 0 ? tRud - tTurnResistance : tRud + tTurnResistance);

        aHdn = totalTorque/moi;
    }

    @Override
    protected void updateAcceleration(State start, double dt) {
        final double sn = Math.sin(rdAngTrans);
        final double fRestRud = -kRud*vLonSq*sinRa*(1 - sn*sn);
        final double fRestWater = -kLon*vLonSq;
        final double fLon = fRestWater + fRestRud;

        final double fLat = -kLat*vLatSq;

        // Evaluate the acceleration wrt the boat heading.
        final double aLon = fLon/mass;
        final double aLat = fLat/mass;

        // Rotate the acceleration wrt the boat heading an angle a (i.e. the reverse of our previous rotation) to get the acceleration wrt our coordinate system.
        ax = aLon*cosa - aLat*sina; // Mass is 1
        ay = aLon*sina + aLat*cosa; // Mass is 1
    }

    @Override
    public Rudder getRudder() {
        return rudder;
    }
}

/*
  Useful links:

  http://hyperphysics.phy-astr.gsu.edu/hbase/mi2.html#irod3

  http://eodg.atm.ox.ac.uk/user/dudhia/rowing/physics/rowing.pdf

  https://en.wikipedia.org/wiki/Drag_coefficient


  Rudder torque-force (lateral) computation result:
  F(a, v) = 0.5*r*A*v^2*k^2*sin(a)*cos^2(pi/2 - 2*a)

  Rudder resistance-force (longitudinal) computation result:
  F(a, v) = 0.5*r*A*v^2*k^2*sin(a)*[1 - sin^2(pi/2 - 2*a)]

  a: rudder angle
  v: speed over water
  r: water density
  A: rudder surface area below the water line
  k: k = vd/v where vd is the speed of water deflected by the rudder (assuming that vd < v).

  This assumes that the force of water stream  moving at speed v through a surface area A is: F = 0.5*r*A*v^2
*/
