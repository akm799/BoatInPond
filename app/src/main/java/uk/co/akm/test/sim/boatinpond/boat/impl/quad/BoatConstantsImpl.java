package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;

/**
 * Created by Thanos Mavroidis on 24/02/2018.
 */
public final class BoatConstantsImpl implements BoatConstants {
    private final double kLon;
    private final double kLat;
    private final double kLonReverse;
    private final double kRud;
    private final double boatLength;
    private final double cogDistanceFromStern;

    public BoatConstantsImpl(BoatPerformance performance, double kLatOverKLon, double kLonReverseOverKLon, double boatLength, double cogDistanceFromStern) {
        checkArgs(boatLength, cogDistanceFromStern, performance.turnRate, performance.turningSpeed, V_TRANSITION);

        this.kLon = kLonEstimation(performance.launchSpeed, performance.distanceLimit);
        this.kLat = kLon*kLatOverKLon;
        this.kLonReverse = kLon*kLonReverseOverKLon;
        this.boatLength = boatLength;
        this.cogDistanceFromStern = cogDistanceFromStern;
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
        return (1 + Math.log(launchSpeed))/distanceLimit;
    }

    private double kRudEstimation(double kLat, double boatLength, double cogDistanceFromStern, double turningRate, double turningSpeed) {
        final double cogDistanceFromBow = (boatLength - cogDistanceFromStern);
        final double omegaSq = turningRate * turningRate;
        final double vSq = turningSpeed * turningSpeed;

        return (kLat/(8*boatLength)) * Math.pow(cogDistanceFromStern, 3) * Math.pow(cogDistanceFromBow, 4) * omegaSq/vSq;
    }

    @Override
    public double getkLon() {
        return kLon;
    }

    @Override
    public double getkLat() {
        return kLat;
    }

    @Override
    public double getKLonReverse() {
        return kLonReverse;
    }

    @Override
    public double getLength() {
        return boatLength;
    }

    @Override
    public double getCentreOfMassFromStern() {
        return cogDistanceFromStern;
    }

    @Override
    public double getkRud() {
        return kRud;
    }
}
