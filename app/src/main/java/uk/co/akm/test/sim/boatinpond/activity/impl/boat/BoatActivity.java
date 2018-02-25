package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.co.akm.test.sim.boatinpond.R;
import uk.co.akm.test.sim.boatinpond.activity.ViewBoxStateActivity;
import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;
import uk.co.akm.test.sim.boatinpond.boat.impl.linear.BoatConstantsApprox;
import uk.co.akm.test.sim.boatinpond.boat.impl.linear.BoatImpl;
import uk.co.akm.test.sim.boatinpond.boat.impl.linear.LinearBoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.BoatConstantsImpl;

/**
 * Created by Thanos Mavroidis on 29/11/2017.
 */
public final class BoatActivity extends ViewBoxStateActivity<Boat, BoatViewBox> {
    private Button commandBtn;

    private TextView speed;
    private TextView heading;
    private TextView location;

    private int nTextSkip;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        speed = findViewById(R.id.speed_txt);
        heading = findViewById(R.id.heading_txt);
        location = findViewById(R.id.location_txt);
        commandBtn = findViewById(R.id.command_btn);

        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.rudder_left_btn).setOnTouchListener(new RudderListener(this, Rudder.LEFT));
        findViewById(R.id.rudder_right_btn).setOnTouchListener(new RudderListener(this, Rudder.RIGHT));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_boat;
    }

    @Override
    protected int getScreenViewId() {
        return R.id.boat_screen_view;
    }

    @Override
    protected final BoatViewBox buildViewBox(int viewWidth, int viewHeight) {
        return new BoatViewBox(30, 5, viewWidth, viewHeight);
    }

    @Override
    protected void drawAdditionalData(BoatViewBox renderingData) {
        if (nTextSkip == 10) {
            nTextSkip = 0;
            speed.setText(renderingData.getSpeed());
            heading.setText(renderingData.getCompassHeading());
            location.setText(renderingData.getCoordinates());
        } else {
            nTextSkip++;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (isRunning()) {
            terminate();
        }
    }

    public void onCommand(View view) {
        if (isRunning()) {
            terminate();
            commandBtn.setText("Start");
        } else {
            startMotion();
            commandBtn.setText("Stop");
        }
    }

    private void startMotion() {
        startMotionLinear();
    }

    private void startMotionLinear() {
        final double v0 = 2.5; // 9 km/h
        final double frVFinal = 0.000001;
        final double tv = 120; // 2 mins
        final double kLatOverKLon = 50;

        final double omgMax = 0.39269908169873; // (90 deg in 4 sec)
        final double frOmgFinal = 0.999999;
        final double tOmg = 5;

        final LinearBoatConstants constants = new BoatConstantsApprox(v0, frVFinal, tv, kLatOverKLon, omgMax, frOmgFinal, tOmg);
        initState(new BoatImpl(constants, Math.PI/2, 3));

        initiate();
    }

    private void startMotionQuadratic() {
        final double kLon = 0.025;
        final double kLatOverKLon = 50;
        final double kLonReverseOverKLon = 10;
        final double boatLength = 4;
        final double cogDistanceFromStern = 1.5;
        final double turningSpeed = 9.26; // 18 Knots
        final double turnRateAtTwentyKnots = 2.5*Math.PI/8; // 56.25 degrees per second

        final BoatConstants constants = new BoatConstantsImpl(kLon, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern, turnRateAtTwentyKnots, turningSpeed);
        initState(new uk.co.akm.test.sim.boatinpond.boat.impl.quad.BoatImpl(constants, Math.PI/2, 0, 3, 0, 0));

        initiate();
    }

    private static final class RudderListener implements View.OnTouchListener {
        private final int direction;
        private final BoatActivity parent;

        RudderListener(BoatActivity parent, int direction) {
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
