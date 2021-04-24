package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

import uk.co.akm.test.sim.boatinpond.boat.Motor;
import uk.co.akm.test.sim.boatinpond.boat.MotorBoat;
import uk.co.akm.test.sim.boatinpond.boat.MotorBoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.impl.MotorImpl;

/**
 * Created by Thanos Mavroidis on 24/02/2018.
 */
public final class MotorBoatImpl2 extends BoatImpl2 implements MotorBoat {
    private final Motor motor;

    public MotorBoatImpl2(MotorBoatConstants constants, double hdn0, double v0) {
        super(constants, hdn0, v0);

        motor = new MotorImpl(constants.getMaxMotorForce(), constants.timeToMaxPower());
    }

    @Override
    protected void updateAdditionalControls(double dt) {
        motor.update(dt);
    }

    @Override
    protected double estimatePropulsionForce() {
        return motor.getForce();
    }

    @Override
    public Motor getMotor() {
        return motor;
    }
}