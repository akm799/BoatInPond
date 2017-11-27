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
        testProperRangeConversion(0, 0);
        testProperRangeConversion(90, 90);
        testProperRangeConversion(-90, -90);
        testProperRangeConversion(180, 180);
        testProperRangeConversion(-180, -180);
        testProperRangeConversion(360, 0);
        testProperRangeConversion(-360, 0);
        testProperRangeConversion(765, 45);
        testProperRangeConversion(-765, -45);
        testProperRangeConversion(270, -90);
        testProperRangeConversion(-270, 90);
        testProperRangeConversion(990, -90);
        testProperRangeConversion(-990, 90);
        testProperRangeConversion(185, -175);
        testProperRangeConversion(225, -135);
    }

    private void testProperRangeConversion(double inputDeg, double expectedDeg) {
        Assert.assertEquals(expectedDeg, toProperAngleDeg(inputDeg), accuracy);
    }

    private double toProperAngleDeg(double deg) {
        final double rad = Angles.toRad(deg);
        final double ap = Angles.toProperAngle(rad);

        return Angles.toDeg(ap);
    }

    @Test
    public void testShouldConvertToCompassHeading() {
        testCompassHeadingConversion(90, 0);
        testCompassHeadingConversion(45, 45);
        testCompassHeadingConversion(0, 90);
        testCompassHeadingConversion(-45, 135);
        testCompassHeadingConversion(-90, 180);
        testCompassHeadingConversion(-135, 225);
        testCompassHeadingConversion(180, 270);
        testCompassHeadingConversion(-180, 270);
        testCompassHeadingConversion(135, 315);
    }

    private void testCompassHeadingConversion(double inputDeg, double expectedDeg) {
        Assert.assertEquals(expectedDeg, Angles.toCompassHeading(inputDeg), accuracy);
    }
}
