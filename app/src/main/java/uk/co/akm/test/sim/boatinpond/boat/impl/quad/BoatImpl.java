package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.HydrofoilRudder;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;
import uk.co.akm.test.sim.boatinpond.boat.impl.PowerHydrofoilRudder;
import uk.co.akm.test.sim.boatinpond.boat.impl.PowerRudder;
import uk.co.akm.test.sim.boatinpond.math.TrigValues;
import uk.co.akm.test.sim.boatinpond.phys.Body;
import uk.co.akm.test.sim.boatinpond.phys.State;

/**
 * Created by Thanos Mavroidis on 24/02/2018.
 */
public class BoatImpl extends Body implements Boat {
    private static final double V_TRANSITION = BoatConstants.V_TRANSITION;

    private final double kLon;
    private final double kLat;
    private final double kLonReverse;

    private final double cogDistanceFromStern;
    private final double rudderAreaFraction;

    private final double kRud;

    private final double kLowBack;
    private final double kLowFront;
    private final double kHighBack;
    private final double kHighFront;
    private final double omegaTransitionBack;
    private final double omegaTransitionFront;

    private double cosa;
    private double sina;
    private double vLon;
    private double vLat;
    private double vLonSqSigned;
    private double vLatSqSigned;

    private double omg;
    private double omgSqSigned;

    private final Rudder rudder;
    private final HydrofoilRudder rudder2;
    private final double maxRudderAngle;

    public BoatImpl(BoatConstants constants, double hdn0, double v0) {
        super(0, 0, 0, hdn0, 0, 0, v0*Math.cos(hdn0), v0*Math.sin(hdn0), 0, 0, 0, 0);

        kLon = constants.getkLon();
        kLat = constants.getkLat();
        kLonReverse = constants.getKLonReverse();

        cogDistanceFromStern = constants.getCentreOfMassFromStern();
        rudderAreaFraction = constants.getRudderAreaFraction();

        kRud = constants.getkRud();

        final double l4 = 4*constants.getLength();
        final double l8 = 2*l4;

        final double x = constants.getCentreOfMassFromStern();
        final double lx = (constants.getLength() - x);

        final double x3 = Math.pow(x, 3);
        final double x4 = x*x3;

        final double lx3 = Math.pow(lx, 3);
        final double lx4 = lx*lx3;

        kLowBack = kLat*x3/l4;
        kHighBack = kLat*x4/l8;
        kLowFront = kLat*lx3/l4;
        kHighFront = kLat*lx4/l8;

        omegaTransitionBack = 2*V_TRANSITION/x;
        omegaTransitionFront = 2*V_TRANSITION/lx;

        maxRudderAngle = constants.getMaximumRudderAngle();
        rudder = new PowerRudder(maxRudderAngle, constants.timeToMaximumRudderAnge());
        rudder2 = new PowerHydrofoilRudder(maxRudderAngle, constants.timeToMaximumRudderAnge(), constants.getRudderLength());
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
        omgSqSigned = omg*Math.abs(omg);
    }

    @Override
    protected final void updateAngularAcceleration(State start, double dt) {
        final double rudderDeflection = rudder.getRudderAngle()/maxRudderAngle;
        final double kRudEffective = rudderDeflection*kRud;
        final double rudderForce = estimateRudderForce(kRudEffective, V_TRANSITION, vLon, vLonSqSigned);
        final double rudderTorque = rudderForce*cogDistanceFromStern;

        final double resistanceTorque = estimateResistanceTorque(omg, omgSqSigned);

        final double totalTorque = rudderTorque + resistanceTorque;

        aHdn = totalTorque; // Assume moment of inertia value of 1.
    }

    @Deprecated
    private double estimateRudderForce(double k, double vTransition, double v, double vSqSigned) {
        if (Math.abs(v) > vTransition) {
            return k*vSqSigned;
        } else {
            return k*v;
        }
    }

    private double estimateRudderTorque(double k, double vSqSigned) {
        final double aoa = rudder2.getAngleOfAttack();
        final double halfLength = rudder2.getHalfLength();
        final double dragCoefficient = rudder2.getDragCoefficient();
        final double liftCoefficient = rudder2.getLiftCoefficient();

        final double torqueDragComponent = dragCoefficient*halfLength*Math.cos(aoa);
        final double torqueLiftComponent = liftCoefficient*(cogDistanceFromStern + halfLength*Math.sin(aoa));
        final double torque = k*vSqSigned*(torqueDragComponent + torqueLiftComponent);

        if (rudder2.getRudderAngle() >= 0) {
            return torque;
        } else {
            return -torque;
        }
    }

    private double estimateResistanceTorque(double omg, double omgSqSigned) {
        return estimateResistanceTorqueBack(omg, omgSqSigned) + estimateResistanceTorqueFront(omg, omgSqSigned);
    }

    private double estimateResistanceTorqueBack(double omg, double omgSqSigned) {
        return estimateResistanceTorque(kHighBack, kLowBack, omegaTransitionBack, omg, omgSqSigned);
    }

    private double estimateResistanceTorqueFront(double omg, double omgSqSigned) {
        return estimateResistanceTorque(kHighFront, kLowFront, omegaTransitionFront, omg, omgSqSigned);
    }

    private double estimateResistanceTorque(double kHigh, double kLow, double omgTransition, double omg, double omgSqSigned) {
        if (Math.abs(omg) > omgTransition) {
            return -kHigh*omgSqSigned;
        } else {
            return -kLow*omg;
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
