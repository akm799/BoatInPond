package uk.co.akm.test.sim.boatinpond.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import uk.co.akm.test.sim.boatinpond.graph.Line;
import uk.co.akm.test.sim.boatinpond.graph.Pixel;
import uk.co.akm.test.sim.boatinpond.graph.Point;

/**
 * Created by Thanos Mavroidis on 26/11/2017.
 */
public abstract class ScreenView<G extends ViewData> extends View {
    private final Paint linesPaint = new Paint();
    private final Paint pointsPaint = new Paint();
    private final Paint borderPaint = new Paint();

    private int linesColour = 0xffff0000;
    private int pointsColour = 0xff00ff00;
    private int borderColour = 0xff000000;

    private int pointsHalfSide = 8;

    private G viewData;

    public ScreenView(Context context) {
        super(context);
        init();
    }

    public ScreenView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScreenView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linesPaint.setColor(linesColour);
        pointsPaint.setColor(pointsColour);
        borderPaint.setColor(borderColour);

        pointsPaint.setStrokeWidth(pointsHalfSide);
    }

    public final void setViewData(G viewData) {
        this.viewData = viewData;
    }

    @Override
    public final void draw(Canvas canvas) {
        super.draw(canvas);

        drawBorder(canvas);

        if (viewData != null) {
            drawCentralShape(viewData, canvas);
            drawViewBoxLines(canvas);
            drawFixedPoints(canvas);
        }
    }

    private void drawBorder(Canvas canvas) {
        final int xMax = getWidth() - 1;
        final int yMax = getHeight() - 1;

        canvas.drawLine(0, 0, xMax , 0, borderPaint);
        canvas.drawLine(xMax, 0, xMax, yMax, borderPaint);
        canvas.drawLine(xMax, yMax, 0, yMax, borderPaint);
        canvas.drawLine(0, yMax, 0, 0, borderPaint);

    }

    protected abstract void drawCentralShape(G viewBox, Canvas canvas);

    private void drawViewBoxLines(Canvas canvas) {
        final Line[] lines = viewData.allLines();
        for (int i = 0; i< viewData.numberOfSetLines() ; i++) {
            final Line line = lines[i];
            canvas.drawLine(line.startPixel.x, line.startPixel.y, line.endPixel.x, line.endPixel.y, linesPaint);
        }
    }

    private void drawFixedPoints(Canvas canvas) {
        final int number = viewData.numberOfSetFixedPoints();
        if (number > 0) {
            final Point[] fixedPoints = viewData.allFixedPoints();
            for (int i=0 ; i<number ; i++) {
                final Pixel p = fixedPoints[i].pixel;
                canvas.drawRect(p.x - pointsHalfSide, p.y - pointsHalfSide, p.x + pointsHalfSide, p.y + pointsHalfSide, pointsPaint);
            }
        }
    }
}
