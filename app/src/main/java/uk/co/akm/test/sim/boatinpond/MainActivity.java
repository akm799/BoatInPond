package uk.co.akm.test.sim.boatinpond;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
        startActivity(new Intent(this, BoatActivity.class));
    }

    public void onMotorBoatInit(View view) {
        startActivity(new Intent(this, MotorBoatActivity.class));
    }
}
