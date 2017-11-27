package uk.co.akm.test.sim.boatinpond.activity.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.co.akm.test.sim.boatinpond.R;
import uk.co.akm.test.sim.boatinpond.activity.ViewBoxStateActivity;

/**
 * Created by Thanos Mavroidis on 26/11/2017.
 */
public final class TestActivity extends ViewBoxStateActivity<TestBody, TestViewBox> {
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
        return R.layout.activity_test;
    }

    @Override
    protected int getScreenViewId() {
        return R.id.test_screen_view;
    }

    @Override
    protected final TestViewBox buildViewBox(int viewWidth, int viewHeight) {
        return new TestViewBox(30, 5, viewWidth, viewHeight);
    }

    @Override
    protected void drawAdditionalData(TestViewBox renderingData) {
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
        initState(new TestBody(10, 0, 0, 3, Math.PI/2));
        initiate();
    }
}
