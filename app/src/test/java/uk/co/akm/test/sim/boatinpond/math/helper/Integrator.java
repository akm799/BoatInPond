package uk.co.akm.test.sim.boatinpond.math.helper;

/**
 * Created by Thanos Mavroidis on 13/12/2017.
 */
public interface Integrator {

    double integrate(Function function, double xMin, double xMax);
}
