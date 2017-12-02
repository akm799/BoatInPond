package uk.co.akm.test.sim.boatinpond.boat.impl;

import uk.co.akm.test.sim.boatinpond.boat.Rudder;

/**
 * Rudder implementation that moves the rudder at a constant speed independent of the boat velocity.
 *
 * Created by Thanos Mavroidis on 02/12/2017.
 */
public final class PowerRudder implements Rudder {
    private double angle;
    private int lastInput;

    private final double maxAngle;
    private final double updateFraction;

    public PowerRudder(double maxAngle, double timeToMaxAngle) {
        this.maxAngle = maxAngle;
        this.updateFraction = maxAngle/timeToMaxAngle;
    }

    @Override
    public void noControlInput() {
        lastInput = NONE;
    }

    @Override
    public void leftControlInput() {
        lastInput = LEFT;
    }

    @Override
    public void rightControlInput() {
        lastInput = RIGHT;
    }

    @Override
    public double getRudderAngle() {
        return angle;
    }

    @Override
    public double getMaxRudderAngle() {
        return maxAngle;
    }

    @Override
    public void update(double dt) {
        if (lastInput != NONE && Math.abs(angle) < maxAngle) {
            angle += lastInput*updateFraction*dt;

            if (angle > maxAngle) {
                angle = maxAngle;
            }

            if (angle < -maxAngle) {
                angle = -maxAngle;
            }
        }
    }
}
