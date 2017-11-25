package uk.co.akm.test.sim.boatinpond.game.impl;

import android.util.Log;

import uk.co.akm.test.sim.boatinpond.game.StateProcessor;
import uk.co.akm.test.sim.boatinpond.game.StateUpdateLoop;
import uk.co.akm.test.sim.boatinpond.phys.UpdatableState;

/**
 * Created by Thanos Mavroidis on 20/11/2017.
 */
public final class StateUpdateLoopThread<T extends UpdatableState, G> extends Thread implements StateUpdateLoop {
    private final double dt;
    private final long uiUpdateMillis;
    private final int nUpdateSteps;

    private final StateProcessor<T, G> stateProcessor;

    private final T state;

    private boolean loop;

    /**
     *
     * @param stateProcessor
     * @param uiUpdateMillis the time interval, in milliseconds, in which the state is rendered in the UI
     * @param nUpdateSteps the number of state updates to be calculated internally for the UI update time interval
     */
    public StateUpdateLoopThread(StateProcessor<T, G> stateProcessor, long uiUpdateMillis, int nUpdateSteps) {
        this.uiUpdateMillis = uiUpdateMillis;
        this.nUpdateSteps = nUpdateSteps;
        this.dt = (uiUpdateMillis/1000.0)/nUpdateSteps;
        this.stateProcessor = stateProcessor;
        this.state = stateProcessor.getStateReference();

        if (state == null) {
            throw new IllegalArgumentException("Cannot construct a " + getClass().getSimpleName() + " instance with a " + StateProcessor.class.getSimpleName() + " instance that gives a null state. It is not possible to update a null state!");
        }
    }

    @Override
    public void initiate() {
        loop = true;
        start();
    }

    @Override
    public void run() {
        loop();
    }

    private void loop() {
        while (loop) {
            loopStep(state);
        }
    }

    private void loopStep(T state) {
        final long startMillis = System.currentTimeMillis();
        updateState(state);
        final G renderingData = stateProcessor.computeRenderingData(state);
        waitIfRequired(startMillis);

        if (loop) {
            stateProcessor.renderState(renderingData);
        }
    }

    private void updateState(T state) {
        for (int i=0 ; i<nUpdateSteps ; i++) {
            state.update(dt);
        }
    }

    private void waitIfRequired(long startMillis) {
        final long evalMillis = (System.currentTimeMillis() - startMillis);
        final long waitMillis = (uiUpdateMillis - evalMillis);
        if (waitMillis > 0) {
            waitFor(waitMillis);
        } else if (waitMillis < 0) {
            Log.d(getClass().getSimpleName(), "Loop step time exceeded update time (" + uiUpdateMillis + " millis) by " + (-waitMillis) + " millis.");
        }
    }

    private void waitFor(long waitMillis) {
        try {
            Thread.sleep(waitMillis);
        } catch (InterruptedException ie) {
            loop = false;
        }
    }

    @Override
    public void terminate() {
        loop = false;
        interrupt();
    }
}
