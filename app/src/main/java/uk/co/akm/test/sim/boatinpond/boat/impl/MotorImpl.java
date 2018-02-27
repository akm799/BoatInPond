package uk.co.akm.test.sim.boatinpond.boat.impl;

import uk.co.akm.test.sim.boatinpond.boat.Motor;

/**
 * Created by Thanos Mavroidis on 27/02/2018.
 */
public final class MotorImpl implements Motor {
    private final double maxPower;
    private final double updateFraction;

    private double power;
    private int lastInput;
    private boolean active;

    public MotorImpl(double maxPower, double timeToMaxPower) {
        this.maxPower = maxPower;
        this.updateFraction = maxPower/timeToMaxPower;
    }

    @Override
    public boolean isOn() {
        return active;
    }

    @Override
    public void turnOn() {
        power = 0;
        lastInput = NONE;
        active = true;
    }

    @Override
    public void turnOff() {
        power = 0;
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
    public double getPower() {
        return power;
    }

    @Override
    public double getMaxPower() {
        return maxPower;
    }

    @Override
    public void update(double dt) {
        if (active && lastInput != NONE && power >= 0 && power <= maxPower) {
            power += lastInput*updateFraction*dt;

            if (power > maxPower) {
                power = maxPower;
            }

            if (power < 0) {
                power = 0;
            }
        }
    }
}
