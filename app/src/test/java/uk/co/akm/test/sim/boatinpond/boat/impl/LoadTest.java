package uk.co.akm.test.sim.boatinpond.boat.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Thanos Mavroidis on 16/12/2017.
 */
public class LoadTest {
    // Approximate parameters for a Bosun dinghy.
    private final double length = 4.27;
    private final double beam = 1.68;
    private final double height = 0.5;
    private final double mainBodyFraction = 0.75;
    private final double mass = 168;
    private final double dummyRudderArea = 1;

    private SimpleBoatStructure underTest;

    @Before
    public void setUp() {
        underTest = new SimpleBoatStructure(length, beam, height, mainBodyFraction, mass, dummyRudderArea);
    }

    @Test
    public void shouldComputeDefaultMass() {
        Assert.assertEquals(mass, underTest.getMass());
    }

    @Test
    public void shouldComputeDefaultIncidenceAreas() {
        Assert.assertTrue(underTest.getSideIncidenceArea() > 0);
        Assert.assertTrue(underTest.getFrontalIncidenceArea() > 0);
        Assert.assertTrue(underTest.getSideIncidenceArea() > underTest.getFrontalIncidenceArea());
    }

    @Test
    public void shouldComputeIncidenceAreas() {
        underTest.setLoad(150);
        Assert.assertTrue(underTest.getSideIncidenceArea() > 0);
        Assert.assertTrue(underTest.getFrontalIncidenceArea() > 0);
        Assert.assertTrue(underTest.getSideIncidenceArea() > underTest.getFrontalIncidenceArea());
    }

    @Test
    public void shouldComputeMaxLoad() {
        final double maxLoad = underTest.getMaxLoad();
        Assert.assertTrue(maxLoad > 0);
    }

    @Test
    public void shouldNotExceedLoad() {
        final double maxLoad = underTest.getMaxLoad();

        try {
            underTest.setLoad(maxLoad + 0.001);
            Assert.fail();
        } catch (IllegalStateException ise) {
            Assert.assertEquals("Max load exceeded.", ise.getMessage());
        }
    }

    @Test
    public void shouldCalculateTotalMass() {
        underTest.setLoad(32);
        Assert.assertEquals(200.0, underTest.getMass());
    }
}