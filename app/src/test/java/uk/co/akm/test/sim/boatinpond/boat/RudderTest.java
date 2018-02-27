package uk.co.akm.test.sim.boatinpond.boat;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.boat.impl.PowerRudder;

/**
 * Created by Thanos Mavroidis on 02/12/2017.
 */
public class RudderTest {
    private final double maxAngle = Math.PI/4;
    private final double timeToMaxAngle = 2;

    private final int nSteps = 20;
    private final double dt = timeToMaxAngle/nSteps;

    private final double fraction = maxAngle/timeToMaxAngle;
    private final double da = fraction*dt;

    private Rudder underTest;

    @Before
    public void setUp() {
        underTest = new PowerRudder(maxAngle, timeToMaxAngle);
    }

    @Test
    public void shouldSetMaxAngle() {
        Assert.assertEquals(maxAngle, underTest.getMaxRudderAngle());
    }

    @Test
    public void shouldHaveZeroInitialdeflection() {
        Assert.assertEquals(0.0, underTest.getRudderAngle());
    }

    @Test
    public void shouldApplyLeftRudder() {
        underTest.leftControlInput();

        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);
            Assert.assertEquals(i*da, underTest.getRudderAngle());
        }
    }

    @Test
    public void shouldApplyRightRudder() {
        underTest.rightControlInput();

        for (int i=1 ; i<=nSteps ; i++) {
            underTest.update(dt);
            Assert.assertEquals(-i*da, underTest.getRudderAngle());
        }
    }

    @Test
    public void shouldNotExceedMaxLeftDeflection() {
        underTest.leftControlInput();

        for (int i=1 ; i<=2*nSteps ; i++) {
            underTest.update(dt);
            final double a = underTest.getRudderAngle();
            Assert.assertTrue(0 <= a && a <= maxAngle);
        }
        Assert.assertEquals(maxAngle, underTest.getRudderAngle());
    }

    @Test
    public void shouldNotExceedMaxRightDeflection() {
        underTest.rightControlInput();

        for (int i=1 ; i<=2*nSteps ; i++) {
            underTest.update(dt);
            final double a = underTest.getRudderAngle();
            Assert.assertTrue(-maxAngle <= a && a <= 0);
        }
        Assert.assertEquals(-maxAngle, underTest.getRudderAngle());
    }

    @Test
    public void shouldApplyFullLeftAndRightRudder() {
        underTest.leftControlInput();
        for (int i=1 ; i<=2*nSteps ; i++) {
            underTest.update(dt);
        }
        Assert.assertEquals(maxAngle, underTest.getRudderAngle());

        underTest.rightControlInput();
        for (int i=1 ; i<=4*nSteps ; i++) {
            underTest.update(dt);
        }
        Assert.assertEquals(-maxAngle, underTest.getRudderAngle());
    }
}
