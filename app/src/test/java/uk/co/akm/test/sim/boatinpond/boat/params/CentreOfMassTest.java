package uk.co.akm.test.sim.boatinpond.boat.params;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Thanos Mavroidis on 10/12/2017.
 */
public class CentreOfMassTest {

    @Test
    public void shouldCalculateCentreOfMass() {
        final double l = 5;
        final double b = 1;
        final double f = 0.8;
        final double x = centreOfMassLengthFromStern(l, b, f);
        Assert.assertTrue(x < l/2);
        Assert.assertTrue(x > l*f/2);
    }

    @Test
    public void shouldCalculateCentreOfMassForDifferentBowSizes() {
        final double l = 5;
        final double b = 1;

        final double f1 = 0.8;
        final double x1 = centreOfMassLengthFromStern(l, b, f1);
        Assert.assertTrue(x1 < l/2);
        Assert.assertTrue(x1 > l*f1/2);

        final double f2 = 0.5;
        final double x2 = centreOfMassLengthFromStern(l, b, f2);
        Assert.assertTrue(x2 < l/2);
        Assert.assertTrue(x2 > l*f2/2);

        Assert.assertTrue(x2 < x1);
    }
    /**
     * Returns the distance of the centre of mass from the stern.
     *
     * @param l length overall
     * @param b beam
     * @param f fraction of the length of the main section (i.e. before the bow section) over the total length (i.e. length overall)
     * @return the distance of the centre of mass from the stern
     */
    private double centreOfMassLengthFromStern(double l, double b, double f) {
        final double ls = l*f;
        final double h = l - ls;
        final double ho2 = h/2;
        final double cf = 1 - 1/Math.sqrt(2);

        final double bowArea = b*ho2;
        final double bowCoM = ls + h*cf;

        final double mainArea = b*ls;
        final double mainCoM = ls/2;

        return (bowCoM*bowArea + mainCoM*mainArea)/(bowArea + mainArea);
    }
}
