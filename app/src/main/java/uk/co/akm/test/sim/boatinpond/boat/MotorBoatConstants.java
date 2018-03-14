package uk.co.akm.test.sim.boatinpond.boat;


/**
 * Interface that gives access to all required constants for the motor boat kinematics estimation.
 *
 * Created by Thanos Mavroidis on 28/02/2018.
 */
public interface MotorBoatConstants extends BoatConstants {

    /**
     * Returns the maximum possible motor propulsion force.
     *
     * @return the maximum possible motor propulsion force
     */
    double getMaxMotorForce();

    /**
     * Returns the time taken for the motor, starting from idle, to reach maximum power.
     *
     * @return the time taken for the motor, starting from idle, to reach maximum power
     */
    double timeToMaxPower();
}
