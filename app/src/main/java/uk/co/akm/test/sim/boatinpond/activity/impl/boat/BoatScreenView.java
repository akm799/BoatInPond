package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

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
final class BoatScreenView extends ScreenView<BoatViewBox> {
    private final int iFr = 20;
    private final int rFr = 4;
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

        // Draw boat shape
        canvas.drawLine(cx - s, cy + s, cx, cy - s, shapePaint);
        canvas.drawLine(cx, cy - s, cx + s, cy + s, shapePaint);
        canvas.drawLine(cx + s, cy + s, cx - s, cy + s, shapePaint);

        // Draw rudder
        final float rds = s/rFr;
        final float rdDfl = rds*viewBox.getRudderDeflectionFraction();
        canvas.drawLine(cx, cy + s, cx - rds*rdDfl, cy + s + rds, shapePaint);
    }
}
