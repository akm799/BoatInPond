package uk.co.akm.test.sim.boatinpond;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import uk.co.akm.test.sim.boatinpond.activity.impl.boat.BoatActivity;
import uk.co.akm.test.sim.boatinpond.activity.impl.boat.MotorBoatActivity;
import uk.co.akm.test.sim.boatinpond.activity.impl.test.TestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onGraphicsTest(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }

    public void onBoatLaunch(View view) {
        final int rudderSizeIndicator = getSeekBarValue(R.id.boat_rudder_indicator);
        final int launchSpeedIndicator = getSeekBarValue(R.id.boat_launch_speed);
        BoatActivity.start(this, rudderSizeIndicator, launchSpeedIndicator);
    }

    public void onMotorBoatInit(View view) {
        final int rudderSizeIndicator = getSeekBarValue(R.id.motor_boat_rudder_indicator);
        final int maxPowerIndicator = getSeekBarValue(R.id.motor_boat_max_power_indicator);
        MotorBoatActivity.start(this, rudderSizeIndicator, maxPowerIndicator);
    }

    private int getSeekBarValue(int resId) {
        return ((SeekBar)findViewById(resId)).getProgress();
    }
}