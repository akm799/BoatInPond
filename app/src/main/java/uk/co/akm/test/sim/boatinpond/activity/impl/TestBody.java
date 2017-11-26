package uk.co.akm.test.sim.boatinpond.activity.impl;


import uk.co.akm.test.sim.boatinpond.phys.Body;

/**
 * Created by Thanos Mavroidis on 26/11/2017.
 */
public final class TestBody extends Body {
    private final double a;

    TestBody(double x0, double y0, double vx0, double vy0, double hdn) {
        super(0, 0, 0, hdn, 0, 0, vx0, vy0, 0, x0, y0, 0);

        final double r = Math.sqrt(x0*x0 + y0*y0);
        final double v = Math.sqrt(vx0*vx0 + vy0*vy0);
        a = v*v/r;
    }

    @Override
    protected void updateAngularAcceleration(double dt) {}

    @Override
    protected void updateAcceleration(double dt) {
        final double x = x();
        final double y = y();
        final double r = Math.sqrt(x*x + y*y);

        ax = -a*x/r;
        ay = -a*y/r;
    }
}