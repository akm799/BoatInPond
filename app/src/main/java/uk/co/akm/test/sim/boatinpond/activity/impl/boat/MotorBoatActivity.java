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
    private Button motorSwitchBtn;
    private TextView motorPowerTxt;

    Motor getMotor() {
        final MotorBoat boat = (MotorBoat)getStateReference();

        return (boat == null ? null : boat.getMotor());
    }

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
        motorSwitchBtn = findViewById(R.id.mb_motor_switch);
        motorPowerTxt = findViewById(R.id.mb_motor_power_txt);
    }

    @Override
    protected void setAdditionalListeners() {
        findViewById(R.id.mb_motor_switch).setOnClickListener(new MotorSwitchListener(this));
        findViewById(R.id.mb_motor_decrease).setOnTouchListener(new MotorListener(this, Motor.DECREASE));
        findViewById(R.id.mb_motor_increase).setOnTouchListener(new MotorListener(this, Motor.INCREASE));
    }

    @Override
    protected void updateAdditionalTextDisplays(BoatViewBox renderingData) {
        final Motor motor = getMotor();
        if (motor != null) {
            motorPowerTxt.setText(buildMotorPowerString(motor));
        }
    }

    private String buildMotorPowerString(Motor motor) {
        final long powerPercentage = getMotorPowerPercentage(motor);

        return (Long.toString(powerPercentage) + "%");
    }

    private long getMotorPowerPercentage(Motor motor) {
        final double power = motor.getForce()/motor.getMaxForce();

        return Math.round(100*power);
    }

    private Boat motorBoatInstance() {
        final double kLatOverKLon = 50;
        final double kLonReverseOverKLon = 10;
        final double boatLength = 4;
        final double cogDistanceFromStern = 1.5;
        final double rudderAreaFraction = 0.05;

        final double launchSpeed = 3.01; // 6 knots
        final double distanceLimit = 75;
        final double turningSpeed = 9.26; // 18 Knots
        final double turnRate = 2.5*Math.PI/8; // 56.25 degrees per second

        final double maxSpeed = 4.12; // 8 knots
        final MotorBoatPerformance performance = new MotorBoatPerformance(launchSpeed, distanceLimit, turnRate, turningSpeed, maxSpeed);

        final MotorBoatConstants constants = new MotorBoatConstantsImpl(performance, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern, rudderAreaFraction);
        return new MotorBoatImpl(constants, Math.PI/2, 0);
    }

    private static final class MotorSwitchListener implements View.OnClickListener {
        private final MotorBoatActivity parent;

        MotorSwitchListener(MotorBoatActivity parent) {
            this.parent = parent;
        }

        @Override
        public void onClick(View view) {
            final Motor motor = parent.getMotor();
            if (motor != null && parent.motorSwitchBtn != null) {
                if (motor.isOn()) {
                    motor.turnOff();
                    parent.motorSwitchBtn.setText("ON");
                } else {
                    motor.turnOn();
                    parent.motorSwitchBtn.setText("OFF");
                }
            }
        }
    }

    private static final class MotorListener implements View.OnTouchListener {
        private final int motorAction;
        private final MotorBoatActivity parent;

        MotorListener(MotorBoatActivity parent, int motorAction) {
            this.parent = parent;
            this.motorAction = motorAction;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            final int btnAction = motionEvent.getAction();

            switch (btnAction) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_DOWN:
                    final Motor motor = parent.getMotor();
                    if (motor != null) {
                        processButtonAction(btnAction, motor);
                    }
                    break;

                default: break;
            }

            return true;
        }

        private void processButtonAction(int btnAction, Motor motor) {
            switch (btnAction) {
                case MotionEvent.ACTION_UP:
                    motor.noControlInput();
                    break;

                case MotionEvent.ACTION_DOWN:
                    changeMotorPower(motor);
                    break;

                default: break;
            }
        }

        private void changeMotorPower(Motor motor) {
            if (motorAction == Motor.DECREASE) {
                motor.decreaseControlInput();
            } else if (motorAction == Motor.INCREASE) {
                motor.increaseControlInput();
            }
        }
    }
}
