package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import uk.co.akm.test.sim.boatinpond.view.ScreenView;
import uk.co.akm.test.sim.boatinpond.view.ViewData;

/**
 * Created by Thanos Mavroidis on 29/11/2017.
 */
final class BoatScreenView extends ScreenView<BoatViewBox> {
    private final int iFr = 20;
    private final int rFr = 2;
    private final Paint shapePaint = new Paint();

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
    }

    @Override
    protected void drawCentralShape(BoatViewBox viewBox, Canvas canvas) {
        final int w = getWidth();
        final int h = getHeight();
        final float s = Math.min(w, h)/iFr;
        final int cx = w/2;
        final int cy = h/2;

        drawBoatShape(cx, cy, s, canvas);
        drawBoatRudder(cx, cy, s, viewBox.getRudderPlotFractions(), canvas);
    }

    private void drawBoatShape(float cx, float cy, float s, Canvas canvas) {
        final float left = cx - s;
        final float right = cx + s;
        final float top = cy - s;
        final float bottom = cy + s;
        canvas.drawLine(left, bottom, cx, top, shapePaint);
        canvas.drawLine(cx, top, right, bottom, shapePaint);
        canvas.drawLine(right, bottom, left, bottom, shapePaint);
    }

    private void drawBoatRudder(float cx, float cy, float s, float[] fractions, Canvas canvas) {
        final float rds = s/rFr;
        final float dx = rds*fractions[0];
        final float dy = rds*fractions[1];
        final float bottom = cy + s;
        canvas.drawLine(cx, bottom, cx - dx, bottom + dy, shapePaint);
    }
}
