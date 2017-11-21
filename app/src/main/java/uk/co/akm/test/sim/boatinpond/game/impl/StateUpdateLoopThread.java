package uk.co.akm.test.sim.boatinpond.game.impl;

import android.util.Log;

import uk.co.akm.test.sim.boatinpond.game.StateProcessor;
import uk.co.akm.test.sim.boatinpond.game.StateUpdateLoop;
import uk.co.akm.test.sim.boatinpond.phys.UpdatableState;

/**
 * Created by Thanos Mavroidis on 20/11/2017.
 */
public final class StateUpdateLoopThread<T extends UpdatableState, G> extends Thread implements StateUpdateLoop {
    private final long dt;
    private final long dtMillis;
    private final StateProcessor<T, G> stateProcessor;

    private final T state;

    private boolean loop;

    public StateUpdateLoopThread(StateProcessor<T, G> stateProcessor, long dt) {
        this.dt = dt;
        this.dtMillis = 1000*dt;
        this.stateProcessor = stateProcessor;
        this.state = stateProcessor.getStateReference();
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
        state.update(dt);
        final G renderingData = stateProcessor.computeRenderingData(state);
        waitIfRequired(startMillis);

        stateProcessor.renderState(renderingData);
    }

    private void waitIfRequired(long startMillis) {
        final long evalMillis = (System.currentTimeMillis() - startMillis);
        final long waitMillis = (dtMillis - evalMillis);
        if (waitMillis > 0) {
            waitFor(waitMillis);
        } else if (waitMillis < 0) {
            Log.d(getClass().getSimpleName(), "Loop step time exceeded update time (" + dtMillis + " millis) by " + (-waitMillis) + " millis.");
        }
    }

    private void waitFor(long waitMillis) {
        try {
            Thread.sleep(waitMillis);
        } catch (InterruptedException ignore) {}
    }

    @Override
    public void terminate() {
        loop = false;
    }
}
