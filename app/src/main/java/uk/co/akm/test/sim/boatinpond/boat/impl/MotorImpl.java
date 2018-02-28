package uk.co.akm.test.sim.boatinpond.boat.impl;

import uk.co.akm.test.sim.boatinpond.boat.Motor;

/**
 * Created by Thanos Mavroidis on 27/02/2018.
 */
public final class MotorImpl implements Motor {
    private final double maxForce;
    private final double updateFraction;

    private double force;
    private int lastInput;
    private boolean active;

    public MotorImpl(double maxForce, double timeToMaxPower) {
        this.maxForce = maxForce;
        this.updateFraction = maxForce/timeToMaxPower;
    }

    @Override
    public boolean isOn() {
        return active;
    }

    @Override
    public void turnOn() {
        force = 0;
        lastInput = NONE;
        active = true;
    }

    @Override
    public void turnOff() {
        force = 0;
        lastInput = NONE;
        active = false;
    }

    @Override
    public void noControlInput() {
        lastInput = NONE;
    }

    @Override
    public void increaseControlInput() {
        lastInput = INCREASE;
    }

    @Override
    public void decreaseControlInput() {
        lastInput = DECREASE;
    }

    @Override
    public double getForce() {
        return force;
    }

    @Override
    public double getMaxForce() {
        return maxForce;
    }

    @Override
    public void update(double dt) {
        if (active && lastInput != NONE && force >= 0 && force <= maxForce) {
            force += lastInput*updateFraction*dt;

            if (force > maxForce) {
                force = maxForce;
            }

            if (force < 0) {
                force = 0;
            }
        }
    }
}
