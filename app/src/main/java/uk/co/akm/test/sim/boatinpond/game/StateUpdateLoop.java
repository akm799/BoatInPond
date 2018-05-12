package uk.co.akm.test.sim.boatinpond.game;

/**
 * Interface implemented by our state update (game) loop. This loop will update our state and then
 * issue requests to render the updated state.
 *
 * Created by Thanos Mavroidis on 20/11/2017.
 */
public interface StateUpdateLoop {

    /**
     * Starts the loop that continuously updates the state.
     */
    void initiate();

    /**
     * Terminates the state updating loop.
     */
    void terminate();

    /**
     * Returns true if the state updating loop is running or false otherwise.
     *
     * @return true if the state updating loop is running or false otherwise
     */
    boolean isRunning();
}
