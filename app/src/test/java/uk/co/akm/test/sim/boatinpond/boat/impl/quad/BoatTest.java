package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.impl.AbstractBoatTest;

/**
 * Created by Thanos Mavroidis on 26/02/2018.
 */
public final class BoatTest extends AbstractBoatTest {
    private final double kLatOverKLon = 50;
    private final double kLonReverseOverKLon = 10;
    private final double boatLength = 4;
    private final double cogDistanceFromStern = 1.5;
    private final double rudderAreaFraction = 0.05;

    private final double launchSpeed = 3.01; // 6 knots
    private final double distanceLimit = 75;
    private final double turningSpeed = 9.26; // 18 Knots
    private final double turnRate = 2.5*Math.PI/8; // 56.25 degrees per second

    private final double maxRudderAngle = Math.PI/4;
    private final double timeToMaxRudderDeflection = 2;

    private final BoatPerformance performance = new BoatPerformance(launchSpeed, distanceLimit, turnRate, turningSpeed, timeToMaxRudderDeflection);

    @Override
    protected void setUpParameters() {
        v0ForSlowDownTest = launchSpeed;
        distanceLimitForSlowDownTest = distanceLimit;
        maxAnglesDiff = 0.2;

        constants = new BoatConstantsImpl(performance, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern, rudderAreaFraction, maxRudderAngle);
    }

    @Override
    protected Boat boatInstance(BoatConstants boatConstants, double hdn0, double v0) {
        return new BoatImpl(boatConstants, hdn0, v0);
    }
}
