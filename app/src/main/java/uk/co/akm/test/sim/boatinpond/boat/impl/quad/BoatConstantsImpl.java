package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.TurningPerformance;

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
    private final TurningPerformance turningPerformance;

    public BoatConstantsImpl(double kLon, double kLatOverKLon, double kLonReverseOverKLon, double boatLength, double cogDistanceFromStern, double turnRate, double turningSpeed) {
        checkArgs(boatLength, cogDistanceFromStern, turnRate, turningSpeed, V_TRANSITION);

        this.kLon = kLon;
        this.kLat = kLon*kLatOverKLon;
        this.kLonReverse = kLon*kLonReverseOverKLon;
        this.boatLength = boatLength;
        this.cogDistanceFromStern = cogDistanceFromStern;
        this.turningPerformance = new TurningPerformanceImpl(turnRate, turningSpeed);
        this.kRud = kRudEstimation(kLat, boatLength, cogDistanceFromStern, turningPerformance);
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

    private double kRudEstimation(double kLat, double boatLength, double cogDistanceFromStern, TurningPerformance turningPerformance) {
        final double cogDistanceFromBow = (boatLength - cogDistanceFromStern);
        final double omegaSq = turningPerformance.omega() * turningPerformance.omega();
        final double vSq = turningPerformance.speed() * turningPerformance.speed();

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

    @Override
    public TurningPerformance getTurningPerformance() {
        return turningPerformance;
    }

    private static final class TurningPerformanceImpl implements TurningPerformance {
        private final double turnRate;
        private final double turningSpeed;

        private TurningPerformanceImpl(double turnRate, double turningSpeed) {
            this.turnRate = turnRate;
            this.turningSpeed = turningSpeed;
        }

        @Override
        public double speed() {
            return turningSpeed;
        }

        @Override
        public double omega() {
            return turnRate;
        }
    }
}
