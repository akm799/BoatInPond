package uk.co.akm.test.sim.boatinpond.math;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Thanos Mavroidis on 12/11/2017.
 */

public class AnglesTest {
    private final double accuracy = 0.000000000001;

    @Test
    public void shouldConvertToProperRange() {
        testConversion(0, 0);
        testConversion(90, 90);
        testConversion(-90, -90);
        testConversion(180, 180);
        testConversion(-180, -180);
        testConversion(360, 0);
        testConversion(-360, 0);
        testConversion(765, 45);
        testConversion(-765, -45);
        testConversion(270, -90);
        testConversion(-270, 90);
        testConversion(990, -90);
        testConversion(-990, 90);
    }

    private void testConversion(double inputDeg, double expectedDeg) {
        Assert.assertEquals(expectedDeg, toProperAngleDeg(inputDeg), accuracy);
    }

    private double toProperAngleDeg(double deg) {
        final double rad = Angles.toRad(deg);
        final double ap = Angles.toProperAngle(rad);

        return Angles.toDeg(ap);
    }
}
