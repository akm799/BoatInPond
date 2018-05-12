package uk.co.akm.test.sim.boatinpond.game.impl;

import android.util.Log;

import uk.co.akm.test.sim.boatinpond.game.StateProcessor;
import uk.co.akm.test.sim.boatinpond.game.StateUpdateLoop;
import uk.co.akm.test.sim.boatinpond.phys.UpdatableState;

/**
 * Thread that updates the state and issues the requests to render the new state. This is, effectively,
 * our game loop. It is a very simple game loop implementation that assumes a constant frame rate and
 * that all physics and graphics calculation will always take less time than the time between two
 * consecutive frames. If the time happens to be greater for some update, then this implementation just
 * logs a warning.
 *
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
     * @param stateProcessor the #StateProcessor used to compute data necessary to render the state and to issue
     *                       requests for the state to be rendered
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

        Log.d(getClass().getSimpleName(), "State update loop thread terminated.");
    }

    private void loopStep(T state) {
        final long startMillis = System.currentTimeMillis();

        // Update the state and compute all data require to render it in this thread.
        updateState(state);
        final G renderingData = stateProcessor.computeRenderingData(state);

        // Wait for as long as it takes before rendering the updated state, so that we have a constant frame rate.
        waitIfRequired(startMillis);

        // Use the rendering data we have computed to render the updated state.
        if (loop) {
            stateProcessor.renderState(renderingData); // This rendering will not be performed in this thread, but in the UI thread.
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
            Log.w(getClass().getSimpleName(), "Loop step time exceeded update time (" + uiUpdateMillis + " millis) by " + (-waitMillis) + " millis.");
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

    @Override
    public boolean isRunning() {
        return loop;
    }
}
