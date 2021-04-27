package uk.co.akm.test.sim.boatinpond.boat.performance;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Random;

public class DistributionTest {

    @Test
    public void shouldComputeAverage() {
        final double[] distribution = generateDistribution(100, 10, 20);
        final double expected = average(distribution);

        final Distribution underTest = new Distribution();
        for (double x: distribution) {
            underTest.add(x);
        }

        Assert.assertEquals(expected, underTest.getAverage(), 0);
    }

    @Test
    public void shouldComputeStandardDeviation() {
        final double[] distribution = generateDistribution(1000, -10, 20);
        final double expected = standardDeviation(distribution);

        final Distribution underTest = new Distribution();
        for (double x: distribution) {
            underTest.add(x);
        }

        final double actual = underTest.getStandardDeviation();
        Assert.assertTrue(actual >= 0);
        Assert.assertEquals(expected, actual, 1E-12);
    }

    private double[] generateDistribution(int n, double xMin, double xMax) {
        if (xMin >= xMax) {
            throw new IllegalArgumentException("Inverted distribution range parameters.");
        }

        final double dx = xMax - xMin;
        final double[] distribution = new double[n];
        final Random random = new Random(System.currentTimeMillis());

        for (int i=0 ; i<n ; i++) {
            distribution[i] = xMin + dx*random.nextDouble();
        }

        return distribution;
    }

    private double average(double[] distribution) {
        double s = 0;
        for (double x : distribution) {
            s += x;
        }

        return s/distribution.length;
    }

    private double standardDeviation(double[] distribution) {
        final double m = average(distribution);

        double s = 0;
        for (double x : distribution) {
            s += Math.pow(x - m, 2);
        }

        return Math.sqrt(s/distribution.length);
    }
}
