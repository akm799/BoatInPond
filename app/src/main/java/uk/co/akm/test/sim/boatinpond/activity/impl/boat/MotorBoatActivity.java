package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.co.akm.test.sim.boatinpond.R;
import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.Motor;
import uk.co.akm.test.sim.boatinpond.boat.MotorBoat;
import uk.co.akm.test.sim.boatinpond.boat.MotorBoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.factory.MotorBoatConstantsFactory;
import uk.co.akm.test.sim.boatinpond.boat.factory.impl.quad.MotorBoatConstantsFactoryImpl;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatConstantsImpl;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatImpl;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatPerformance;

/**
 * Created by Thanos Mavroidis on 29/11/2017.
 */
public final class MotorBoatActivity extends AbstractBoatActivity {
    private static final String RUDDER_SIZE_INDICATOR_KEY = "rudder.size.indicator";
    private static final String MAX_MOTOR_POWER_INDICATOR_KEY = "max.motor.power.indicator";

    public static void start(Context context, int rudderSizeIndicator, int maxPowerIndicator) {
        final Intent intent = new Intent(context, MotorBoatActivity.class);
        intent.putExtra(RUDDER_SIZE_INDICATOR_KEY, rudderSizeIndicator);
        intent.putExtra(MAX_MOTOR_POWER_INDICATOR_KEY, maxPowerIndicator);

        context.startActivity(intent);
    }

    private Button motorSwitchBtn;
    private TextView motorPowerTxt;
    private final View[] motorPowerBtns= new View[2];

    Motor getMotor() {
        final MotorBoat boat = (MotorBoat)getStateReference();

        return (boat == null ? null : boat.getMotor());
    }

    void setPowerButtonsEnabled(boolean enabled) {
        for (View btn: motorPowerBtns) {
            if (btn != null) {
                btn.setEnabled(enabled);
            }
        }
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
        motorPowerBtns[0] = findViewById(R.id.mb_motor_decrease);
        motorPowerBtns[1] = findViewById(R.id.mb_motor_increase);
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
        final int rudderSizeIndicator = getIntent().getIntExtra(RUDDER_SIZE_INDICATOR_KEY, 50);
        final int maxPowerIndicator = getIntent().getIntExtra(MAX_MOTOR_POWER_INDICATOR_KEY, 38);
        final MotorBoatConstantsFactory constantsFactory = new MotorBoatConstantsFactoryImpl();
        final MotorBoatConstants constants = constantsFactory.instance(rudderSizeIndicator, maxPowerIndicator);

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
                    parent.setPowerButtonsEnabled(false);
                    parent.motorSwitchBtn.setText("ON");
                } else {
                    motor.turnOn();
                    parent.setPowerButtonsEnabled(true);
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
