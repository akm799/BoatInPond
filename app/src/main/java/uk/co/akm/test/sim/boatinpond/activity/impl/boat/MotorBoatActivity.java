package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.co.akm.test.sim.boatinpond.R;
import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.Motor;
import uk.co.akm.test.sim.boatinpond.boat.MotorBoat;
import uk.co.akm.test.sim.boatinpond.boat.MotorBoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatConstantsImpl;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatImpl;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatPerformance;

/**
 * Created by Thanos Mavroidis on 29/11/2017.
 */
public final class MotorBoatActivity extends AbstractBoatActivity {
    private Button motorSwitch;
    private TextView motorPowerTxt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_motor_boat;
    }

    @Override
    protected int getScreenViewId() {
        return R.id.mb_boat_screen_view;
    }

    @Override
    protected int getSpeedTextDisplayResId() {
        return R.id.mb_speed_txt;
    }

    @Override
    protected int getHeadingTextDisplayResId() {
        return R.id.mb_heading_txt;
    }

    @Override
    protected int getPositionTextDisplayResId() {
        return R.id.mb_location_txt;
    }

    @Override
    protected int getCommandButtonResId() {
        return R.id.mb_command_btn;
    }

    @Override
    protected int getLeftRudderControlResId() {
        return R.id.mb_rudder_left_btn;
    }

    @Override
    protected int getRightRudderControlResId() {
        return R.id.mb_rudder_right_btn;
    }

    @Override
    protected Boat boatInstance() {
        return motorBoatInstance();
    }

    @Override
    protected void initAdditionalViews() {
        motorSwitch = findViewById(R.id.mb_motor_switch);
        motorPowerTxt = findViewById(R.id.mb_motor_power_txt);
    }

    @Override
    protected void setAdditionalListeners() {
        findViewById(R.id.mb_motor_switch).setOnClickListener(new MotorSwitchListener(this));
        findViewById(R.id.mb_motor_increase).setOnTouchListener(new MotorListener(this, Motor.INCREASE));
        findViewById(R.id.mb_motor_decrease).setOnTouchListener(new MotorListener(this, Motor.DECREASE));
    }

    @Override
    protected void updateAdditionalTextDisplays(BoatViewBox renderingData) {
        final MotorBoat boat = (MotorBoat)getStateReference();
        if (boat != null) {
            final Motor motor = boat.getMotor();
            if (motor != null) {
                final double power = motor.getForce()/motor.getMaxForce();
                final long powerPercentage = 100 * Math.round(power);
                motorPowerTxt.setText(Long.toString(powerPercentage) + "%");
            }
        }
    }

    private Boat motorBoatInstance() {
        final double kLatOverKLon = 50;
        final double kLonReverseOverKLon = 10;
        final double boatLength = 4;
        final double cogDistanceFromStern = 1.5;

        final double launchSpeed = 3.01; // 6 knots
        final double distanceLimit = 75;
        final double turningSpeed = 9.26; // 18 Knots
        final double turnRate = 2.5*Math.PI/8; // 56.25 degrees per second

        final double maxSpeed = 4.12; // 8 knots
        final MotorBoatPerformance performance = new MotorBoatPerformance(launchSpeed, distanceLimit, turnRate, turningSpeed, maxSpeed);

        final MotorBoatConstants constants = new MotorBoatConstantsImpl(performance, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern);
        return new MotorBoatImpl(constants, Math.PI/2, 0);
    }

    private static final class MotorSwitchListener implements View.OnClickListener {
        private final MotorBoatActivity parent;

        MotorSwitchListener(MotorBoatActivity parent) {
            this.parent = parent;
        }

        @Override
        public void onClick(View view) {
            final MotorBoat boat = (MotorBoat)parent.getStateReference();
            if (boat != null) {
                final Motor motor = boat.getMotor();
                final Button motorSwitch = parent.motorSwitch;
                if (motor != null && motorSwitch != null) {
                    if (motor.isOn()) {
                        motor.turnOff();
                        motorSwitch.setText("ON");
                    } else {
                        motor.turnOn();
                        motorSwitch.setText("OFF");
                    }
                }
            }
        }
    }

    private static final class MotorListener implements View.OnTouchListener {
        private final int action;
        private final AbstractBoatActivity parent;

        MotorListener(AbstractBoatActivity parent, int action) {
            this.parent = parent;
            this.action = action;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                final MotorBoat boat = (MotorBoat)parent.getStateReference();
                if (boat != null) {
                    if (action == Motor.DECREASE) {
                        boat.getMotor().decreaseControlInput();
                    } else if (action == Motor.INCREASE) {
                        boat.getMotor().increaseControlInput();
                    }
                }
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                final MotorBoat boat = (MotorBoat)parent.getStateReference();
                if (boat != null) {
                    boat.getMotor().noControlInput();
                }
            }

            return true;
        }
    }
}
