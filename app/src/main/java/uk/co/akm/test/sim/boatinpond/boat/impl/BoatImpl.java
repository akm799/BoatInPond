package uk.co.akm.test.sim.boatinpond.boat.impl;


import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;
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
public final class BoatImpl extends Body implements Boat {
    // Resistance coefficient across the axis of the boat heading.
    private final double kLon;
    // Resistance coefficient perpendicular to the axis of the boat heading.
    private final double kLat;

    // Rudder deflection coefficient.
    private final double kRud;
    // Boat angular motion resistance coefficient.
    private final double kAng;

    private final Rudder rudder = new PowerRudder(Math.PI/4, 2);

    // Variables for the update steps, held here for optimization purposes.
    private double cosa; // The cosine of the heading angle.
    private double sina; // The sine of the heading angle.

    private double vx;   // The x-axis velocity component.
    private double vy;   // The y-axis velocity component.
    private double vLon; // The component of the velocity vector along the boat axis.

    /**
     * Boat constructor specifying the boat characteristics and the initial conditions.
     *
     * @param constants the boat performance constants
     * @param hdn0 the heading along which the boat is pushed
     * @param v0 the speed at which the boat is initially pushed
     */
    public BoatImpl(BoatConstants constants, double hdn0, double v0) {
        super(0, 0, 0, hdn0, 0, 0, v0*Math.cos(hdn0), v0*Math.sin(hdn0), 0, 0, 0, 0);

        this.kLon = constants.kLon;
        this.kLat = constants.kLat;
        this.kRud = constants.kRud;
        this.kAng = constants.kAng;
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

        vx = start.vx();
        vy = start.vy();
        vLon = vx*cosa + vy*sina;
    }

    @Override
    protected void updateAngularAcceleration(State start, double dt) {
        aHdn = kRud*vLon*Math.sin(2*rudder.getRudderAngle()) - kAng*omgHdn(); // Moment of inertia is 1.
    }

    @Override
    protected void updateAcceleration(State start, double dt) {
        final double vLat = -vx*sina + vy*cosa;

        //TODO Simulate increased longitudinal resistance due to rudder use (kLonEffective = kLon*kRat*sin(ra) where kRat ~ rudderArea/totalBoatFrontalArea)

        // Evaluate the acceleration wrt the boat heading.
        final double aLon = -kLon*vLon; // Mass is 1
        final double aLat = -kLat*vLat; // Mass is 1

        // Rotate the acceleration wrt the boat heading an angle a (i.e. the reverse of our previous rotation) to get the acceleration wrt our normal coordinate system.
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


  Rudder force computation result:

  F(a, v) = 0.5*r*A*v^2*k^2*sin(a)*cos^2(pi/2 - 2*a)
  a: rudder angle
  v: speed over water
  r: water density
  A: rudder surface area below the water line
  k: k = vd/v where vd is the speed of water deflected by the rudder (asuming that vd , v).

  This assumes that the force of water stream  moving at speed v through a surface area A is: F = 0.5*r*A*v^2
*/
