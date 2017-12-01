package uk.co.akm.test.sim.boatinpond.activity.impl.test;


import uk.co.akm.test.sim.boatinpond.phys.Body;
import uk.co.akm.test.sim.boatinpond.phys.State;

/**
 * Simulating circular motion for testing.
 *
 * Created by Thanos Mavroidis on 26/11/2017.
 */
final class TestBody extends Body {
    private final double a;

    private static double omega(double x0, double y0, double vx0, double vy0) {
        final double r = Math.sqrt(x0*x0 + y0*y0);
        final double v = Math.sqrt(vx0*vx0 + vy0*vy0);

        return v/r;
    }

    TestBody(double x0, double y0, double vx0, double vy0, double hdn0) {
        super(omega(x0, y0, vx0, vy0), 0, 0, hdn0, 0, 0, vx0, vy0, 0, x0, y0, 0);

        final double r = Math.sqrt(x0*x0 + y0*y0);
        final double v = Math.sqrt(vx0*vx0 + vy0*vy0);
        a = v*v/r;
    }

    @Override
    protected void updateAngularAcceleration(State start, double dt) {}

    @Override
    protected void updateAcceleration(State start, double dt) {
        final double x = x();
        final double y = y();
        final double r = Math.sqrt(x*x + y*y);

        ax = -a*x/r;
        ay = -a*y/r;
    }
}
