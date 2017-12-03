package uk.co.akm.test.sim.boatinpond.activity;

import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Layout listener that performs its action only once and then immediately proceeds to remove itself
 * from the view tree observer of the view it is listening to.
 *
 * Created by Thanos Mavroidis on 03/12/2017.
 */
public abstract class OnceOnlyLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
    private final View parent;

    public OnceOnlyLayoutListener(View parent) {
        this.parent = parent;
    }

    @Override
    public final void onGlobalLayout() {
        try {
            onGlobalLayoutSingleAction();
        } finally {
            parent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    /**
     * Performs the once-only action.
     */
    protected abstract void onGlobalLayoutSingleAction();
}
