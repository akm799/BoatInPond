package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.Hydrofoil;
import uk.co.akm.test.sim.boatinpond.boat.impl.foil.HydrofoilImpl;

/**
 * Created by Thanos Mavroidis on 24/02/2018.
 */
public class BoatConstantsImpl implements BoatConstants {
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

    public BoatConstantsImpl(BoatPerformance performance,
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
        this.kRud = kRudEstimation(kLat, boatLength, cogDistanceFromStern, performance.turnRate, performance.turningSpeed);
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

    @Deprecated
    private double kRudEstimation(double kLat, double boatLength, double cogDistanceFromStern, double turningRate, double turningSpeed) {
        final double cogDistanceFromBow = (boatLength - cogDistanceFromStern);
        final double omegaSq = turningRate * turningRate;
        final double vSq = turningSpeed * turningSpeed;

        return (kLat/(8*boatLength)) * Math.pow(cogDistanceFromStern, 3) * Math.pow(cogDistanceFromBow, 4) * omegaSq/vSq;
    }

    /**
     * Estimates the rudder torque coefficient assuming that the input turning speed (omega)
     * is higher than the transition angular velocity omega of both the front and aft boat
     * sections.
     */
    private double kRudEstimation2(
            double kLat,
            double boatLength,
            double boatToRudderLengthRatio,
            double cogDistanceFromStern,
            double turningRate,
            double turningSpeed
    ) {
        final Hydrofoil hydrofoil = new HydrofoilImpl();
        final double aoa = hydrofoil.getMaxLiftAngleOfAttack();
        final double halfLength = (boatLength/boatToRudderLengthRatio)/2;
        final double dragCoefficient = hydrofoil.getDragCoefficient(aoa);
        final double liftCoefficient = hydrofoil.getLiftCoefficient(aoa);

        final double torqueDragComponent = dragCoefficient*halfLength*Math.cos(aoa);
        final double torqueLiftComponent = liftCoefficient*(cogDistanceFromStern + halfLength*Math.sin(aoa));
        final double c = torqueDragComponent + torqueLiftComponent;

        final double cogDistanceFromBow = (boatLength - cogDistanceFromStern);
        final double rudderLengthEffective = halfLength*Math.sin(aoa);
        final double cogDistanceFromRudderMidpoint = cogDistanceFromStern + rudderLengthEffective;
        final double af = cogDistanceFromBow/boatLength;
        final double as = cogDistanceFromRudderMidpoint/boatLength;
        final double df3 = Math.pow(cogDistanceFromBow/2, 3);
        final double ds3 = Math.pow(cogDistanceFromRudderMidpoint/2, 3);

        final double omegaSq = turningRate * turningRate;
        final double vSq = turningSpeed * turningSpeed;

        return kLat*omegaSq*(af*df3 + as*ds3)/(c * vSq);
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
