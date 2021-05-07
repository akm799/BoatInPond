package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

import uk.co.akm.test.sim.boatinpond.boat.MotorBoatConstants;

/**
 * Created by Thanos Mavroidis on 28/02/2018.
 */
public final class MotorBoatConstantsImpl extends BoatConstantsImpl implements MotorBoatConstants {
    private final double maxMotorForce;
    private final double timeToMaxPower;

    public MotorBoatConstantsImpl(MotorBoatPerformance performance,
                                  double kLatOverKLon,
                                  double kLonReverseOverKLon,
                                  double boatLength,
                                  double cogDistanceFromStern,
                                  double rudderAreaFraction,
                                  double maxRudderAngle,
                                  double boatToRudderLengthRatio) {
        super(performance, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern, rudderAreaFraction, maxRudderAngle, boatToRudderLengthRatio);

        maxMotorForce = estimateMaxMotorForce(performance);
        timeToMaxPower = performance.timeToMaxPower;
    }

    private double estimateMaxMotorForce(MotorBoatPerformance performance) {
        final double k = getkLon();
        final double v = performance.maxSpeed;

        return (v >= V_TRANSITION ? k*v*v : k*v);
    }

    @Override
    public double getMaxMotorForce() {
        return maxMotorForce;
    }

    @Override
    public double timeToMaxPower() {
        return timeToMaxPower;
    }
}
