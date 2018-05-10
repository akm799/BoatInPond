package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

import junit.framework.Assert;

import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;

/**
 * Created by Thanos Mavroidis on 10/04/2018.
 */
public final class BoatConstantsTest {
    private final double kLatOverKLon = 50;
    private final double kLonReverseOverKLon = 10;
    private final double boatLength = 4;
    private final double cogDistanceFromStern = 1.5;
    private final double rudderAreaFraction = 0.05;

    private final double launchSpeed = 3.01; // 6 knots
    private final double distanceLimit = 75;
    private final double turningSpeed = 9.26; // 18 Knots
    private final double turnRate = 2.5*Math.PI/8; // 56.25 degrees per second

    private final double maxRudderAngle = Math.PI/4;
    private final double timeToMaxRudderDeflection = 2;

    private final double expectedKLon = (1 + Math.log(launchSpeed))/distanceLimit;

    private final BoatPerformance performance = new BoatPerformance(launchSpeed, distanceLimit, turnRate, turningSpeed, timeToMaxRudderDeflection);

    private final BoatConstants underTest = new BoatConstantsImpl(performance, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern, rudderAreaFraction, maxRudderAngle);

    @Test
    public void shouldEvaluateKLon() {
        final double actualKLon = underTest.getkLon();
        Assert.assertEquals(expectedKLon, actualKLon);
    }

    @Test
    public void shouldEvaluateKLat() {
        final double expectedKLat = expectedKLon*kLatOverKLon;
        final double actualKLat = underTest.getkLat();
        Assert.assertEquals(expectedKLat, actualKLat);
    }

    @Test
    public void shouldEvaluateKLonReverse() {
        final double expectedKLonReverse = expectedKLon*kLonReverseOverKLon;
        final double actualKLonReverse = underTest.getKLonReverse();
        Assert.assertEquals(expectedKLonReverse, actualKLonReverse);
    }
}
