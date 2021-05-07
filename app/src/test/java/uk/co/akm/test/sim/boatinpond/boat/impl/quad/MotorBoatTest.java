package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

import junit.framework.Assert;

import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.boat.Motor;
import uk.co.akm.test.sim.boatinpond.boat.MotorBoat;
import uk.co.akm.test.sim.boatinpond.boat.MotorBoatConstants;
import uk.co.akm.test.sim.boatinpond.phys.Updater;

/**
 * Created by Thanos Mavroidis on 01/03/2018.
 */
public class MotorBoatTest {
    private final double kLatOverKLon = 50;
    private final double kLonReverseOverKLon = 10;
    private final double boatLength = 4;
    private final double cogDistanceFromStern = 1.5;
    private final double rudderAreaFraction = 0.05;

    private final double launchSpeed = 3.01; // 6 knots
    private final double distanceLimit = 75;
    private final double turningSpeed = 9.26; // 18 Knots
    private final double turnRadius = 20.66668818928022; // Equivalent to 57 degrees per second turning rate.

    private final double maxRudderAngle = Math.PI/4;
    private final double timeToMaxRudderDeflection = 2;
    private final double boatToRudderLengthRatio = 20;

    private final double maxSpeed = 4.12; // 8 knots
    private final double timeToMaxPower = 4;

    private final MotorBoatPerformance performance = new MotorBoatPerformance(launchSpeed, distanceLimit, turningSpeed, turnRadius, timeToMaxRudderDeflection, maxSpeed, timeToMaxPower);
    private final MotorBoatConstants constants = new MotorBoatConstantsImpl2(performance, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern, rudderAreaFraction, maxRudderAngle, boatToRudderLengthRatio);

    @Test
    public void shouldApproachMaxSpeed() {
        final int nSteps = 1000000;
        final double maxSpeedProximity = 1.0E-11;
        final double travelingTime = 600; // 10 mins

        final MotorBoat underTest = new MotorBoatImpl2(constants, 0, 0);
        Assert.assertEquals(0.0, underTest.v());
        Assert.assertEquals(0.0, underTest.vx());
        Assert.assertEquals(0.0, underTest.vy());

        powerUp(underTest.getMotor());
        Updater.update(underTest, travelingTime, nSteps);

        Assert.assertTrue(underTest.v() < maxSpeed);
        Assert.assertTrue(underTest.vx() < maxSpeed);
        Assert.assertTrue(maxSpeed - underTest.v() < maxSpeedProximity);
        Assert.assertEquals(underTest.v(), underTest.vx());
        Assert.assertEquals(0.0, underTest.vy());
    }

    private void powerUp(Motor motor) {
        final int nSteps = 100;
        final double timeToMaxPower = 4;
        final double dt = timeToMaxPower/nSteps;

        final double fraction = motor.getMaxForce()/timeToMaxPower;
        final double df = fraction*dt;

        motor.turnOn();
        motor.increaseControlInput();
        for (int i=1 ; i<=nSteps ; i++) {
            motor.update(dt);
            Assert.assertEquals(i*df, motor.getForce(), 1.0E-14);
        }

        Assert.assertEquals(motor.getMaxForce(), motor.getForce());
    }
}
