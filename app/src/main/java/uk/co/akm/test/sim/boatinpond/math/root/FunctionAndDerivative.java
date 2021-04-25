package uk.co.akm.test.sim.boatinpond.math.root;

/**
 * Interface representing a single value function and its first derivative.
 */
public interface FunctionAndDerivative {

    /**
     * Returns the value of the function at point x.
     *
     * @param x the point at which the function value will be returned
     * @return the value of the function
     */
    double f(double x);

    /**
     * Returns the value of the first derivative of function at point x.
     *
     * @param x the point at which the function first derivative value will be returned
     * @return the value of the first derivative function
     */
    double fp(double x);
}
