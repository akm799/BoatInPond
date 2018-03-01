package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.co.akm.test.sim.boatinpond.activity.ViewBoxStateActivity;
import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;

/**
 * Created by Thanos Mavroidis on 01/03/2017.
 */
public abstract class AbstractBoatActivity extends ViewBoxStateActivity<Boat, BoatViewBox> {
    private Button commandBtn;

    private TextView speed;
    private TextView heading;
    private TextView location;

    private int nTextSkip;

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
        commandBtn = findViewById(getCommandButtonResId());
    }

    protected abstract int getSpeedTextDisplayResId();

    protected abstract int getHeadingTextDisplayResId();

    protected abstract int getPositionTextDisplayResId();

    protected abstract int getCommandButtonResId();

    protected void initAdditionalViews() {}

    private void checkBaseViewsInit() {
        checkForNull(speed, "Speed display text view has not been initialized.");
        checkForNull(heading, "Heading display text view has not been initialized.");
        checkForNull(location, "Position display text view has not been initialized.");
        checkForNull(commandBtn, "Start/stop button has not been initialized.");
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
        return new BoatViewBox(30, 5, viewWidth, viewHeight);
    }

    @Override
    protected final void drawAdditionalData(BoatViewBox renderingData) {
        if (nTextSkip == 10) {
            nTextSkip = 0;
            updateTextDisplays(renderingData);
            updateAdditionalTextDisplays(renderingData);
        } else {
            nTextSkip++;
        }
    }

    private void updateTextDisplays(BoatViewBox renderingData) {
        speed.setText(renderingData.getSpeed());
        heading.setText(renderingData.getCompassHeading());
        location.setText(renderingData.getCoordinates());
    }

    protected void updateAdditionalTextDisplays(BoatViewBox renderingData) {}

    @Override
    public void onPause() {
        super.onPause();

        if (isRunning()) {
            terminate();
        }
    }

    public final void onCommand(View view) {
        if (isRunning()) {
            terminate();
            commandBtn.setText("Start");
        } else {
            startMotion();
            commandBtn.setText("Stop");
        }
    }

    private void startMotion() {
        final Boat boat = boatInstance();
        initState(boat);
        initiate();
    }

    protected abstract Boat boatInstance();

    private static final class RudderListener implements View.OnTouchListener {
        private final int direction;
        private final AbstractBoatActivity parent;

        RudderListener(AbstractBoatActivity parent, int direction) {
            this.parent = parent;
            this.direction = direction;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                final Boat boat = parent.getStateReference();
                if (boat != null) {
                    if (direction == Rudder.LEFT) {
                        boat.getRudder().leftControlInput();
                    } else if (direction == Rudder.RIGHT) {
                        boat.getRudder().rightControlInput();
                    }
                }
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                final Boat boat = parent.getStateReference();
                if (boat != null) {
                    boat.getRudder().noControlInput();
                }
            }

            return true;
        }
    }
}
