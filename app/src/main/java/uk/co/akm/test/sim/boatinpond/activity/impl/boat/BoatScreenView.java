package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import uk.co.akm.test.sim.boatinpond.activity.OnceOnlyLayoutListener;
import uk.co.akm.test.sim.boatinpond.game.GameConstants;
import uk.co.akm.test.sim.boatinpond.view.ScreenView;
import uk.co.akm.test.sim.boatinpond.view.ViewData;

/**
 * Created by Thanos Mavroidis on 29/11/2017.
 */
final class BoatScreenView extends ScreenView<BoatViewBox> {
    private final int iFr = 20;
    private final int rFr = 2;
    private final Paint shapePaint = new Paint();

    private int cx;
    private int cy;
    private float s;

    private float left;
    private float right;
    private float top;
    private float bottom;

    private float rds;

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

        s = Math.min(w, h)/iFr;
        cx = w/2;
        cy = h/2;

        left = cx - s;
        right = cx + s;
        top = cy - s;
        bottom = cy + s;

        rds = s/rFr;
    }

    @Override
    protected void drawCentralShape(BoatViewBox viewBox, Canvas canvas) {
        drawBoatShape(canvas);
        drawBoatRudder(viewBox.getRudderPlotFractions(), canvas);
    }

    private void drawBoatShape(Canvas canvas) {
        canvas.drawLine(left, bottom, cx, top, shapePaint);
        canvas.drawLine(cx, top, right, bottom, shapePaint);
        canvas.drawLine(right, bottom, left, bottom, shapePaint);
    }

    private void drawBoatRudder(float[] fractions, Canvas canvas) {
        final float dx = rds*fractions[GameConstants.X_INDEX];
        final float dy = rds*fractions[GameConstants.Y_INDEX];
        canvas.drawLine(cx, bottom, cx - dx, bottom + dy, shapePaint);
    }
}
