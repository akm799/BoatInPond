package uk.co.akm.test.sim.boatinpond.boat;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.boat.impl.MotorImpl;

/**
 * Created by Thanos Mavroidis on 27/02/2018.
 */
public class MotorTest {
    private final double maxForce = 100;
    private final double timeToMaxPower = 4;

    private final int nSteps = 100;
    private final double dt = timeToMaxPower/nSteps;

    private final double fraction = maxForce/timeToMaxPower;
    private final double df = fraction*dt;

    private Motor underTest;

    @Before
    public void setUp() {
        underTest = new MotorImpl(maxForce, timeToMaxPower);
    }

    @Test
    public void shouldSetMaxPower() {
        Assert.assertEquals(maxForce, underTest.getMaxForce());
    }

    @Test
    public void shouldBeOffInitially() {
        Assert.assertFalse(underTest.isOn());
        Assert.assertEquals(0.0, underTest.getForce());
    }

    @Test
    public void shouldTurnOn() {
        underTest.turnOn();
        Assert.assertTrue(underTest.isOn());
        Assert.assertEquals(0.0, underTest.getForce());
    }

    @Test
    public void shouldTurnOff() {
        underTest.turnOn();

        underTest.turnOff();
        Assert.assertFalse(underTest.isOn());
        Assert.assertEquals(0.0, underTest.getForce());
    }

    @Test
    public void shouldPowerUp() {
        underTest.turnOn();

        underTest.increaseControlInput();
        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);
            Assert.assertEquals(i*df, underTest.getForce());
        }

        Assert.assertEquals(maxForce, underTest.getForce());
    }

    @Test
    public void shouldPowerDown() {
        underTest.turnOn();
        powerUp();

        underTest.decreaseControlInput();
        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);
            Assert.assertEquals(maxForce - i*df, underTest.getForce());
        }

        Assert.assertEquals(0.0, underTest.getForce());
    }

    @Test
    public void shouldNotPowerUpWhenMotorIsOff() {
        Assert.assertFalse(underTest.isOn());

        underTest.increaseControlInput();
        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);
            Assert.assertEquals(0.0, underTest.getForce());
        }

        Assert.assertEquals(0.0, underTest.getForce());
    }

    @Test
    public void shouldCutPower() {
        underTest.turnOn();
        powerUp();

        underTest.turnOff();
        Assert.assertFalse(underTest.isOn());
        Assert.assertEquals(0.0, underTest.getForce());
    }

    @Test
    public void shouldNotIncreasePowerAfterTurningOn() {
        underTest.turnOn();
        powerUp();

        // Turn on and then update repeatedly. This should not change the zero power level.
        underTest.turnOff();
        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);
            Assert.assertEquals(0.0, underTest.getForce());
        }

        Assert.assertEquals(0.0, underTest.getForce());
    }

    @Test
    public void shouldNotExceedMaxPower() {
        underTest.turnOn();

        underTest.increaseControlInput();
        for (int i=1 ; i<=2*nSteps ; i++) {
            underTest.update(dt);

            if (i <= nSteps) {
                Assert.assertEquals(i*df, underTest.getForce());
            } else {
                Assert.assertEquals(maxForce, underTest.getForce());
            }
        }

        Assert.assertEquals(maxForce, underTest.getForce());
    }

    @Test
    public void shouldNotReachNegativePower() {
        underTest.turnOn();
        powerUp();

        underTest.decreaseControlInput();
        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);

            if (i <= nSteps) {
                Assert.assertEquals(maxForce - i*df, underTest.getForce());
            } else {
                Assert.assertEquals(0.0, underTest.getForce());
            }
        }

        Assert.assertEquals(0.0, underTest.getForce());
    }

    private void powerUp() {
        underTest.increaseControlInput();
        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);
            Assert.assertEquals(i*df, underTest.getForce());
        }

        Assert.assertEquals(maxForce, underTest.getForce());
    }
}
