package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.co.akm.test.sim.boatinpond.R;
import uk.co.akm.test.sim.boatinpond.activity.ViewBoxStateActivity;
import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;

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
        final double v0 = 2.5; // 9 km/h
        final double frVFinal = 0.000001;
        final double tv = 120; // 2 mins
        final double kLatOverKLon = 50;

        final double omgMax = 0.39269908169873; // (90 deg in 4 sec)
        final double frOmgFinal = 0.999999;
        final double tOmg = 5;

        final BoatConstants constants = new BoatConstants(v0, frVFinal, tv, kLatOverKLon, omgMax, frOmgFinal, tOmg);

        initState(new Boat(constants, Math.PI/2, 3, Math.PI/32));
        initiate();
    }
}
