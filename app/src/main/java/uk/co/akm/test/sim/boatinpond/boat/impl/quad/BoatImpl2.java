package uk.co.akm.test.sim.boatinpond.boat.impl.quad;


import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.HydrofoilRudder;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;
import uk.co.akm.test.sim.boatinpond.boat.impl.PowerHydrofoilRudder;
import uk.co.akm.test.sim.boatinpond.math.TrigValues;
import uk.co.akm.test.sim.boatinpond.phys.Body;
import uk.co.akm.test.sim.boatinpond.phys.State;

/**
 * Implementation where the resistance torque is calculated by taking into account the water
 * resistance due to the boat rotation as well as the effect on resisting that rotation played
 * by the lateral water resistance (as the boat turns). The rudder is also modelled as a hydrofoil.
 *
 * Created by Thanos Mavroidis on 23/04/2021.
 */
public class BoatImpl2 extends Body implements Boat {
    private static final double V_TRANSITION = BoatConstants.V_TRANSITION;

    private final double kLon;
    private final double kLat;
    private final double kLonReverse;

    private final double boatLength;
    private final double cogDistanceFromStern;
    private final double rudderAreaFraction;

    private final double kRud;

    private final double dFront;
    private final double kFront;

    private double cosa;
    private double sina;
    private double vLon;
    private double vLat;
    private double vLonSqSigned;
    private double vLatSqSigned;

    private double omg;

    private final HydrofoilRudder rudder;

    public BoatImpl2(BoatConstants constants, double hdn0, double v0) {
        super(0, 0, 0, hdn0, 0, 0, v0*Math.cos(hdn0), v0*Math.sin(hdn0), 0, 0, 0, 0);

        kLon = constants.getkLon();
        kLat = constants.getkLat();
        kLonReverse = constants.getKLonReverse();

        boatLength = constants.getLength();
        cogDistanceFromStern = constants.getCentreOfMassFromStern();
        rudderAreaFraction = constants.getRudderAreaFraction();

        kRud = constants.getkRud();

        final double cogDistanceFromBow = boatLength - cogDistanceFromStern;
        final double aFront = cogDistanceFromBow/boatLength;
        dFront = cogDistanceFromBow;
        kFront = aFront * kLat;

        final double maxRudderAngle = constants.getMaximumRudderAngle();
        rudder = new PowerHydrofoilRudder(maxRudderAngle, constants.timeToMaximumRudderAnge(), constants.getRudderLength());
    }

    @Override
    protected final void initUpdate(State start, double dt) {
        updateControls(dt);
        computeVariables(start);
    }

    private void updateControls(double dt) {
        rudder.update(dt);
        updateAdditionalControls(dt);
    }

    protected void updateAdditionalControls(double dt) {}

    private void computeVariables(State start) {
        final TrigValues hdn = start.hdnTrig();
        cosa = hdn.cos();
        sina = hdn.sin();

        final double vx = start.vx();
        final double vy = start.vy();
        vLon =  vx*cosa + vy*sina;
        vLat = -vx*sina + vy*cosa;
        vLonSqSigned = vLon*Math.abs(vLon);
        vLatSqSigned = vLat*Math.abs(vLat);

        omg = start.omgHdn();
    }

    @Override
    protected final void updateAngularAcceleration(State start, double dt) {
        final double rudderTorque = estimateRudderTorque(kRud, vLon);
        final double resistanceTorque = estimateResistanceTorque(kLat, omg, vLon, vLat);

        aHdn = rudderTorque + resistanceTorque; // Assume moment of inertia value of 1.
    }

    private double estimateRudderTorque(double k, double v) {
        final double aoa = rudder.getAngleOfAttack();
        final double halfLength = rudder.getHalfLength();
        final double dragCoefficient = rudder.getDragCoefficient();
        final double liftCoefficient = rudder.getLiftCoefficient();

        final double torqueDragComponent = dragCoefficient*halfLength*Math.sin(aoa);
        final double torqueLiftComponent = liftCoefficient*(cogDistanceFromStern + halfLength*Math.cos(aoa));
        final double torqueMagnitude = k*v*v*(torqueDragComponent + torqueLiftComponent);

        if (v >= 0) {
            if (rudder.getRudderAngle() >= 0) {
                return torqueMagnitude;
            } else {
                return -torqueMagnitude;
            }
        } else { // When the boat is going backwards the rudder torque is reversed.
            if (rudder.getRudderAngle() >= 0) {
                return -torqueMagnitude;
            } else {
                return torqueMagnitude;
            }
        }
    }

    private double estimateResistanceTorque(double kLat, double omg, double vLon, double vLat) {
        // The constants for the back boat section depend on the current rudder deflection angle.
        final double effectiveRudderLength = rudder.getHalfLength()*Math.sin(rudder.getAngleOfAttack());
        final double dBack = cogDistanceFromStern + effectiveRudderLength;
        final double kBack = (dBack/boatLength) * kLat;

        final double vRotFront, vRotBack;
        if (vLon >= 0) {
            vRotFront = omg*dFront - vLat; // The lateral water flow reduces the rotational resistance of the front boat section.
            vRotBack = omg*dBack + vLat; // The lateral water flow increases the rotational resistance of the back boat section.
        } else { // When the boat is moving backwards, the reverse is true.
            vRotFront = omg*dFront + vLat;
            vRotBack = omg*dBack - vLat;
        }

        final double forceFront = resistanceForceMagnitude(kFront, vRotFront);
        final double forceBack = resistanceForceMagnitude(kBack, vRotBack);
        final double torqueMagnitude = forceFront*dFront + forceBack*dBack;

        if (omg >= 0) {
            return -torqueMagnitude;
        } else {
            return torqueMagnitude;
        }
    }

    private double resistanceForceMagnitude(double k, double v) {
        final double vAbs;
        if (v >= 0) {
            vAbs = v;
        } else {
            vAbs = -v;
        }

        if (vAbs < V_TRANSITION) {
            return k*vAbs;
        } else {
            return k*vAbs*vAbs;
        }
    }

    @Override
    protected final void updateAcceleration(State start, double dt) {
        final double kLon = (vLon >= 0 ? this.kLon : this.kLonReverse);
        final double kLonEffective = kLon + kLon*rudderAreaFraction*Math.sin(Math.abs(rudder.getRudderAngle())); // Approximate increased longitudinal resistance due to rudder deflection.

        // Evaluate the acceleration wrt the linear heading.
        final double propulsionLon = estimatePropulsionForce();
        final double resistanceLon = estimateLinearResistanceForce(kLonEffective, V_TRANSITION, vLon, vLonSqSigned);
        final double aLon = propulsionLon + resistanceLon; // Assume mass value of 1
        final double aLat = estimateLinearResistanceForce(kLat, V_TRANSITION, vLat, vLatSqSigned); // Assume mass value of 1

        // Rotate the acceleration wrt the linear heading an angle a (i.e. the reverse of our previous rotation) to get the acceleration wrt our coordinate system.
        ax = aLon*cosa - aLat*sina;
        ay = aLon*sina + aLat*cosa;
    }

    protected double estimatePropulsionForce() {
        return 0;
    }

    private double estimateLinearResistanceForce(double k, double vTransition, double v, double vSqSigned) {
        if (Math.abs(v) > vTransition) {
            return -k*vSqSigned;
        } else {
            return -k*v;
        }
    }

    @Override
    public final Rudder getRudder() {
        return rudder;
    }
}
