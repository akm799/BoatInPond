package uk.co.akm.test.sim.boatinpond.activity.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import uk.co.akm.test.sim.boatinpond.view.ScreenView;
import uk.co.akm.test.sim.boatinpond.view.ViewData;

/**
 * Created by Thanos Mavroidis on 26/11/2017.
 */
final class TestScreenView extends ScreenView {
    private final Paint shapePaint = new Paint();

    public TestScreenView(Context context) {
        super(context);
        init();
    }

    public TestScreenView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        shapePaint.setStrokeWidth(8);
        shapePaint.setColor(0xff000000);
    }

    @Override
    protected void drawCentralShape(ViewData viewBox, Canvas canvas) {
        final int w = getWidth();
        final int h = getHeight();
        final float s = Math.min(w, h)/20;
        final int cx = w/2;
        final int cy = h/2;

        canvas.drawLine(cx - s, cy + s, cx, cy - s, shapePaint);
        canvas.drawLine(cx, cy - s, cx + s, cy + s, shapePaint);
        canvas.drawLine(cx + s, cy + s, cx - s, cy + s, shapePaint);
    }
}
