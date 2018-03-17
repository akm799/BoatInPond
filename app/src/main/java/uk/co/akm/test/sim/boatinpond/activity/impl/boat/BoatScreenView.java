package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import uk.co.akm.test.sim.boatinpond.activity.OnceOnlyLayoutListener;
import uk.co.akm.test.sim.boatinpond.activity.impl.boat.shape.BoatShapeDrawer;
import uk.co.akm.test.sim.boatinpond.view.ScreenView;

/**
 * Created by Thanos Mavroidis on 29/11/2017.
 */
final class BoatScreenView extends ScreenView<BoatViewBox> {
    private final Paint shapePaint = new Paint();

    private BoatShapeDrawer boatShapeDrawer;

    public BoatScreenView(Context context) {
        super(context);
        init();
    }

    public BoatScreenView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoatScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        shapePaint.setStrokeWidth(8);
        shapePaint.setColor(0xff000000);

        getViewTreeObserver().addOnGlobalLayoutListener(new OnceOnlyLayoutListener(this) {
            @Override
            protected void onGlobalLayoutSingleAction() {
                initConstants();
            }
        });
    }

    private void initConstants() {
        final int w = getWidth();
        final int h = getHeight();
        boatShapeDrawer = new BoatShapeDrawer(w, h, 0.075f);
    }

    @Override
    protected void drawCentralShape(BoatViewBox viewBox, Canvas canvas) {
        boatShapeDrawer.drawBoat(canvas, shapePaint, viewBox.getRudderPlotFractions());
    }
}
