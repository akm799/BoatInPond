package uk.co.akm.test.sim.boatinpond.graph;

import uk.co.akm.test.sim.boatinpond.phys.State;

/**
 * Created by Thanos Mavroidis on 29/11/2017.
 */
public class Mock2DState implements State {
    private double x;
    private double y;
    private double hdn;

    Mock2DState(double x, double y, double hdn) {
        this.x = x;
        this.y = y;
        this.hdn = hdn;
    }

    @Override
    public double aHdn() {
        return 0;
    }

    @Override
    public double aAzm() {
        return 0;
    }

    @Override
    public double aRll() {
        return 0;
    }

    @Override
    public double omgHdn() {
        return 0;
    }

    @Override
    public double omgAmz() {
        return 0;
    }

    @Override
    public double omgRll() {
        return 0;
    }

    @Override
    public double hdn() {
        return hdn;
    }

    @Override
    public double hdnP() {
        return 0;
    }

    @Override
    public double azm() {
        return 0;
    }

    @Override
    public double azmP() {
        return 0;
    }

    @Override
    public double rll() {
        return 0;
    }

    @Override
    public double rllP() {
        return 0;
    }

    @Override
    public double ax() {
        return 0;
    }

    @Override
    public double ay() {
        return 0;
    }

    @Override
    public double az() {
        return 0;
    }

    @Override
    public double vx() {
        return 0;
    }

    @Override
    public double vy() {
        return 0;
    }

    @Override
    public double vz() {
        return 0;
    }

    @Override
    public double v() {
        return 0;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return 0;
    }

    @Override
    public double t() {
        return 0;
    }
}
