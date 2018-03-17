package uk.co.akm.test.sim.boatinpond.activity.impl.boat.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import uk.co.akm.test.sim.boatinpond.game.GameConstants;

/**
 * Class responsible for drawing the boat shape at the centre of the parent view.
 *
 * Created by Thanos Mavroidis on 17/03/2018.
 */
public final class BoatShapeDrawer {
    private final BoatShapeData shapeData = new BoatShapeData();

    private final float cx;
    private final float left;
    private final float right;
    private final float top;
    private final float bottom;
    private final float bowStartY;
    private final float rudderLength;

    /**
     * @param viewWidth the parent view width
     * @param viewHeight the parent view height
     * @param availableWidthFraction the width of the boat in the parent view as a fraction of the
     *                               total width of the parent view
     */
    public BoatShapeDrawer(int viewWidth, int viewHeight, float availableWidthFraction) {
        final float boatWidth = availableWidthFraction*viewHeight;
        final float boatHeight = shapeData.lengthOverBeamRatio*boatWidth;

        cx = viewWidth/2;
        final float cy = viewHeight/2;

        left      = cx - boatWidth/2;
        right     = cx + boatWidth/2;
        top       = cy - boatHeight/2;
        bottom    = cy + boatHeight/2;
        bowStartY = bottom - boatHeight*shapeData.mainBodyFraction;
        rudderLength = boatHeight*shapeData.rudderLengthOverTotalLengthRatio;
    }

    /**
     * @param canvas the canvas where the boat shape will be drawn
     * @param paint the paint used to draw the boat shape
     * @param fractions the dx and dy fractions representing the rudder deflection angle (i.e.
     *                  dx and dy are such that dx*dx + dy*dy = 1).
     */
    public void drawBoat(Canvas canvas, Paint paint, float[] fractions) {
        drawBoatBody(canvas, paint);
        drawBoatRudder(canvas, paint, fractions);
    }

    private void drawBoatBody(Canvas canvas, Paint paint) {
        canvas.drawLine(left, bottom, left, bowStartY, paint);
        canvas.drawLine(left, bowStartY, cx, top, paint);
        canvas.drawLine(cx, top, right, bowStartY, paint);
        canvas.drawLine(right, bowStartY, right, bottom, paint);
        canvas.drawLine(right, bottom, left, bottom, paint);
    }

    private void drawBoatRudder(Canvas canvas, Paint paint, float[] fractions) {
        final float dx = rudderLength*fractions[GameConstants.X_INDEX];
        final float dy = rudderLength*fractions[GameConstants.Y_INDEX];
        canvas.drawLine(cx, bottom, cx - dx, bottom + dy, paint);
    }
}
