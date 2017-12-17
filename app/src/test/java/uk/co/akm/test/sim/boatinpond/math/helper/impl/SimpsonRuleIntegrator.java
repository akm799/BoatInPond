package uk.co.akm.test.sim.boatinpond.math.helper.impl;

import uk.co.akm.test.sim.boatinpond.math.Function;
import uk.co.akm.test.sim.boatinpond.math.helper.Integrator;

/**
 * Created by Thanos Mavroidis on 13/12/2017.
 */
public final class SimpsonRuleIntegrator implements Integrator {
    private final int n;

    public SimpsonRuleIntegrator(int n) {
        if (n <= 0 || n%2 != 0) {
            throw new IllegalArgumentException("Error: number of steps " + n + " zero, negative or odd.");
        }

        this.n = n;
    }

    @Override
    public double integrate(Function function, double xMin, double xMax) {
        final double h = (xMax - xMin)/n;

        double s = 0;
        for (int j=1 ; j<=n/2 ; j++) {
            final int j2 = 2*j;
            s += (function.f(xj(j2 - 2, xMin, h)) + 4*function.f(xj(j2 - 1, xMin, h)) + function.f(xj(j2, xMin, h)));
        }

        return h*s/3;
    }

    private double xj(int j, double xMin, double h) {
        return xMin + j*h;
    }
}
