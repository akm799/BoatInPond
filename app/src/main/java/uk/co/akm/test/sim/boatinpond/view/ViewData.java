package uk.co.akm.test.sim.boatinpond.view;

import uk.co.akm.test.sim.boatinpond.graph.ViewBoxLines;
import uk.co.akm.test.sim.boatinpond.phys.UpdatableState;

/**
 * Created by Thanos Mavroidis on 27/11/2017.
 */
public interface ViewData<T extends UpdatableState> extends ViewBoxLines {

    void additionalData(T state);
}
