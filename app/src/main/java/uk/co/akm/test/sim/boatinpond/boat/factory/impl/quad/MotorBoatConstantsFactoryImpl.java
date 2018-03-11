package uk.co.akm.test.sim.boatinpond.boat.factory.impl.quad;

import uk.co.akm.test.sim.boatinpond.boat.MotorBoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.factory.MotorBoatConstantsFactory;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatConstantsImpl;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatPerformance;

/**
 * Created by Thanos Mavroidis on 11/03/2018.
 */
public final class MotorBoatConstantsFactoryImpl implements MotorBoatConstantsFactory {
    private final double kLatOverKLon = 50;
    private final double kLonReverseOverKLon = 10;
    private final double boatLength = 4;
    private final double cogDistanceFromStern = 1.5;
    private final double minRudderAreaFraction = 0.05;
    private final double maxRudderAreaFraction = 0.1;

    private final double launchSpeed = 3.01; // 6 knots
    private final double distanceLimit = 75;
    private final double minTurningSpeed = 5.14; // 10 Knots
    private final double maxTurningSpeed = 20.56; // 40 Knots
    private final double turnRate = 2.5*Math.PI/8; // 56.25 degrees per second

    private final double minMaxSpeed = 1.03; //  2 Knots
    private final double maxMaxSpeed = 9.26; // 18 Knots

    private final String rudderSizeIndicatorName = "rudder size";
    private final String maxMotorPowerIndicatorName = "maximum available motor power";
    private final TargetValueCalculator calculator = new TargetValueCalculator();

    @Override
    public MotorBoatConstants instance(int rudderSizeIndicator, int maxMotorPowerIndicator) {
        checkArgs(rudderSizeIndicator, maxMotorPowerIndicator);

        // High turning speed means small rudder and low turning speed means large rudder. This is because it is water speed past the rudder, i.e. high water speed gives a large rudder force even for a small rudder.
        final double turningSpeed = calculator.computeValue(MAX_INDICATOR_SIZE - rudderSizeIndicator, minTurningSpeed, maxTurningSpeed, rudderSizeIndicatorName);
        final double rudderAreaFraction = calculator.computeValue(rudderSizeIndicator, minRudderAreaFraction, maxRudderAreaFraction, rudderSizeIndicatorName);
        final double maxSpeed = calculator.computeValue(maxMotorPowerIndicator, minMaxSpeed, maxMaxSpeed, maxMotorPowerIndicatorName);
        final MotorBoatPerformance performance = new MotorBoatPerformance(launchSpeed, distanceLimit, turnRate, turningSpeed, maxSpeed);

        return new MotorBoatConstantsImpl(performance, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern, rudderAreaFraction);
    }

    private void checkArgs(int rudderSizeIndicator, int maxMotorPowerIndicator) {
        calculator.checkIndicator(rudderSizeIndicator, rudderSizeIndicatorName);
        calculator.checkIndicator(maxMotorPowerIndicator, maxMotorPowerIndicatorName);
    }
}
