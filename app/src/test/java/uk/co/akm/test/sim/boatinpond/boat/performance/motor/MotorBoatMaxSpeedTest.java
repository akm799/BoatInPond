package uk.co.akm.test.sim.boatinpond.boat.performance.motor;

import junit.framework.Assert;

import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.boat.Motor;
import uk.co.akm.test.sim.boatinpond.boat.MotorBoat;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatConstantsImpl2;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatImpl2;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatPerformance;
import uk.co.akm.test.sim.boatinpond.boat.performance.helper.BoatPerformanceTestHelper;

public class MotorBoatMaxSpeedTest {
    private final double kLatOverKLon = 50;
    private final double kLonReverseOverKLon = 10;
    private final double boatLength = 4;
    private final double cogDistanceFromStern = 1.5;
    private final double rudderAreaFraction = 0.075;

    private final double launchSpeed = 3.01; // 6 knots
    private final double distanceLimit = 75;
    private final double turningSpeed = 20.56; // 40 Knots
    private final double turnRadius = 20.66668818928022; // Equivalent to 57 degrees per second turning rate.

    private final double maxRudderAngle = Math.PI/4;
    private final double timeToMaxRudderDeflection = 2;
    private final double boatToRudderLengthRatio = 20;

    private final double maxSpeed = 9.26; // 18 Knots

    private final double timeToMaxPower = 4;

    private final MotorBoatPerformance performance = new MotorBoatPerformance(launchSpeed, distanceLimit, turningSpeed, turnRadius, timeToMaxRudderDeflection, maxSpeed, timeToMaxPower);
    private final MotorBoatConstantsImpl2 constants = new MotorBoatConstantsImpl2(performance, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern, rudderAreaFraction, maxRudderAngle, boatToRudderLengthRatio);

    private final double dt = 0.0001;

    @Test
    public void shouldReachMaxSpeed() {
        final MotorBoat underTest = new MotorBoatImpl2(constants, Math.PI/2, 0);
        Assert.assertEquals(0, underTest.v(), 0);

        final double twoMins = 2*60;
        applyMaximumPower(underTest.getMotor());
        BoatPerformanceTestHelper.update(underTest, dt, twoMins);

        final double v = underTest.v();
        Assert.assertTrue(v > 0);
        Assert.assertTrue(v < maxSpeed);
        Assert.assertTrue(maxSpeed - v < 1E-10);
    }

    private void applyMaximumPower(Motor motor) {
        motor.turnOn();
        motor.increaseControlInput();
        increaseToMaximumPower(motor);
        motor.noControlInput();
    }

    private void increaseToMaximumPower(Motor motor) {
        double increase = Double.MAX_VALUE;
        while (increase > 0) {
            final double beforeIncrease = motor.getForce();
            motor.update(dt);
            increase = motor.getForce() - beforeIncrease;
        }
    }
}
