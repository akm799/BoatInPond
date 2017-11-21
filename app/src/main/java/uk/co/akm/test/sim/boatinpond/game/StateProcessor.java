package uk.co.akm.test.sim.boatinpond.game;

import uk.co.akm.test.sim.boatinpond.phys.UpdatableState;

/**
 * Created by Thanos Mavroidis on 20/11/2017.
 */
public interface StateProcessor<T extends UpdatableState, G> {

    T getStateReference();

    G computeRenderingData(T state);

    void renderState(G renderingData);
}
