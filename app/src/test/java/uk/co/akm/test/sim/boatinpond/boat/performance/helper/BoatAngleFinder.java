package uk.co.akm.test.sim.boatinpond.boat.performance.helper;

import junit.framework.Assert;

import uk.co.akm.test.sim.boatinpond.math.root.FunctionAndDerivative;
import uk.co.akm.test.sim.boatinpond.math.root.NewtonRaphsonRootFinder;

/**
 * Estimates the constant angle between the boat's heading and velocity vectors while the boat is in
 * a sustained circular turn at a constant speed.
 */
public final class BoatAngleFinder {
    private final double c;
    private final double v;

    // In order to estimate the angle when the boat's lateral velocity is high, we need to solve a
    // transcendental trigonometric equation.
    private final NewtonRaphsonRootFinder rootFinder = new NewtonRaphsonRootFinder();
    private final FunctionAndDerivative functionAndDerivative = new FunctionAndDerivative() {

        // Function whose root we wish to find.
        @Override
        public double f(double x) {
            final double sn = Math.sin(x);

            return sn*sn*Math.cos(x) - c;
        }

        // First derivative of the function whose root we wish to find.
        @Override
        public double fp(double x) {
            final double sn = Math.sin(x);
            final double cn = Math.cos(x);

            return sn*(2*cn*cn - sn*sn);
        }
    };

    /**
     * The input to these constructor are the relevant boat constants and the sustained circular
     * turn's characteristics.
     *
     * @param mass the boat mass
     * @param kLat the boat lateral water resistance coefficient (i.e the resistance coefficient to the sideways movement of the boat)
     * @param turnRadius the sustained circular turn's radius
     * @param speed the constant turning speed
     */
    public BoatAngleFinder(double mass, double kLat, double turnRadius, double speed) {
        this.c = mass/(kLat*turnRadius);
        this.v = speed;
    }

    /**
     * Asserts that the input (i.e. observed) angle between the boat's heading and velocity vectors,
     * during a sustained circular turn at constant speed, is equal the angle found by this estimator
     * within the input accuracy.
     *
     * @param expectedAngle the expected (i.e. observed) angle in radians
     * @param accuracy the expected maximum (absolute) difference, in radians, between the expected and estimated angle
     */
    public void assertAngleEquals(double expectedAngle, double accuracy) {
        // Angle estimation assuming that the boat's lateral speed is less than the transition speed.
        final double phiLowVLat = estimateAngleAssumingLowVLat();

        // Angle estimation assuming that the boat's lateral speed is greater than the transition speed.
        // This needs to solve a transcendental equation numerically and we use the previous estimate as
        // starting point of our numerical iteration.
        final double phiHighVLat = estimateAngleAssumingHighVLat(phiLowVLat);

        assertAngleEquals(expectedAngle, phiLowVLat, phiHighVLat, accuracy);
    }

    private void assertAngleEquals(double expectedAngle, double actualAngleLowVLat, double actualAngleHighVLat, double accuracy) {
        final double diffLow = Math.abs(actualAngleLowVLat - expectedAngle);
        final double diffHigh = Math.abs(actualAngleHighVLat - expectedAngle);
        final double diff = Math.min(diffLow, diffHigh);
        final double actualAngle = (diff == diffLow ? actualAngleLowVLat : actualAngleHighVLat);
        final String failureMessage = "Expected angle is " + expectedAngle + " radians but the actual angle is " + actualAngle + " radians. The difference is greater than the input accuracy of " + accuracy + " radians.";

        Assert.assertTrue(failureMessage, diff <= accuracy);
    }

    private double estimateAngleAssumingLowVLat() {
        return Math.asin(2*c*v)/2;
    }

    private double estimateAngleAssumingHighVLat(double x0) {
        return rootFinder.findRoot(functionAndDerivative, x0, 1E-18);
    }
}
