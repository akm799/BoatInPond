package uk.co.akm.test.sim.boatinpond.game;

import uk.co.akm.test.sim.boatinpond.phys.UpdatableState;

/**
 * Created by Thanos Mavroidis on 20/11/2017.
 */
public interface StateProcessor<T extends UpdatableState, G> {

    /**
     * Returns a reference to the current state.
     *
     * @return a reference to the current state
     */
    T getStateReference();

    /**
     * Computes and returns the data required to render the input state.
     *
     * @param state the state to be rendered
     * @return the data required to render the input state
     */
    G computeRenderingData(T state);

    /**
     * Issues a request to render the state represented by the input rendering data.
     *
     * @param renderingData the rendering data representing the state to be rendered
     */
    void renderState(G renderingData);
}
