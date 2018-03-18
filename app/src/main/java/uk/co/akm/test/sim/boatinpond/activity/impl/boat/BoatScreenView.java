package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import uk.co.akm.test.sim.boatinpond.R;
import uk.co.akm.test.sim.boatinpond.activity.OnceOnlyLayoutListener;
import uk.co.akm.test.sim.boatinpond.activity.impl.boat.shape.BoatShapeDrawer;
import uk.co.akm.test.sim.boatinpond.dimensions.BoatDimensionConstants;
import uk.co.akm.test.sim.boatinpond.view.ScreenView;

/**
 * Created by Thanos Mavroidis on 29/11/2017.
 */
final class BoatScreenView extends ScreenView<BoatViewBox> {
    private final Paint fillPaint = new Paint();
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
        initPaints(getContext());

        getViewTreeObserver().addOnGlobalLayoutListener(new OnceOnlyLayoutListener(this) {
            @Override
            protected void onGlobalLayoutSingleAction() {
                initConstants();
            }
        });
    }

    private void initPaints(Context context) {


        shapePaint.setColor(ContextCompat.getColor(context, R.color.colorBlack));

        fillPaint.setColor(ContextCompat.getColor(context, R.color.colorWoodBrown));
        fillPaint.setStyle(Paint.Style.FILL);
    }

    private void initConstants() {
        final float availableWidthFraction  = (float)(BoatDimensionConstants.BOAT_BEAM/BoatDimensionConstants.VIEW_BOX_WIDTH);
        boatShapeDrawer = new BoatShapeDrawer(this, availableWidthFraction);

        final float shapePaintStrokeWidth = getWidth()*availableWidthFraction*boatShapeDrawer.getOutlineWidthOverBeamRatio();
        shapePaint.setStrokeWidth(shapePaintStrokeWidth);
    }

    @Override
    protected void drawCentralShape(BoatViewBox viewBox, Canvas canvas) {
        boatShapeDrawer.drawBoat(canvas, shapePaint, fillPaint, viewBox.getRudderPlotFractions());
    }
}
