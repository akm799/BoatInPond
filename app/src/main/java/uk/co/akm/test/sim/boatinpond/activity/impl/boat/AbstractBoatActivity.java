package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import uk.co.akm.test.sim.boatinpond.activity.ViewBoxStateActivity;
import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;
import uk.co.akm.test.sim.boatinpond.dimensions.BoatDimensionConstants;

/**
 * Created by Thanos Mavroidis on 01/03/2017.
 */
public abstract class AbstractBoatActivity extends ViewBoxStateActivity<Boat, BoatViewBox> {
    private static final int N_STEPS_TO_SKIP_FOR_TEXT_UPDATE = 10;

    private TextView speed;
    private TextView heading;
    private TextView location;
    private TextView leftRudderAngle;
    private TextView rightRudderAngle;

    private int nStepsForTextUpdate;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        checkBaseViewsInit();
        setListeners();
    }

    private void initViews() {
        initBaseViews();
        initAdditionalViews();
    }

    private void initBaseViews() {
        speed = findViewById(getSpeedTextDisplayResId());
        heading = findViewById(getHeadingTextDisplayResId());
        location = findViewById(getPositionTextDisplayResId());
        leftRudderAngle = findViewById(getLeftRudderTextDisplayResId());
        rightRudderAngle = findViewById(getRightRudderTextDisplayResId());
    }

    protected abstract int getSpeedTextDisplayResId();

    protected abstract int getHeadingTextDisplayResId();

    protected abstract int getPositionTextDisplayResId();

    protected abstract int getLeftRudderTextDisplayResId();

    protected abstract int getRightRudderTextDisplayResId();

    protected void initAdditionalViews() {}

    private void checkBaseViewsInit() {
        checkForNull(speed, "Speed display text view has not been initialized.");
        checkForNull(heading, "Heading display text view has not been initialized.");
        checkForNull(location, "Position display text view has not been initialized.");
        checkForNull(leftRudderAngle, "Left rudder deflection display text view has not been initialized.");
        checkForNull(rightRudderAngle, "Right rudder deflection display text view has not been initialized.");
    }

    private void checkForNull(Object ref, String errorMessage) {
        if (ref == null) {
            throw new IllegalStateException(errorMessage);
        }
    }

    private void setListeners() {
        setRudderListeners();
        setAdditionalListeners();
    }

    protected void setAdditionalListeners() {}

    private void setRudderListeners() {
        findViewById(getLeftRudderControlResId()).setOnTouchListener(new RudderListener(this, Rudder.LEFT));
        findViewById(getRightRudderControlResId()).setOnTouchListener(new RudderListener(this, Rudder.RIGHT));
    }

    protected abstract int getLeftRudderControlResId();

    protected abstract int getRightRudderControlResId();

    @Override
    protected final BoatViewBox buildViewBox(int viewWidth, int viewHeight) {
        final double horizontalSide = BoatDimensionConstants.VIEW_BOX_WIDTH;
        final double lineSpacing = BoatDimensionConstants.VIEW_BOX_LINE_SPACING;

        return new BoatViewBox(horizontalSide, lineSpacing, viewWidth, viewHeight);
    }

    @Override
    protected final void startAnimation() {
        startMotion();
    }

    private void startMotion() {
        final Boat boat = boatInstance();
        initState(boat);
        initiate();
    }

    protected abstract Boat boatInstance();

    @Override
    protected final void drawAdditionalData(BoatViewBox renderingData) {
        if (nStepsForTextUpdate == N_STEPS_TO_SKIP_FOR_TEXT_UPDATE) {
            nStepsForTextUpdate = 0;
            updateTextDisplays(renderingData);
            updateAdditionalTextDisplays(renderingData);
        } else {
            nStepsForTextUpdate++;
        }
    }

    private void updateTextDisplays(BoatViewBox renderingData) {
        speed.setText(renderingData.getSpeed());
        heading.setText(renderingData.getCompassHeading());
        location.setText(renderingData.getCoordinates());
        leftRudderAngle.setText(renderingData.getLeftRudderDeflection());
        rightRudderAngle.setText(renderingData.getRightRudderDeflection());
    }

    protected void updateAdditionalTextDisplays(BoatViewBox renderingData) {}

    @Override
    public void onPause() {
        super.onPause();

        if (isRunning()) {
            terminate();
        }

        if (!isFinishing()) { // State persistence not implemented.
            finish();
        }
    }

    private static final class RudderListener implements View.OnTouchListener {
        private final int direction;
        private final AbstractBoatActivity parent;

        RudderListener(AbstractBoatActivity parent, int direction) {
            this.parent = parent;
            this.direction = direction;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            final int btnAction = motionEvent.getAction();

            switch (btnAction) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_DOWN:
                    final Boat boat = parent.getStateReference();
                    final Rudder rudder = (boat == null ? null : boat.getRudder());
                    if (rudder != null) {
                        processButtonAction(btnAction, rudder);
                    }
                    break;

                default: break;
            }

            return true;
        }

        private void processButtonAction(int btnAction, Rudder rudder) {
            switch (btnAction) {
                case MotionEvent.ACTION_UP:
                    rudder.noControlInput();
                    break;

                case MotionEvent.ACTION_DOWN:
                    changeRudderDeflection(rudder);
                    break;

                default: break;
            }
        }

        private void changeRudderDeflection(Rudder rudder) {
            if (direction == Rudder.LEFT) {
                rudder.leftControlInput();
            } else if (direction == Rudder.RIGHT) {
                rudder.rightControlInput();
            }
        }
    }
}
