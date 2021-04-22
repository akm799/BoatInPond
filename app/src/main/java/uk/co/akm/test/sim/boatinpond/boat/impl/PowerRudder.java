package uk.co.akm.test.sim.boatinpond.boat.impl;

import uk.co.akm.test.sim.boatinpond.boat.Rudder;
import uk.co.akm.test.sim.boatinpond.math.MathConstants;

/**
 * Rudder implementation that moves the rudder at a constant speed independent of the boat velocity.
 *
 * Created by Thanos Mavroidis on 02/12/2017.
 */
//TODO Make the class package private.
public class PowerRudder implements Rudder {
    private double angle;
    private double angleOfAttack;
    private int lastInput;

    private final double minAngle;
    private final double maxAngle;
    private final double updateFraction;

    //TODO Make the constructor package private.
    public PowerRudder(double maxAngle, double timeToMaxAngle) {
        checkArguments(maxAngle, timeToMaxAngle);
        this.maxAngle = maxAngle;
        this.minAngle = -maxAngle;
        this.updateFraction = maxAngle/timeToMaxAngle;
    }

    private void checkArguments(double maxAngle, double timeToMaxAngle) {
        if (maxAngle <= 0) {
            throw new IllegalArgumentException("Negative rudder maximum deflection angle: " + maxAngle);
        }

        if (maxAngle > MathConstants.PI_OVER_TWO) {
            throw new IllegalArgumentException("The rudder maximum deflection angle exceeds 90 degrees.");
        }

        if (timeToMaxAngle <= 0) {
            throw new IllegalArgumentException("Negative time to reach the rudder maximum deflection angle: " + timeToMaxAngle);
        }
    }

    @Override
    public final void noControlInput() {
        lastInput = NONE;
    }

    @Override
    public final void leftControlInput() {
        lastInput = LEFT;
    }

    @Override
    public final void rightControlInput() {
        lastInput = RIGHT;
    }

    @Override
    public final double getRudderAngle() {
        return angle;
    }

    @Override
    public final double getMaxRudderAngle() {
        return maxAngle;
    }

    @Override
    public final void update(double dt) {
        if (lastInput != NONE && Math.abs(angle) <= maxAngle) {
            angle += lastInput*updateFraction*dt;

            if (angle > maxAngle) {
                angle = maxAngle;
            }

            if (angle < minAngle) {
                angle = minAngle;
            }

            if (angle < 0) {
                angleOfAttack = -angle;
            } else {
                angleOfAttack = angle;
            }
        }
    }

    double getAngleAbsolute() {
        return angleOfAttack;
    }
}
