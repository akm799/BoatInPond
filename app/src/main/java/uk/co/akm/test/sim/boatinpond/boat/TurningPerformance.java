package uk.co.akm.test.sim.boatinpond.boat;

/**
 * Defines the target boat turning performance as the turn rate achieved by the boat at a specified speed.
 *
 * Created by Thanos Mavroidis on 24/02/2018.
 */
public interface TurningPerformance {

    /**
     * @return the boat speed of the turning boat
     */
    double speed();

    /**
     * @return the turn rate (angular velocity) of the turning boat at the given speed
     */
    double omega();
}
