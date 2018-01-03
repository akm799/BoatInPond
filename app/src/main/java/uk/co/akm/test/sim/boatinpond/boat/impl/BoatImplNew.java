package uk.co.akm.test.sim.boatinpond.boat.impl;


import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;
import uk.co.akm.test.sim.boatinpond.math.MathConstants;
import uk.co.akm.test.sim.boatinpond.math.TrigValues;
import uk.co.akm.test.sim.boatinpond.phys.Body;
import uk.co.akm.test.sim.boatinpond.phys.State;

/**
 * //TODO Investigate errors in the class with angular parameter oscillations and exponential increases during turns.
 *
 * Simulates a propulsion-less boat gliding in a pond after some initial push. The simulation assumes
 * that the boat is pushed from the centre of the pond with some initial speed along some heading.
 * Before this initial push, the boat rudder is fixed at some angle. Due to the water resistance, the
 * boat is expected to, eventually, come to a standstill at some point in the pond, with some final
 * heading.
 *
 * Please note that the mass of the boat as well as its moment of inertia are both assumed to be 1.
 *
 * Created by Thanos Mavroidis on 18/12/2017.
 */
public final class BoatImplNew extends Body implements Boat {
    private static final double V_TRANSITION = 1;
    private static final double OMG_TRANSITION = 1;

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

    // The boat length
    private final double length;

    // The rudder area
    private final double rudderOverFrontalIncidenceArea;

    // The distance of the centre of gravity from the stern.
    private final double cgFromStern;

    // The maximum rudder angle.
    private final double maxRudderAngle;

    // Half the distance from the bow to the boat centre of gravity.
    private final double rFront;

    // Half the distance from the stern to the boat centre of gravity.
    private final double rBack;

    // The turning force resistance coefficient of the forward section of the boat.
    private final double kResFront;

    // The turning force resistance coefficient of the aft section of the boat.
    private final double kResBack;


    private final Rudder rudder = new PowerRudder(Math.PI/4, 2);

    // Variables for the update steps, held here for optimization purposes.
    private double cosa; // The cosine of the heading angle.
    private double sina; // The sine of the heading angle.

    private double vLon; // The component of the velocity vector along the boat axis.
    private double vLat; // The component of the velocity vector perpendicular to the boat axis.
    private double vLonAbs; // The boat speed along the boat axis.
    private double vLatAbs; // The boat speed perpendicular to the boat axis.
    private double vLonSq; // The component of the velocity vector along the boat axis, squared.
    private double vLatSq; // The component of the velocity vector perpendicular to the boat axis, squared.
    private int signLon; // The sign of the velocity along the boat axis (1 if vLon >= 0 or -1 if vLon < 0)
    private int signLat; // The sign of the velocity perpendicular to the boat axis (1 if vLat >= 0 or -1 if vLat < 0)

    private double omg; // The heading angular velocity.
    private double omgSq; // The heading angular velocity, squared.
    private double omgAbs; // The absolute value of the heading angular velocity.
    private int signOmg; // The sign of the heading angular velocity (1 if omgHdn >= 0 or -1 if omgHdn < 0)

    private double rudderAngle; // The rudder angle.
    private double rudderAreaProjection; // The projection factor of the rudder area exposed to the longitudinal water flow.

    private double vFront; // The linear rotational (turning) velocity of the boat point halfway between the bow and the centre of gravity.
    private double vBack; // The linear rotational (turning) velocity of the boat point halfway between the stern and the centre of gravity.

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
        this.length = constants.getLength();
        this.cgFromStern = constants.getCentreOfMassFromStern();
        this.maxRudderAngle = rudder.getMaxRudderAngle();
        this.rudderOverFrontalIncidenceArea = constants.getkAng();

        final double lFront = length - cgFromStern;
        final double lBack = cgFromStern;
        this.rFront = lFront/2;
        this.rBack = lBack/2;
        this.kResFront = lFront*kLat/length;
        this.kResBack = lBack*kLat/length;
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
        vLon = vx*cosa + vy*sina;
        vLat = -vx*sina + vy*cosa;
        vLonSq = vLon*vLon;
        vLatSq = vLat*vLat;
        vLonAbs = Math.abs(vLon);
        vLatAbs = Math.abs(vLat);
        signLon = (vLon >= 0 ? 1 : -1);
        signLat = (vLat >= 0 ? 1 : -1);

        omg = start.omgHdn();
        omgSq = omg*omg;
        omgAbs = Math.abs(omg);
        signOmg = (omg >= 0 ? 1 : -1);

        rudderAngle = rudder.getRudderAngle();
        rudderAreaProjection = Math.sin(rudderAngle*MathConstants.PI_OVER_TWO/maxRudderAngle);

        vFront = omgAbs*rFront;
        vBack = omgAbs*rBack;
    }

    @Override
    protected void updateAngularAcceleration(State start, double dt) {
        final double rudderForce = (vLonAbs > V_TRANSITION ? kRud*vLonSq*rudderAreaProjection : kRud*vLonAbs*rudderAreaProjection);
        final double rudderTorque = rudderForce*cgFromStern;

        final double fResFront = (vFront > V_TRANSITION ? kResFront*vFront*vFront : kResFront*vFront);
        final double fResBack = (vBack > V_TRANSITION ? kResBack*vBack*vBack : kResBack*vBack);
        final double turningResistanceTorqueAbs = fResFront*rFront + fResBack*rBack;
        final double turningResistanceTorque = (omgAbs > OMG_TRANSITION ? -turningResistanceTorqueAbs : turningResistanceTorqueAbs);

        aHdn = (rudderTorque + turningResistanceTorque)/moi;
    }

    @Override
    protected void updateAcceleration(State start, double dt) {
        final double fRestRud = estimateBoatRudderResistance();
        final double fRestWater = computeBoatLongitudinalResistance();
        final double fLon = fRestWater + fRestRud;

        final double fLat = computeBoatLateralResistance();

        // Evaluate the acceleration wrt the boat heading.
        final double aLon = fLon/mass;
        final double aLat = fLat/mass;

        // Rotate the acceleration wrt the boat heading an angle a (i.e. the reverse of our previous rotation) to get the acceleration wrt our coordinate system.
        ax = aLon*cosa - aLat*sina;
        ay = aLon*sina + aLat*cosa;
    }

    private double computeBoatLateralResistance() {
        if (vLatAbs > V_TRANSITION) {
            return -signLat*kLat*vLatSq;
        } else {
            return -kLat*vLat;
        }
    }

    private double estimateBoatRudderResistance() {
        final double kResRud = kLon*Math.sin(rudderAngle)*rudderOverFrontalIncidenceArea;
        if (vLonAbs > V_TRANSITION) {
            return -signLon*kResRud*vLonSq;
        } else {
            return -kResRud*vLon;
        }
    }

    private double computeBoatLongitudinalResistance() {
        if (vLonAbs > V_TRANSITION) {
            return -signLon*kLon*vLonSq;
        } else {
            return -kLon*vLon;
        }
    }

    @Override
    public Rudder getRudder() {
        return rudder;
    }
}