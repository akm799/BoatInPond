package uk.co.akm.test.sim.boatinpond.boat;


/**
 * Interface that gives access to all required constants for the motor boat kinematics estimation.
 *
 * Created by Thanos Mavroidis on 28/02/2018.
 */
public interface MotorBoatConstants extends BoatConstants {

    /**
     * @return the maximum possible motor propulsion force
     */
    double getMaxMotorForce();
}
