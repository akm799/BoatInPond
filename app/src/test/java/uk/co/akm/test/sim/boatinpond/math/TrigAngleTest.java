package uk.co.akm.test.sim.boatinpond.math;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Thanos Mavroidis on 03/12/2017.
 */
public class TrigAngleTest {
    private final double accuracy = 0.0000000000000003;

    private TrigAngle underTest;

    @Before
    public void setUp() {
        underTest = new TrigAngle();
    }

    @Test
    public void shouldBuildUndefinedTrigAngle() {
        Assert.assertTrue(Double.isNaN(underTest.cos()));
        Assert.assertTrue(Double.isNaN(underTest.sin()));
    }

    @Test
    public void shouldBuildTrigAngle() {
        final double oneOverRootTwo = 1/Math.sqrt(2);
        final TrigValues underTest = TestTrigAnglesFactory.instance(oneOverRootTwo, oneOverRootTwo);
        Assert.assertEquals(oneOverRootTwo, underTest.cos());
        Assert.assertEquals(oneOverRootTwo, underTest.sin());
    }

    @Test
    public void shouldNotBuildTrigAngleWithInvalidArgument() {
        try {
            TestTrigAnglesFactory.instance(0, 1 + accuracy);
            Assert.fail();
        } catch (IllegalArgumentException iae) {
            Assert.assertTrue(iae.getMessage() != null);
        }
    }

    @Test
    public void shouldNotBuildInvalidTrigAngle() {
        try {
            TestTrigAnglesFactory.instance(0.8, 0.5);
            Assert.fail();
        } catch (IllegalArgumentException iae) {
            Assert.assertTrue(iae.getMessage() != null);
        }
    }

    @Test
    public void shouldBuildNegativeTrigAngle() {
        final TrigValues underTest = TestTrigAnglesFactory.instance(0, -1);
        Assert.assertEquals(0.0, underTest.cos());
        Assert.assertEquals(-1.0, underTest.sin());
    }

    @Test
    public void shouldSetTrigAngle() {
        underTest.set(Math.PI/4);

        final double oneOverRootTwo = 1/Math.sqrt(2);
        Assert.assertEquals(oneOverRootTwo, underTest.cos(), accuracy);
        Assert.assertEquals(oneOverRootTwo, underTest.sin(), accuracy);
    }

    @Test
    public void shouldCopyTrigAngle() {
        final TrigAngle source = new TrigAngle();
        source.set(Math.PI/4);

        final TrigAngle underTest = this.underTest.copy(source);
        Assert.assertEquals(this.underTest, underTest);

        final double oneOverRootTwo = 1/Math.sqrt(2);
        Assert.assertEquals(oneOverRootTwo, underTest.cos(), accuracy);
        Assert.assertEquals(oneOverRootTwo, underTest.sin(), accuracy);
    }

    @Test
    public void shouldNegateTrigAngle() {
        this.underTest.set(Math.PI/4);
        final TrigAngle underTest = this.underTest.negative();
        Assert.assertEquals(this.underTest, underTest);

        final double oneOverRootTwo = 1/Math.sqrt(2);
        Assert.assertEquals(oneOverRootTwo, underTest.cos(), accuracy);
        Assert.assertEquals(-oneOverRootTwo, underTest.sin(), accuracy);
    }

    @Test
    public void shouldNegateNegativeTrigAngle() {
        this.underTest.set(-Math.PI/4);
        final TrigAngle underTest = this.underTest.negative();
        Assert.assertEquals(this.underTest, underTest);

        final double oneOverRootTwo = 1/Math.sqrt(2);
        Assert.assertEquals(oneOverRootTwo, underTest.cos(), accuracy);
        Assert.assertEquals(oneOverRootTwo, underTest.sin(), accuracy);
    }

    @Test
    public void shouldAddTigAngles() {
        final TrigAngle other = new TrigAngle();
        other.set(Math.PI/4);

        this.underTest.set(Math.PI/4);
        final TrigAngle underTest = this.underTest.add(other);
        Assert.assertEquals(this.underTest, underTest);

        Assert.assertEquals(0, underTest.cos(), accuracy);
        Assert.assertEquals(1, underTest.sin(), accuracy);
    }

    @Test
    public void shouldSubtractTigAngles() {
        final TrigAngle other = new TrigAngle();
        other.set(-Math.PI/4);

        this.underTest.set(Math.PI/4);
        final TrigAngle underTest = this.underTest.add(other);
        Assert.assertEquals(this.underTest, underTest);

        Assert.assertEquals(1, underTest.cos(), accuracy);
        Assert.assertEquals(0, underTest.sin(), accuracy);
    }

    @Test
    public void shouldSubtractRightTigAngle() {
        final TrigValues minusPiOverTwo = TestTrigAnglesFactory.instance(0, -1);

        this.underTest.set(Math.PI/4);
        final TrigAngle underTest = this.underTest.add(minusPiOverTwo);
        Assert.assertEquals(this.underTest, underTest);

        final double oneOverRootTwo = 1/Math.sqrt(2);
        Assert.assertEquals(oneOverRootTwo, underTest.cos(), accuracy);
        Assert.assertEquals(-oneOverRootTwo, underTest.sin(), accuracy);
    }
}
