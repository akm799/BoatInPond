package uk.co.akm.test.sim.boatinpond.math.helper.impl;

import junit.framework.Assert;

import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.math.Function;
import uk.co.akm.test.sim.boatinpond.math.helper.Integrator;

/**
 * Created by Thanos Mavroidis on 13/12/2017.
 */
public class SimpsonRuleIntegratorTest {
    private final double accuracy = 0.000000000000033;

    @Test
    public void shouldIntegrateQuadraticFunction() {
        final Function xSq = new Function() {
            @Override
            public double f(double x) {
                return x*x;
            }
        };

        final double xMin = 0;
        final double xMax = 3;
        final double expected = 9;

        final Integrator underTest = new SimpsonRuleIntegrator(10000);
        final double actual = underTest.integrate(xSq, xMin, xMax);
        Assert.assertEquals(expected, actual, accuracy);
    }

    @Test
    public void shouldIntegrateQuadraticFunctionSymmetrically() {
        final Function xSq = new Function() {
            @Override
            public double f(double x) {
                return x*x;
            }
        };

        final double xMin = -3;
        final double xMax =  3;
        final double expected = 18;

        final Integrator underTest = new SimpsonRuleIntegrator(10000);
        final double actual = underTest.integrate(xSq, xMin, xMax);
        Assert.assertEquals(expected, actual, accuracy);
    }

    @Test
    public void shouldIntegrateSinFunction() {
        final Function sin = new Function() {
            @Override
            public double f(double x) {
                return Math.sin(x);
            }
        };

        final double xMin = 0;
        final double xMax = Math.PI;
        final double expected = Math.cos(xMin) - Math.cos(xMax);

        final Integrator underTest = new SimpsonRuleIntegrator(10000);
        final double actual = underTest.integrate(sin, xMin, xMax);
        Assert.assertEquals(expected, actual, accuracy);
    }

    @Test
    public void shouldIntegrateCosFunction() {
        final Function cos = new Function() {
            @Override
            public double f(double x) {
                return Math.cos(x);
            }
        };

        final double xMin = -Math.PI/2;
        final double xMax =  Math.PI/2;
        final double expected = Math.sin(xMax) - Math.sin(xMin);

        final Integrator underTest = new SimpsonRuleIntegrator(10000);
        final double actual = underTest.integrate(cos, xMin, xMax);
        Assert.assertEquals(expected, actual, accuracy);
    }

    @Test
    public void shouldReturnNegativeResult() {
        final Function sin = new Function() {
            @Override
            public double f(double x) {
                return Math.sin(x);
            }
        };

        final double xMin = -Math.PI;
        final double xMax = 0;
        final double expected = Math.cos(xMin) - Math.cos(xMax);
        Assert.assertTrue(expected < 0);

        final Integrator underTest = new SimpsonRuleIntegrator(10000);
        final double actual = underTest.integrate(sin, xMin, xMax);
        Assert.assertEquals(expected, actual, accuracy);
    }

    @Test
    public void shouldReturnZeroResult() {
        final Function sin = new Function() {
            @Override
            public double f(double x) {
                return Math.sin(x);
            }
        };

        final double xMin = -Math.PI;
        final double xMax =  Math.PI;
        final double expected = 0;

        final Integrator underTest = new SimpsonRuleIntegrator(10000);
        final double actual = underTest.integrate(sin, xMin, xMax);
        Assert.assertEquals(expected, actual, accuracy);
    }

    @Test
    public void shouldImproveAccuracyWithMoreSteps() {
        final double deviation1 = getDeviation(100);
        final double deviation2 = getDeviation(1000);
        final double deviation3 = getDeviation(10000);

        Assert.assertTrue(deviation2 < deviation1);
        Assert.assertTrue(deviation3 < deviation2);
    }

    private double getDeviation(int n) {
        final Function sin = new Function() {
            @Override
            public double f(double x) {
                return Math.sin(x);
            }
        };

        final double xMin = 0;
        final double xMax = Math.PI;
        final double expected = Math.cos(xMin) - Math.cos(xMax);

        final Integrator underTest = new SimpsonRuleIntegrator(n);
        final double actual = underTest.integrate(sin, xMin, xMax);

        return Math.abs(actual - expected);
    }
}
