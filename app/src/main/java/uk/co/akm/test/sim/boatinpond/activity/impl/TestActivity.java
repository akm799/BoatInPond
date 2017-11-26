package uk.co.akm.test.sim.boatinpond.activity.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import uk.co.akm.test.sim.boatinpond.R;
import uk.co.akm.test.sim.boatinpond.activity.ViewBoxStateActivity;
import uk.co.akm.test.sim.boatinpond.graph.ViewBox;

/**
 * Created by Thanos Mavroidis on 26/11/2017.
 */
public final class TestActivity extends ViewBoxStateActivity<TestBody, ViewBox> {
    private Button commandBtn;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    protected final ViewBox buildViewBox(int viewWidth, int viewHeight) {
        return new ViewBox(30, 5, viewWidth, viewHeight);
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
        initState(new TestBody(10, 0, 0, 3, Math.PI/4));
        initiate();
    }
}
