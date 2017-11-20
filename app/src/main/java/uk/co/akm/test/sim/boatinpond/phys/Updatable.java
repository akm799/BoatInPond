package uk.co.akm.test.sim.boatinpond.phys;

/**
 * All implementations of this interface have a state that can be updated over a small time increment.
 *
 * Created by Thanos Mavroidis on 11/11/2017.
 */
public interface Updatable {

    /**
     * Updates the body state over a small time increment.
     *
     * @param dt the small time increment
     */
    void update(double dt);
}
