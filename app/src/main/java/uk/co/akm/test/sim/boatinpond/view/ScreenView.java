package uk.co.akm.test.sim.boatinpond.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import uk.co.akm.test.sim.boatinpond.graph.Line;
import uk.co.akm.test.sim.boatinpond.graph.ViewBoxLines;

/**
 * Created by Thanos Mavroidis on 26/11/2017.
 */
public abstract class ScreenView<G extends ViewBoxLines> extends View {
    private final Paint linesPaint = new Paint();

    private G viewBox;

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
        linesPaint.setColor(0xffff0000);
    }

    public final void setViewBox(G viewBox) {
        this.viewBox = viewBox;
    }

    @Override
    public final void draw(Canvas canvas) {
        super.draw(canvas);

        if (viewBox != null) {
            drawCentralShape(viewBox, canvas);
            drawViewBoxLines(canvas);
        }
    }

    protected abstract void drawCentralShape(G viewBox, Canvas canvas);

    private void drawViewBoxLines(Canvas canvas) {
        final Line[] lines = viewBox.allLines();
        for (int i=0 ; i<viewBox.numberOfSetLines() ; i++) {
            final Line line = lines[i];
            canvas.drawLine(line.startPixel.x, line.startPixel.y, line.endPixel.x, line.endPixel.y, linesPaint);
        }
    }
}
