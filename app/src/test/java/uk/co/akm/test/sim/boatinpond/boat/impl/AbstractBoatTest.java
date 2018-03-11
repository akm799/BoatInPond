package uk.co.akm.test.sim.boatinpond.boat.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;
import uk.co.akm.test.sim.boatinpond.math.Angles;
import uk.co.akm.test.sim.boatinpond.phys.State;
import uk.co.akm.test.sim.boatinpond.phys.UpdatableState;
import uk.co.akm.test.sim.boatinpond.phys.Updater;

/**
 * Created by Thanos Mavroidis on 26/02/2018.
 */
public abstract class AbstractBoatTest {
    private final int nSteps = 1000000;
    private final double accuracy = 0.00001;

    private final double v0 = 2.5; // 9 km/h
    private final double turningTime = 5;

    enum RudderState {NEUTRAL_POSITION, FULL_LEFT_RUDDER, FULL_RIGHT_RUDDER}

    protected double v0ForSlowDownTest = 0;
    protected double distanceLimitForSlowDownTest = 0;
    protected double maxAnglesDiff = 0;
    protected BoatConstants constants = null;

    @Before
    public void setUp() {
        setUpParameters();
        checkSetParameters();
    }

    protected abstract void setUpParameters();

    private void checkSetParameters() {
        if (v0ForSlowDownTest <= 0) {
            throw new IllegalStateException("'v0ForSlowDownTest' test parameter not set properly.");
        }

        if (distanceLimitForSlowDownTest <= 0) {
            throw new IllegalStateException("'distanceLimitForSlowDownTest' test parameter not set properly.");
        }

        if (maxAnglesDiff <= 0) {
            throw new IllegalStateException("'maxAnglesDiff' test parameter not set properly.");
        }

        if (constants == null) {
            throw new IllegalStateException("'constants' test parameter not set properly.");
        }
    }

    @Test
    public void shouldSetInitialConditions() {
        final UpdatableState underTest = boatInstance(Math.PI/2, v0, RudderState.NEUTRAL_POSITION);
        Assert.assertEquals(Math.PI/2, underTest.hdn(), accuracy);
        Assert.assertEquals(Math.PI/2, underTest.hdnP(), accuracy);
        Assert.assertNotNull(underTest.hdnTrig());
        Assert.assertEquals(0, underTest.hdnTrig().cos(), accuracy);
        Assert.assertEquals(1, underTest.hdnTrig().sin(), accuracy);

        Assert.assertEquals(v0, underTest.v(), accuracy);
        Assert.assertEquals(0, underTest.vx(), accuracy);
        Assert.assertEquals(v0, underTest.vy(), accuracy);
    }

    @Test
    public void shouldComeToRest() {
        final double v0 = v0ForSlowDownTest;
        final double slowingDownTime = 600; // 10 mins

        final UpdatableState underTest = boatInstance(0, v0, RudderState.NEUTRAL_POSITION);
        Assert.assertEquals(v0, underTest.v(), accuracy);
        Assert.assertEquals(v0, underTest.vx(), accuracy);

        final double distanceLimit = distanceLimitForSlowDownTest;
        final double limitApproach = 0.003; // How close we can get to the limit within the (long) time during which we are slowing down.

        Updater.update(underTest, slowingDownTime, nSteps);
        Assert.assertEquals(0.0, underTest.v(), accuracy);
        Assert.assertEquals(0.0, underTest.vx(), accuracy);
        Assert.assertTrue(underTest.x() > 0);
        Assert.assertTrue(underTest.x() < distanceLimit);
        Assert.assertTrue(distanceLimit - underTest.x() < limitApproach);
        Assert.assertEquals(0.0, underTest.y(), accuracy);
    }

    @Test
    public void shouldTurnLeftFromZeroDeg() {
        final double hdn0 = 0;
        final UpdatableState underTest = boatInstance(hdn0, v0, RudderState.FULL_LEFT_RUDDER);
        Assert.assertEquals(v0, underTest.v(), accuracy);
        Assert.assertEquals(v0, underTest.vx(), accuracy);
        Assert.assertEquals(0, underTest.vy(), accuracy);
        Assert.assertEquals(hdn0, underTest.hdn(), accuracy);

        Updater.update(underTest, turningTime, nSteps);
        printFinalState("turnLeftFromZeroDeg", hdn0, underTest);
        Assert.assertTrue(underTest.hdn() > hdn0);
        Assert.assertTrue(underTest.vx() > 0);
        Assert.assertTrue(underTest.vy() > 0);
        Assert.assertTrue(underTest.v() < v0);
        if (underTest.v() > 0) {
            Assert.assertEquals(underTest.hdn(), velocityAngle(underTest), maxAnglesDiff);
        }

        Assert.assertTrue(underTest.x() > 0);
        Assert.assertTrue(underTest.y() > 0);
    }

    @Test
    public void shouldTurnRightFromZeroDeg() {
        final double hdn0 = 0;
        final UpdatableState underTest = boatInstance(hdn0, v0, RudderState.FULL_RIGHT_RUDDER);
        Assert.assertEquals(v0, underTest.v(), accuracy);
        Assert.assertEquals(v0, underTest.vx(), accuracy);
        Assert.assertEquals(0, underTest.vy(), accuracy);
        Assert.assertEquals(hdn0, underTest.hdn(), accuracy);

        Updater.update(underTest, turningTime, nSteps);
        printFinalState("turnRightFromZeroDeg", hdn0, underTest);
        Assert.assertTrue(underTest.hdn() < hdn0);
        Assert.assertTrue(underTest.vx() > 0);
        Assert.assertTrue(underTest.vy() < 0);
        Assert.assertTrue(underTest.v() < v0);
        if (underTest.v() > 0) {
            Assert.assertEquals(underTest.hdn(), velocityAngle(underTest), maxAnglesDiff);
        }

        Assert.assertTrue(underTest.x() > 0);
        Assert.assertTrue(underTest.y() < 0);
    }

    @Test
    public void shouldTurnLeftFrom90Deg() {
        final double hdn0 = Math.PI/2;
        final UpdatableState underTest = boatInstance(hdn0, v0, RudderState.FULL_LEFT_RUDDER);
        Assert.assertEquals(v0, underTest.v(), accuracy);
        Assert.assertEquals(v0, underTest.vy(), accuracy);
        Assert.assertEquals(0, underTest.vx(), accuracy);
        Assert.assertEquals(hdn0, underTest.hdn(), accuracy);

        Updater.update(underTest, turningTime, nSteps);
        printFinalState("turnLeftFrom90Deg", hdn0, underTest);
        Assert.assertTrue(underTest.hdn() > hdn0);
        Assert.assertTrue(underTest.vx() < 0);
        Assert.assertTrue(underTest.vy() > 0);
        Assert.assertTrue(underTest.v() < v0);
        if (underTest.v() > 0) {
            Assert.assertEquals(underTest.hdn(), velocityAngle(underTest), maxAnglesDiff);
        }

        Assert.assertTrue(underTest.x() < 0);
        Assert.assertTrue(underTest.y() > 0);
    }

    @Test
    public void shouldTurnRightFrom90Deg() {
        final double hdn0 = Math.PI/2;
        final UpdatableState underTest = boatInstance(hdn0, v0, RudderState.FULL_RIGHT_RUDDER);
        Assert.assertEquals(v0, underTest.v(), accuracy);
        Assert.assertEquals(v0, underTest.vy(), accuracy);
        Assert.assertEquals(0, underTest.vx(), accuracy);
        Assert.assertEquals(hdn0, underTest.hdn(), accuracy);

        Updater.update(underTest, turningTime, nSteps);
        printFinalState("turnRightFrom90Deg", hdn0, underTest);
        Assert.assertTrue(underTest.hdn() < hdn0);
        Assert.assertTrue(underTest.vx() > 0);
        Assert.assertTrue(underTest.vy() > 0);
        Assert.assertTrue(underTest.v() < v0);
        if (underTest.v() > 0) {
            Assert.assertEquals(underTest.hdn(), velocityAngle(underTest), maxAnglesDiff);
        }

        Assert.assertTrue(underTest.x() > 0);
        Assert.assertTrue(underTest.y() > 0);
    }

    @Test
    public void shouldTurnRightFromZeroDegAndThenBackLeft() {
        final double hdn0 = 0;
        final UpdatableState underTest1 = boatInstance(hdn0, v0, RudderState.FULL_RIGHT_RUDDER); // First turn right ...
        Assert.assertEquals(v0, underTest1.v(), accuracy);
        Assert.assertEquals(v0, underTest1.vx(), accuracy);
        Assert.assertEquals(0, underTest1.vy(), accuracy);
        Assert.assertEquals(hdn0, underTest1.hdn(), accuracy);

        Updater.update(underTest1, turningTime, nSteps);
        Assert.assertTrue(underTest1.hdn() < hdn0);
        Assert.assertTrue(underTest1.vx() > 0);
        Assert.assertTrue(underTest1.vy() < 0);
        Assert.assertTrue(underTest1.v() < v0);
        if (underTest1.v() > 0) {
            Assert.assertEquals(underTest1.hdn(), velocityAngle(underTest1), maxAnglesDiff);
        }

        final UpdatableState underTest2 = boatInstance(underTest1.hdn(), v0, RudderState.FULL_LEFT_RUDDER); // ... and then turn back left from the last heading with the same speed ...
        Updater.update(underTest2, turningTime, nSteps);
        Assert.assertEquals(hdn0, underTest2.hdn(), accuracy); // ... and end up back at the heading where we started.
        Assert.assertTrue(underTest2.v() < v0);
        if (underTest2.v() > 0) {
            Assert.assertEquals(underTest2.hdn(), velocityAngle(underTest2), maxAnglesDiff);
        }
    }

    @Test
    public void shouldTurnRightFromZeroDegAndThenLeft() {
        final double hdn0 = 0;
        final UpdatableState underTest1 = boatInstance(hdn0, v0, RudderState.FULL_RIGHT_RUDDER); // First turn right ...
        Assert.assertEquals(v0, underTest1.v(), accuracy);
        Assert.assertEquals(v0, underTest1.vx(), accuracy);
        Assert.assertEquals(0, underTest1.vy(), accuracy);
        Assert.assertEquals(hdn0, underTest1.hdn(), accuracy);

        Updater.update(underTest1, turningTime, nSteps);
        Assert.assertTrue(underTest1.hdn() < hdn0);
        Assert.assertTrue(underTest1.vx() > 0);
        Assert.assertTrue(underTest1.vy() < 0);
        Assert.assertTrue(underTest1.v() < v0);
        if (underTest1.v() > 0) {
            Assert.assertEquals(underTest1.hdn(), velocityAngle(underTest1), maxAnglesDiff);
        }

        final UpdatableState underTest2 = boatInstance(underTest1.hdn(), underTest1.v(), RudderState.FULL_LEFT_RUDDER); // ... and then turn back left from the last heading at the current (reduced) speed ...
        Updater.update(underTest2, turningTime, nSteps);
        Assert.assertTrue(underTest2.hdn() > underTest1.hdn()); // ... and end up a bit to the left from our last heading ...
        Assert.assertTrue(underTest2.hdn() < hdn0); // ... but not all the way back from where we started.
        Assert.assertTrue(underTest2.v() < underTest1.v());
        if (underTest2.v() > 0) {
            Assert.assertEquals(underTest2.hdn(), velocityAngle(underTest2), maxAnglesDiff);
        }
    }

    @Test
    public void shouldLooseMoreEnergyWhenTurning() {
        final double time = 3;
        final double hdn0 = 0;
        final UpdatableState underTestStraight = boatInstance(hdn0, v0, RudderState.NEUTRAL_POSITION);
        final UpdatableState underTestTurning = boatInstance(hdn0, v0, RudderState.FULL_LEFT_RUDDER);

        Updater.update(underTestStraight, time, nSteps);
        Updater.update(underTestTurning, time, nSteps);

        Assert.assertTrue(underTestStraight.v() < v0);
        Assert.assertTrue(underTestTurning.v() < v0);
        Assert.assertTrue(underTestTurning.v() < underTestStraight.v()); // Turning boat has less energy at the end than the straight one.
    }

    private UpdatableState boatInstance(double hdn0, double v0, RudderState rudderState) {
        final Boat boat = boatInstance(constants, hdn0, v0);
        if (rudderState != null) {
            setFullRudder(rudderState, boat.getRudder());
        }

        return boat;
    }

    protected abstract Boat boatInstance(BoatConstants boatConstants, double hdn0, double v0);

    private void setFullRudder(RudderState rudderState, Rudder rudder) {
        switch (rudderState) {
            case NEUTRAL_POSITION:
                return;

            case FULL_LEFT_RUDDER:
                rudder.leftControlInput();
                break;

            case FULL_RIGHT_RUDDER:
                rudder.rightControlInput();
                break;

            default: throw new IllegalArgumentException("Unrecognized rudder state: " + rudderState);
        }

        double a = 0;
        while (a < rudder.getMaxRudderAngle()) {
            rudder.update(0.1);
            a = Math.abs(rudder.getRudderAngle());
        }
    }

    private double velocityAngle(State state) {
        return properAngle(state.vx(), state.vy());
    }

    private double properAngle(double x, double y) {
        if (y == 0) {
            final double r = Math.sqrt(x*x + y*y);
            final double a = Math.acos(Math.abs(x)/r);

            return (x >=0 ? a : -a);
        } else {
            return Math.atan2(y, x);
        }
    }

    private void printFinalState(String name, double hdn0, State state) {
        final double turnDeg = Math.abs(Angles.toDeg(state.hdn()) - Angles.toDeg(hdn0));
        System.out.println(name + ": hdn=" + Angles.toDeg(state.hdn()) + " turn=" + turnDeg +" vx=" + state.vx() + " vy=" + state.vy());
    }
}
