package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import uk.co.akm.test.sim.boatinpond.R;
import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.impl.linear.BoatConstantsApprox;
import uk.co.akm.test.sim.boatinpond.boat.impl.linear.LinearBoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.BoatConstantsImpl;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.BoatPerformance;

/**
 * Created by Thanos Mavroidis on 29/11/2017.
 */
public final class BoatActivity extends AbstractBoatActivity {

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
        return linearResistanceInstance();
    }

    private Boat linearResistanceInstance() {
        final double v0 = 2.5; // 9 km/h
        final double frVFinal = 0.000001;
        final double tv = 120; // 2 mins
        final double kLatOverKLon = 50;

        final double omgMax = 0.39269908169873; // (90 deg in 4 sec)
        final double frOmgFinal = 0.999999;
        final double tOmg = 5;

        final LinearBoatConstants constants = new BoatConstantsApprox(v0, frVFinal, tv, kLatOverKLon, omgMax, frOmgFinal, tOmg);
        return new uk.co.akm.test.sim.boatinpond.boat.impl.linear.BoatImpl(constants, Math.PI/2, 3);
    }

    private Boat quadraticResistanceInstance() {
        final double kLatOverKLon = 50;
        final double kLonReverseOverKLon = 10;
        final double boatLength = 4;
        final double cogDistanceFromStern = 1.5;

        final double launchSpeed = 3.01; // 6 knots
        final double distanceLimit = 75;
        final double turningSpeed = 9.26; // 18 Knots
        final double turnRate = 2.5*Math.PI/8; // 56.25 degrees per second
        final BoatPerformance performance = new BoatPerformance(launchSpeed, distanceLimit, turnRate, turningSpeed);

        final BoatConstants constants = new BoatConstantsImpl(performance, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern);
        return new uk.co.akm.test.sim.boatinpond.boat.impl.quad.BoatImpl(constants, Math.PI/2, 3);
    }
}
