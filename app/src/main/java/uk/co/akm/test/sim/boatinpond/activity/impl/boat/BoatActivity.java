package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import android.content.Context;
import android.content.Intent;

import uk.co.akm.test.sim.boatinpond.R;
import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.factory.BoatConstantsFactory;
import uk.co.akm.test.sim.boatinpond.boat.factory.impl.quad.BoatConstantsFactoryImpl;
import uk.co.akm.test.sim.boatinpond.boat.impl.linear.BoatConstantsApprox;
import uk.co.akm.test.sim.boatinpond.boat.impl.linear.LinearBoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.BoatConstantsImpl;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.BoatPerformance;

/**
 * Created by Thanos Mavroidis on 29/11/2017.
 */
public final class BoatActivity extends AbstractBoatActivity {
    private static final String RUDDER_SIZE_INDICATOR_KEY = "rudder.size.indicator";
    private static final String LAUNCH_SPEED_INDICATOR_KEY = "launch.speed.indicator";

    public static void start(Context context, int rudderSizeIndicator, int launchSpeedIndicator) {
        final Intent intent = new Intent(context, BoatActivity.class);
        intent.putExtra(RUDDER_SIZE_INDICATOR_KEY, rudderSizeIndicator);
        intent.putExtra(LAUNCH_SPEED_INDICATOR_KEY, launchSpeedIndicator);

        context.startActivity(intent);
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
    protected int getSpeedTextDisplayResId() {
        return R.id.speed_txt;
    }

    @Override
    protected int getHeadingTextDisplayResId() {
        return R.id.heading_txt;
    }

    @Override
    protected int getPositionTextDisplayResId() {
        return R.id.location_txt;
    }

    @Override
    protected int getLeftRudderControlResId() {
        return R.id.rudder_left_btn;
    }

    @Override
    protected int getRightRudderControlResId() {
        return R.id.rudder_right_btn;
    }

    @Override
    protected Boat boatInstance() {
        final int rudderSizeIndicator = getIntent().getIntExtra(RUDDER_SIZE_INDICATOR_KEY, 50);
        final BoatConstantsFactory factory = new BoatConstantsFactoryImpl();
        final BoatConstants constants = factory.instance(rudderSizeIndicator);

        final int launchSpeedIndicator = getIntent().getIntExtra(LAUNCH_SPEED_INDICATOR_KEY, 50);
        final double v0 = determineLaunchSpeed(launchSpeedIndicator);

        return new uk.co.akm.test.sim.boatinpond.boat.impl.quad.BoatImpl(constants, Math.PI/2, v0);
    }

    private Boat linearResistanceInstance() {
        final double v0 = 2.5; // 9 km/h
        final double frVFinal = 0.000001;
        final double tv = 120; // 2 mins
        final double kLatOverKLon = 50;

        final double omgMax = 0.39269908169873; // (90 deg in 4 sec)
        final double frOmgFinal = 0.999999;
        final double tOmg = 5;

        final int launchSpeedIndicator = getIntent().getIntExtra(LAUNCH_SPEED_INDICATOR_KEY, 50);
        final double launchSpeed = determineLaunchSpeed(launchSpeedIndicator);

        final LinearBoatConstants constants = new BoatConstantsApprox(v0, frVFinal, tv, kLatOverKLon, omgMax, frOmgFinal, tOmg);
        return new uk.co.akm.test.sim.boatinpond.boat.impl.linear.BoatImpl(constants, Math.PI/2, launchSpeed);
    }

    private double determineLaunchSpeed(int launchSpeedIndicator) {
        final double minSpeed = 1.03; //  2 Knots
        final double maxSpeed = 9.26; // 18 Knots
        final double speedRange = maxSpeed - minSpeed;
        final double fraction = launchSpeedIndicator/100.0;

        return minSpeed + fraction*speedRange;
    }
}
