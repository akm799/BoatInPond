package uk.co.akm.test.sim.boatinpond.boat.impl;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Thanos Mavroidis on 16/12/2017.
 */
public class CentreOfMassTest {

    @Test
    public void shouldCalculateCentreOfMass() {
        final double l = 5;
        final double b = 1;
        final double f = 0.8;
        final SimpleBoatStructure underTest = new SimpleBoatStructure(l, b, 1, f, 1);
        final double x = underTest.getCentreOfMassFromStern();
        Assert.assertTrue(x < l/2);
        Assert.assertTrue(x > l*f/2);
    }

    @Test
    public void shouldCalculateCentreOfMassForDifferentBowSizes() {
        final double l = 5;
        final double b = 1;

        final double f1 = 0.8;
        final SimpleBoatStructure underTest1 = new SimpleBoatStructure(l, b, 1, f1, 1);
        final double x1 = underTest1.getCentreOfMassFromStern();
        Assert.assertTrue(x1 < l/2);
        Assert.assertTrue(x1 > l*f1/2);

        final double f2 = 0.5;
        final SimpleBoatStructure underTest2 = new SimpleBoatStructure(l, b, 1, f2, 1);
        final double x2 = underTest2.getCentreOfMassFromStern();
        Assert.assertTrue(x2 < l/2);
        Assert.assertTrue(x2 > l*f2/2);

        Assert.assertTrue(x2 < x1);
    }
}
