package uk.co.akm.test.sim.boatinpond.boat.impl.linear;

import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.impl.AbstractBoatTest;


/**
 * Created by Thanos Mavroidis on 11/11/2017.
 */
public class BoatTest extends AbstractBoatTest {
    private final double v0ForLinearTest = 2.5; // 9 km/h
    private final double frVFinal = 0.000001;
    private final double tv = 60; // 1 min
    private final double kLatOverKLon = 50;

    private final double omgMax = 0.39269908169873; // (90 deg in 4 sec)
    private final double frOmgFinal = 0.999999;
    private final double tOmg = 5;


    @Override
    protected void setUpParameters() {
        v0ForSlowDownTest = v0ForLinearTest;
        distanceLimitForSlowDownTest = evaluateDistanceLimit();
        maxAnglesDiff = 0.02;

        constants = new BoatConstantsApprox(v0ForLinearTest, frVFinal, tv, kLatOverKLon, omgMax, frOmgFinal, tOmg);
    }

    private double evaluateDistanceLimit() {
        final double k = -Math.log(frVFinal)/tv;

        return v0ForLinearTest/k;
    }

    @Override
    protected Boat boatInstance(BoatConstants boatConstants, double hdn0, double v0) {
        return new BoatImpl((LinearBoatConstants)boatConstants, hdn0, v0);
    }
}