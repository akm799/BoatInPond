package uk.co.akm.test.sim.boatinpond.activity.impl;


import uk.co.akm.test.sim.boatinpond.phys.Body;

/**
 * Created by Thanos Mavroidis on 26/11/2017.
 */
public final class TestBody extends Body {

    TestBody(double vy0, double hdn) {
        super(0, 0, 0, hdn, 0, 0, 0, vy0, 0, 0, 0, 0);
    }

    @Override
    protected void updateAngularAcceleration(double dt) {}

    @Override
    protected void updateAcceleration(double dt) {}
}
