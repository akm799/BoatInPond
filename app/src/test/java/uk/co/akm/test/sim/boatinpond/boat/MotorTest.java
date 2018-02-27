package uk.co.akm.test.sim.boatinpond.boat;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.boat.impl.MotorImpl;

/**
 * Created by Thanos Mavroidis on 27/02/2018.
 */
public class MotorTest {
    private final double maxPower = 100;
    private final double timeToMaxPower = 4;

    private final int nSteps = 100;
    private final double dt = timeToMaxPower/nSteps;

    private final double fraction = maxPower/timeToMaxPower;
    private final double dp = fraction*dt;

    private Motor underTest;

    @Before
    public void setUp() {
        underTest = new MotorImpl(maxPower, timeToMaxPower);
    }

    @Test
    public void shouldSetMaxPower() {
        Assert.assertEquals(maxPower, underTest.getMaxPower());
    }

    @Test
    public void shouldBeOffInitially() {
        Assert.assertFalse(underTest.isOn());
        Assert.assertEquals(0.0, underTest.getPower());
    }

    @Test
    public void shouldTurnOn() {
        underTest.turnOn();
        Assert.assertTrue(underTest.isOn());
        Assert.assertEquals(0.0, underTest.getPower());
    }

    @Test
    public void shouldTurnOff() {
        underTest.turnOn();

        underTest.turnOff();
        Assert.assertFalse(underTest.isOn());
        Assert.assertEquals(0.0, underTest.getPower());
    }

    @Test
    public void shouldPowerUp() {
        underTest.turnOn();

        underTest.increaseControlInput();
        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);
            Assert.assertEquals(i*dp, underTest.getPower());
        }

        Assert.assertEquals(maxPower, underTest.getPower());
    }

    @Test
    public void shouldPowerDown() {
        underTest.turnOn();
        powerUp();

        underTest.decreaseControlInput();
        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);
            Assert.assertEquals(maxPower - i*dp, underTest.getPower());
        }

        Assert.assertEquals(0.0, underTest.getPower());
    }

    @Test
    public void shouldNotPowerUpWhenMotorIsOff() {
        Assert.assertFalse(underTest.isOn());

        underTest.increaseControlInput();
        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);
            Assert.assertEquals(0.0, underTest.getPower());
        }

        Assert.assertEquals(0.0, underTest.getPower());
    }

    @Test
    public void shouldCutPower() {
        underTest.turnOn();
        powerUp();

        underTest.turnOff();
        Assert.assertFalse(underTest.isOn());
        Assert.assertEquals(0.0, underTest.getPower());
    }

    @Test
    public void shouldNotIncreasePowerAfterTurningOn() {
        underTest.turnOn();
        powerUp();

        // Turn on and then update repeatedly. This should not change the zero power level.
        underTest.turnOff();
        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);
            Assert.assertEquals(0.0, underTest.getPower());
        }

        Assert.assertEquals(0.0, underTest.getPower());
    }

    @Test
    public void shouldNotExceedMaxPower() {
        underTest.turnOn();

        underTest.increaseControlInput();
        for (int i=1 ; i<=2*nSteps ; i++) {
            underTest.update(dt);

            if (i <= nSteps) {
                Assert.assertEquals(i*dp, underTest.getPower());
            } else {
                Assert.assertEquals(maxPower, underTest.getPower());
            }
        }

        Assert.assertEquals(maxPower, underTest.getPower());
    }

    @Test
    public void shouldNotReachNegativePower() {
        underTest.turnOn();
        powerUp();

        underTest.decreaseControlInput();
        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);

            if (i <= nSteps) {
                Assert.assertEquals(maxPower - i*dp, underTest.getPower());
            } else {
                Assert.assertEquals(0.0, underTest.getPower());
            }
        }

        Assert.assertEquals(0.0, underTest.getPower());
    }

    private void powerUp() {
        underTest.increaseControlInput();
        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);
            Assert.assertEquals(i*dp, underTest.getPower());
        }

        Assert.assertEquals(maxPower, underTest.getPower());
    }
}
