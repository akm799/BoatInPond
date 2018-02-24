package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;
import uk.co.akm.test.sim.boatinpond.boat.impl.PowerRudder;
import uk.co.akm.test.sim.boatinpond.math.TrigValues;
import uk.co.akm.test.sim.boatinpond.phys.Body;
import uk.co.akm.test.sim.boatinpond.phys.State;

/**
 * Created by Thanos Mavroidis on 24/02/2018.
 */
public final class BoatImpl extends Body implements Boat {
    private static final double V_TRANSITION = BoatConstants.V_TRANSITION;

    private final double kLon;
    private final double kLat;
    private final double kLonReverse;

    private final double cogDistanceFromStern;

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

    private final double maxRudderAngle = Math.PI/4;
    private final Rudder rudder = new PowerRudder(maxRudderAngle, 2);

    public BoatImpl(BoatConstants constants, double hdn0, double vx0, double vy0, double x0, double y0) {
        super(0, 0, 0, hdn0, 0, 0, vx0, vy0, 0, x0, y0, 0);

        kLon = constants.getkLon();
        kLat = constants.getkLat();
        kLonReverse = constants.getKLonReverse();

        cogDistanceFromStern = constants.getCentreOfMassFromStern();

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
        kHighBack = kLat*x4/(l8);
        kLowFront = kLat*lx3/l4;
        kHighFront = kLat*lx4/l8;

        omegaTransitionBack = 2*V_TRANSITION/x;
        omegaTransitionFront = 2*V_TRANSITION/lx;
    }

    @Override
    protected void initUpdate(State start, double dt) {
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
    protected void updateAngularAcceleration(State start, double dt) {
        final double rudderDeflection = rudder.getRudderAngle()/maxRudderAngle;
        final double kRudEffective = rudderDeflection*kRud;
        final double rudderForce = estimateRudderForce(kRudEffective, V_TRANSITION, vLon, vLonSqSigned);
        final double rudderTorque = rudderForce*cogDistanceFromStern;

        final double resistanceTorque = estimateResistanceTorque(omg, omgSqSigned);

        final double totalTorque = rudderTorque + resistanceTorque;

        aHdn = totalTorque; // Assume moment of inertia value of 1.
    }

    private double estimateRudderForce(double k, double vTransition, double v, double vSqSigned) {
        if (Math.abs(v) > vTransition) {
            return k*vSqSigned;
        } else {
            return k*v;
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
    protected void updateAcceleration(State start, double dt) {
        final double kLon = (vLon >= 0 ? this.kLon : this.kLonReverse);

        // Evaluate the acceleration wrt the linear heading.
        final double aLon = estimateLinearResistanceForce(kLon, V_TRANSITION, vLon, vLonSqSigned); // Assume mass value of 1
        final double aLat = estimateLinearResistanceForce(kLat, V_TRANSITION, vLat, vLatSqSigned); // Assume mass value of 1

        // Rotate the acceleration wrt the linear heading an angle a (i.e. the reverse of our previous rotation) to get the acceleration wrt our coordinate system.
        ax = aLon*cosa - aLat*sina;
        ay = aLon*sina + aLat*cosa;
    }

    private double estimateLinearResistanceForce(double k, double vTransition, double v, double vSqSigned) {
        if (Math.abs(v) > vTransition) {
            return -k*vSqSigned;
        } else {
            return -k*v;
        }
    }

    @Override
    public Rudder getRudder() {
        return rudder;
    }
}
