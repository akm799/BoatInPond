package uk.co.akm.test.sim.boatinpond.boat.performance.boat;

import junit.framework.Assert;

import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.BoatConstantsImpl2;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.BoatImpl2;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.BoatPerformance;
import uk.co.akm.test.sim.boatinpond.boat.performance.helper.BoatPerformanceTestHelper;

public class BoatDistanceLimitTest {
    private final double kLatOverKLon = 50;
    private final double kLonReverseOverKLon = 10;
    private final double boatLength = 4;
    private final double cogDistanceFromStern = 1.5;
    private final double rudderAreaFraction = 0.075;

    private final double launchSpeed = 3.01; // 6 knots
    private final double distanceLimit = 75;
    private final double turningSpeed = 20.56; // 40 Knots
    private final double turnRate = 57*Math.PI/180; //TODO Check minimum value so we can have a realistic value.

    private final double maxRudderAngle = Math.PI/4;
    private final double timeToMaxRudderDeflection = 2;
    private final double boatToRudderLengthRatio = 20;

    private final BoatPerformance performance = new BoatPerformance(launchSpeed, distanceLimit, turnRate, turningSpeed, timeToMaxRudderDeflection);
    private final BoatConstantsImpl2 constants = new BoatConstantsImpl2(performance, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern, rudderAreaFraction, maxRudderAngle, boatToRudderLengthRatio);

    private final double dt = 0.0001;

    @Test
    public void shouldReachDistanceLimit() {
        // Launch boat along the positive y-axis direction.
        final Boat underTest = new BoatImpl2(constants, Math.PI/2, launchSpeed);
        Assert.assertEquals(launchSpeed, underTest.v(), 0);

        final double tenMins = 10*60;
        BoatPerformanceTestHelper.update(underTest, dt, tenMins);

        final double v = underTest.v();
        Assert.assertTrue(v > 0);
        Assert.assertTrue(v < 1E-7);

        final double y = underTest.y();
        Assert.assertTrue(y > 0);
        Assert.assertTrue(y < distanceLimit);
        Assert.assertTrue(distanceLimit - y < 1E-3);
    }
}