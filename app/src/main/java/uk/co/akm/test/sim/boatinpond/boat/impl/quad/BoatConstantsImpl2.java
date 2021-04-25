package uk.co.akm.test.sim.boatinpond.boat.impl.quad;



import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.Hydrofoil;
import uk.co.akm.test.sim.boatinpond.boat.impl.foil.HydrofoilImpl;
import uk.co.akm.test.sim.boatinpond.math.root.FunctionAndDerivative;
import uk.co.akm.test.sim.boatinpond.math.root.NewtonRaphsonRootFinder;

/**
 * Created by Thanos Mavroidis on 23/04/2021.
 */
public class BoatConstantsImpl2 implements BoatConstants {
    private final double kLon;
    private final double kLat;
    private final double kLonReverse;
    private final double kRud;
    private final double boatLength;
    private final double cogDistanceFromStern;
    private final double rudderAreaFraction;
    private final double rudderLength;
    private final double maxRudderAngle;
    private final double timeToMaxRudderAngle;

    public BoatConstantsImpl2(BoatPerformance performance,
                              double kLatOverKLon,
                              double kLonReverseOverKLon,
                              double boatLength,
                              double cogDistanceFromStern,
                              double rudderAreaFraction,
                              double maxRudderAngle,
                              double boatToRudderLengthRatio) {
        checkArgs(boatLength, cogDistanceFromStern, performance.turnRate, performance.turningSpeed, V_TRANSITION);

        this.kLon = kLonEstimation(performance.launchSpeed, performance.distanceLimit);
        this.kLat = kLon*kLatOverKLon;
        this.kLonReverse = kLon*kLonReverseOverKLon;
        this.boatLength = boatLength;
        this.cogDistanceFromStern = cogDistanceFromStern;
        this.rudderAreaFraction = rudderAreaFraction;
        this.maxRudderAngle = maxRudderAngle;
        this.rudderLength = boatLength/boatToRudderLengthRatio;
        this.timeToMaxRudderAngle = performance.timeToMaxRudderDeflection;
        this.kRud = kRudEstimation(kLat, boatLength, boatToRudderLengthRatio, cogDistanceFromStern, performance.turnRadius, performance.turningSpeed);
    }

    private void checkArgs(double length, double cogDistanceFromStern, double omega, double v, double vTransition) {
        if (cogDistanceFromStern >= length) {
            throw new IllegalArgumentException("Distance of the centre of gravity from the stern (" + cogDistanceFromStern + ") is more than the total boat length (" + length + ").");
        }

        if (v < vTransition) {
            throw new IllegalArgumentException("Input boat turning speed (" + v + ") is too low. It is less than the transition speed (" + vTransition + ").");
        }

        final double omegaMin = Math.min(2*vTransition/cogDistanceFromStern, 2*vTransition/(length - cogDistanceFromStern));
        if (omega < omegaMin) {
            throw new IllegalArgumentException("Input boat turning angular velocity (" + omega + ") is less that the minimum value (" + omegaMin + ").");
        }
    }

    private double kLonEstimation(double launchSpeed, double distanceLimit) {
        return (Math.log(launchSpeed/V_TRANSITION) + V_TRANSITION)/distanceLimit;
    }

    /**
     * Estimates the rudder torque coefficient assuming that the input turning speed (omega)
     * is higher than the transition angular velocity omega of both the front and aft boat
     * sections.
     */
    private double kRudEstimation(
            double kLat,
            double boatLength,
            double boatToRudderLengthRatio,
            double cogDistanceFromStern,
            double turnRadius,
            double turningSpeed
    ) {
        final Hydrofoil hydrofoil = new HydrofoilImpl();
        final double phi = phi(kLat, 1, turnRadius, turningSpeed); // Assume mass equal to 1.
        final double vLon = turningSpeed*Math.cos(phi);
        final double vLat = turningSpeed*Math.sin(phi);
        checkVComponents(vLon, vLat);

        final double c = estimateRudderTorqueCoefficient(hydrofoil, boatLength, boatToRudderLengthRatio, cogDistanceFromStern);
        final double resistanceTorque = estimateResistanceTorqueMagnitude(hydrofoil, kLat, boatLength, boatToRudderLengthRatio, cogDistanceFromStern, turnRadius, turningSpeed, vLat);

        return resistanceTorque/(c * vLon * vLon);
    }

    private void checkVComponents(double vLon, double vLat) {
        if (vLon < V_TRANSITION) {
            throw new IllegalArgumentException("Unable to reach vLon value above the transition speed, during the rudder coefficient estimation.");
        }

        if (vLat < V_TRANSITION) {
            throw new IllegalArgumentException("Unable to reach vLat value above the transition speed, during the rudder coefficient estimation.");
        }
    }

    private double estimateRudderTorqueCoefficient(
            Hydrofoil hydrofoil,
            double boatLength,
            double boatToRudderLengthRatio,
            double cogDistanceFromStern
    ) {
        final double aoa = hydrofoil.getMaxLiftAngleOfAttack();
        final double halfLength = (boatLength/boatToRudderLengthRatio)/2;
        final double dragCoefficient = hydrofoil.getDragCoefficient(aoa);
        final double liftCoefficient = hydrofoil.getLiftCoefficient(aoa);

        final double torqueDragComponent = dragCoefficient*halfLength*Math.sin(aoa);
        final double torqueLiftComponent = liftCoefficient*(cogDistanceFromStern + halfLength*Math.cos(aoa));

        return torqueDragComponent + torqueLiftComponent;
    }

    private double estimateResistanceTorqueMagnitude(
            Hydrofoil hydrofoil,
            double kLat,
            double boatLength,
            double boatToRudderLengthRatio,
            double cogDistanceFromStern,
            double radius,
            double v,
            double vLat
    ) {
        final double dFront = boatLength - cogDistanceFromStern;
        final double kFront = (dFront/boatLength) * kLat;

        final double halfLength = (boatLength/boatToRudderLengthRatio)/2;
        final double effectiveRudderLength = halfLength*Math.cos(hydrofoil.getMaxLiftAngleOfAttack());
        final double dBack = cogDistanceFromStern + effectiveRudderLength;
        final double kBack = (dBack/boatLength) * kLat;

        final double omg = v / radius;
        final double vRotFront = omg*dFront - vLat; // The lateral water flow reduces the rotational resistance of the front boat section.
        final double vRotBack = omg*dBack + vLat; // The lateral water flow increases the rotational resistance of the back boat section.

        final double forceFront = resistanceForceMagnitude(kFront, vRotFront);
        final double forceBack = resistanceForceMagnitude(kBack, vRotBack);

        return forceFront*dFront + forceBack*dBack;
    }

    /**
     * Returns the absolute difference between the angles of the boat heading and velocity
     * vectors that are expected during a sustained turn of radius {@code radius} at a
     * constant speed {@code v}
     *
     * @param kLat the boat lateral water resistance coefficient
     * @param mass the boat mass
     * @param radius the sustained turn radius
     * @param v the sustained turn constant speed
     * @return the absolute difference between the angles of the boat heading and velocity
     * vectors during a sustained turn at a constant speed
     */
    private double phi(final double kLat, final double mass, final double radius, double v) {
        final FunctionAndDerivative f = new FunctionAndDerivative() {
            final double c = 2*mass/(kLat*radius);

            @Override
            public double f(double x) {
                return Math.sin(x) * Math.sin(2*x) - c;
            }

            @Override
            public double fp(double x) {
                return Math.cos(x)*Math.sin(2*x) + 2*Math.sin(x)*Math.cos(2*x);
            }
        };

        final double phi0 = 15*Math.PI/180;
        return  (new NewtonRaphsonRootFinder()).findRoot(f, phi0, 0.000001);
    }

    private double resistanceForceMagnitude(double k, double v) {
        final double vAbs = Math.abs(v);
        if (vAbs < V_TRANSITION) {
            return k*vAbs;
        } else {
            return k*vAbs*vAbs;
        }
    }

    @Override
    public final double getkLon() {
        return kLon;
    }

    @Override
    public final double getkLat() {
        return kLat;
    }

    @Override
    public final double getKLonReverse() {
        return kLonReverse;
    }

    @Override
    public final double getLength() {
        return boatLength;
    }

    @Override
    public final double getCentreOfMassFromStern() {
        return cogDistanceFromStern;
    }

    @Override
    public final double getkRud() {
        return kRud;
    }

    @Override
    public double getRudderAreaFraction() {
        return rudderAreaFraction;
    }

    @Override
    public double getMaximumRudderAngle() {
        return maxRudderAngle;
    }

    @Override
    public double timeToMaximumRudderAnge() {
        return timeToMaxRudderAngle;
    }

    @Override
    public double getRudderLength() {
        return rudderLength;
    }
}
