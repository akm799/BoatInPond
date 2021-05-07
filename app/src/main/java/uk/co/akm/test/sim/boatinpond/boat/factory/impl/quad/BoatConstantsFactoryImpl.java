package uk.co.akm.test.sim.boatinpond.boat.factory.impl.quad;

import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.factory.BoatConstantsFactory;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.BoatConstantsImpl;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.BoatPerformance;

/**
 * Created by Thanos Mavroidis on 11/03/2018.
 */
public final class BoatConstantsFactoryImpl implements BoatConstantsFactory {
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
    private final double turnRadius = 20.66668818928022; // Equivalent to 57 degrees per second turning rate at 40 knots.

    private final double maxRudderAngle = Math.PI/4;
    private final double timeToMaxRudderDeflection = 2;
    private final double boatToRudderLengthRatio = 20;

    private final String indicatorName = "rudder size";
    private final TargetValueCalculator calculator = new TargetValueCalculator();

    @Override
    public BoatConstants instance(int rudderSizeIndicator) {
        calculator.checkIndicator(rudderSizeIndicator, indicatorName);

        // High turning speed means small rudder and low turning speed means large rudder. This is because it is water speed past the rudder, i.e. high water speed gives a large rudder force even for a small rudder.
        final double turningSpeed = calculator.computeValue(MAX_INDICATOR_SIZE - rudderSizeIndicator, minTurningSpeed, maxTurningSpeed, indicatorName);
        final double rudderAreaFraction = calculator.computeValue(rudderSizeIndicator, minRudderAreaFraction, maxRudderAreaFraction, indicatorName);
        final BoatPerformance performance = new BoatPerformance(launchSpeed, distanceLimit, turningSpeed, turnRadius, timeToMaxRudderDeflection);

        return new BoatConstantsImpl(performance, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern, rudderAreaFraction, maxRudderAngle, boatToRudderLengthRatio);
    }
}