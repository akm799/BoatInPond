package uk.co.akm.test.sim.boatinpond.activity;

import android.support.v7.app.AppCompatActivity;

import uk.co.akm.test.sim.boatinpond.game.StateProcessor;
import uk.co.akm.test.sim.boatinpond.game.StateUpdateLoop;
import uk.co.akm.test.sim.boatinpond.game.impl.StateUpdateLoopThread;
import uk.co.akm.test.sim.boatinpond.phys.UpdatableState;

/**
 * Created by Thanos Mavroidis on 25/11/2017.
 */
public abstract class AbstractStateActivity<T extends UpdatableState, G> extends AppCompatActivity implements StateProcessor<T, G>, StateUpdateLoop {
    private final long uiUpdateMillis;
    private final int nUpdateSteps;
    private final RenderRunnable<G> renderRunnable;

    private T state;
    private StateUpdateLoop stateUpdateLoop;

    protected AbstractStateActivity(long uiUpdateMillis, int nUpdateSteps) {
        this.uiUpdateMillis = uiUpdateMillis;
        this.nUpdateSteps = nUpdateSteps;
        this.renderRunnable = new RenderRunnable<G>(this);
    }

    protected final void initState(T state) {
        this.state = state;
    }

    @Override
    public final T getStateReference() {
        return state;
    }

    @Override
    public final void initiate() {
        stateUpdateLoop = new StateUpdateLoopThread<T, G>(this, uiUpdateMillis, nUpdateSteps);
        stateUpdateLoop.initiate();
    }

    @Override
    public final void renderState(G renderingData) {
        renderRunnable.setRenderingData(renderingData);
        runOnUiThread(renderRunnable);
    }

    protected abstract void drawState(G renderingData);

    protected abstract void drawAdditionalData(G renderingData);

    @Override
    public final void terminate() {
        if (stateUpdateLoop != null) {
            stateUpdateLoop.terminate();
            stateUpdateLoop = null;
        }
    }

    @Override
    public final boolean isRunning() {
        return (stateUpdateLoop != null && stateUpdateLoop.isRunning());
    }

    private static final class RenderRunnable<G> implements Runnable {
        private final AbstractStateActivity<?, G> parent;

        private G renderingData;

        RenderRunnable(AbstractStateActivity<?, G> parent) {
            this.parent = parent;
        }

        void setRenderingData(G renderingData) {
            this.renderingData = renderingData;
        }

        @Override
        public void run() {
            if (renderingData != null) {
                parent.drawState(renderingData);
                parent.drawAdditionalData(renderingData);
            }
        }
    }
}
