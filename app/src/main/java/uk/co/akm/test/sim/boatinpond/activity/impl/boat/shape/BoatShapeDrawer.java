package uk.co.akm.test.sim.boatinpond.activity.impl.boat.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import uk.co.akm.test.sim.boatinpond.game.GameConstants;

/**
 * Class responsible for drawing the boat shape at the centre of the parent view.
 *
 * Created by Thanos Mavroidis on 17/03/2018.
 */
public final class BoatShapeDrawer {
    private final Path boatShape;
    private final BoatShapeData shapeData = new BoatShapeData();

    private final float cx;
    private final float left;
    private final float right;
    private final float top;
    private final float bottom;
    private final float bowStartY;
    private final float rudderLength;

    /**
     * @param parent the parent view in which the boat shape will be drawn
     * @param availableWidthFraction the width of the boat in the parent view as a fraction of the
     *                               total width of the parent view
     */
    public BoatShapeDrawer(View parent, float availableWidthFraction) {
        final int height = parent.getHeight();
        final float boatWidth = availableWidthFraction*height;
        final float boatHeight = shapeData.lengthOverBeamRatio*boatWidth;

        cx = parent.getWidth()/2;
        final float cy = height/2;

        left      = cx - boatWidth/2;
        right     = cx + boatWidth/2;
        top       = cy - boatHeight/2;
        bottom    = cy + boatHeight/2;
        bowStartY = bottom - boatHeight*shapeData.mainBodyFraction;
        rudderLength = boatHeight*shapeData.rudderLengthOverTotalLengthRatio;
        boatShape = buildBoatShapePath();
    }

    private Path buildBoatShapePath() {
        final Path path = new Path();
        path.moveTo(left, bottom);
        path.lineTo(left, bowStartY);
        path.lineTo(cx, top);
        path.lineTo(right, bowStartY);
        path.lineTo(right, bottom);
        path.lineTo(left, bottom);

        return path;
    }

    /**
     * @param canvas the canvas where the boat shape will be drawn
     * @param outLinePaint the paint used to draw the boat shape outline
     * @param fillPaint the paint used to fill the boat shape
     * @param fractions the dx and dy fractions representing the rudder deflection angle (i.e.
     *                  dx and dy are such that dx*dx + dy*dy = 1).
     */
    public void drawBoat(Canvas canvas, Paint outLinePaint, Paint fillPaint, float[] fractions) {
        drawBoatBody(canvas, outLinePaint, fillPaint);
        drawBoatRudder(canvas, outLinePaint, fractions);
    }

    private void drawBoatBody(Canvas canvas, Paint outLinePaint, Paint fillPaint) {
        canvas.drawPath(boatShape, fillPaint);
        drawBoatOutline(canvas, outLinePaint);
    }

    private void drawBoatOutline(Canvas canvas, Paint paint) {
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
