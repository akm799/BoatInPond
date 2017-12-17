package uk.co.akm.test.sim.boatinpond.boat.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Thanos Mavroidis on 17/12/2017.
 */
public class DragCoefficientTest {
    // Approximate parameters for a Bosun dinghy.
    private final double length = 4.27;
    private final double beam = 1.68;
    private final double height = 0.5;
    private final double mainBodyFraction = 0.75;
    private final double mass = 168;

    private SimpleBoatStructure underTest;

    @Before
    public void setUp() {
        underTest = new SimpleBoatStructure(length, beam, height, mainBodyFraction, mass);
    }

    @Test
    public void shouldCalculateDragCoefficients() {
        final double cdLon = underTest.getLongitudinalDragCoefficient();
        final double cdLat = underTest.getLateralDragCoefficient();

        Assert.assertTrue(cdLon > 0);
        Assert.assertTrue(cdLat > 0);
        Assert.assertTrue(cdLat > cdLon);
    }

    @Test
    public void shouldCalculateLongitudinalDragCoefficient() {
        final double f1 = 0.75;
        final SimpleBoatStructure underTest1 = new SimpleBoatStructure(length, beam, height, f1, mass);
        final double cdLon1 = underTest1.getLongitudinalDragCoefficient();

        final double f2 = 0.4;
        final SimpleBoatStructure underTest2 = new SimpleBoatStructure(length, beam, height, f2, mass);
        final double cdLon2 = underTest2.getLongitudinalDragCoefficient();

        Assert.assertTrue(cdLon1 > 0);
        Assert.assertTrue(cdLon2 > 0);
        Assert.assertTrue(cdLon1 > cdLon2);
    }
}
