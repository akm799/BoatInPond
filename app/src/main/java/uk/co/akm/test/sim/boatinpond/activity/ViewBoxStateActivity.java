package uk.co.akm.test.sim.boatinpond.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import uk.co.akm.test.sim.boatinpond.game.GameConstants;
import uk.co.akm.test.sim.boatinpond.phys.UpdatableState;
import uk.co.akm.test.sim.boatinpond.view.ScreenView;
import uk.co.akm.test.sim.boatinpond.view.ViewData;

/**
 * Created by Thanos Mavroidis on 26/11/2017.
 */
public abstract class ViewBoxStateActivity<T extends UpdatableState, G extends ViewData> extends AbstractStateActivity<T, G> {
    private G viewBox;
    private ScreenView<G> screenView;

    public ViewBoxStateActivity() {
        super(GameConstants.UI_UPDATE_MILLIS, GameConstants.N_UPDATE_STEPS);
    }

    protected abstract int getLayoutId();

    protected abstract int getScreenViewId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        screenView = findViewById(getScreenViewId());
        addLayoutListener(screenView);
    }

    private void addLayoutListener(View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new OnceOnlyLayoutListener(view) {
            @Override
            protected void onGlobalLayoutSingleAction() {
                viewBox = buildViewBox(screenView.getWidth(), screenView.getHeight());
                startAnimation();
            }
        });
    }

    protected abstract G buildViewBox(int viewWidth, int viewHeight);

    protected abstract void startAnimation();

    @Override
    public final G computeRenderingData(T state) {
        viewBox.buildFeatures(state);
        viewBox.additionalData(state);
        return viewBox;
    }

    @Override
    protected final void drawState(G renderingData) {
        screenView.setViewData(viewBox);
        screenView.invalidate();
    }
}
