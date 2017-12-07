package uk.co.akm.test.sim.boatinpond.graph;

import junit.framework.Assert;

import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.math.TestTrigAnglesFactory;
import uk.co.akm.test.sim.boatinpond.math.TrigAngle;
import uk.co.akm.test.sim.boatinpond.math.TrigValues;

/**
 * Created by Thanos Mavroidis on 19/11/2017.
 */
public class PointTest {
    private final double accuracy = 0.000000000000001;

    @Test
    public void shouldCreateNullPoint() {
        final Point underTest = new Point();
        Assert.assertTrue(underTest.isNull());
        Assert.assertFalse(underTest.isNotNull());
    }

    @Test
    public void shouldSetPoint() {
        final Point underTest = new Point();

        underTest.set(1, 2);
        Assert.assertFalse(underTest.isNull());
        Assert.assertTrue(underTest.isNotNull());
        Assert.assertEquals(1.0, underTest.x);
        Assert.assertEquals(2.0, underTest.y);
    }

    @Test
    public void shouldSetNullPoint() {
        final Point underTest = new Point();

        underTest.set(0, 0);
        Assert.assertFalse(underTest.isNull());
        Assert.assertTrue(underTest.isNotNull());

        underTest.setNull();
        Assert.assertTrue(underTest.isNull());
        Assert.assertFalse(underTest.isNotNull());
    }

    @Test
    public void shouldTranslatePoint() {
        final Point underTest = new Point();
        underTest.set(1, 2);

        underTest.translate(2, 1);
        Assert.assertEquals(3.0, underTest.x);
        Assert.assertEquals(3.0, underTest.y);

        underTest.translate(-4, -1);
        Assert.assertEquals(-1.0, underTest.x);
        Assert.assertEquals( 2.0, underTest.y);
    }

    @Test
    public void shouldRotatePoint() {
        final double c = 1/Math.sqrt(2);

        final Point underTest = new Point();
        underTest.set(1, 0);

        underTest.rotate(Math.PI/4);
        Assert.assertEquals(c, underTest.x, accuracy);
        Assert.assertEquals(c, underTest.y, accuracy);

        underTest.rotate(Math.PI/4);
        Assert.assertEquals(0, underTest.x, accuracy);
        Assert.assertEquals(1, underTest.y, accuracy);

        underTest.rotate(-3*Math.PI/4);
        Assert.assertEquals( c, underTest.x, accuracy);
        Assert.assertEquals(-c, underTest.y, accuracy);
    }

    @Test
    public void shouldFastRotatePoint() {
        final double c = 1/Math.sqrt(2);

        final Point underTest = new Point();
        underTest.set(1, 0);

        final TrigAngle deg45 = new TrigAngle();
        deg45.set(Math.PI/4);

        final TrigAngle degMinus135 = new TrigAngle();
        degMinus135.set(-3*Math.PI/4);

        underTest.fastRotate(deg45);
        Assert.assertEquals(c, underTest.x, accuracy);
        Assert.assertEquals(c, underTest.y, accuracy);

        underTest.fastRotate(deg45);
        Assert.assertEquals(0, underTest.x, accuracy);
        Assert.assertEquals(1, underTest.y, accuracy);

        underTest.fastRotate(degMinus135);
        Assert.assertEquals( c, underTest.x, accuracy);
        Assert.assertEquals(-c, underTest.y, accuracy);
    }

    @Test
    public void shouldInverseFastRotatePoint() {
        final double c = 1/Math.sqrt(2);

        final Point underTest = new Point();
        underTest.set(c, c);

        final TrigAngle deg45 = new TrigAngle();
        deg45.set(Math.PI/4);

        underTest.fastRotate(deg45);
        Assert.assertEquals(0, underTest.x, accuracy);
        Assert.assertEquals(1, underTest.y, accuracy);

        underTest.fastRotate(deg45.negative());
        Assert.assertEquals(c, underTest.x, accuracy);
        Assert.assertEquals(c, underTest.y, accuracy);
    }

    @Test
    public void shouldFastRotatePointAfterNegation() {
        final double c = 1/Math.sqrt(2);

        final Point underTest = new Point();
        underTest.set(1, 0);

        final TrigAngle deg45 = new TrigAngle();
        deg45.set(-Math.PI/4);
        deg45.negative();

        final TrigAngle degMinus135 = new TrigAngle();
        degMinus135.set(3*Math.PI/4);
        degMinus135.negative();

        underTest.fastRotate(deg45);
        Assert.assertEquals(c, underTest.x, accuracy);
        Assert.assertEquals(c, underTest.y, accuracy);

        underTest.fastRotate(deg45);
        Assert.assertEquals(0, underTest.x, accuracy);
        Assert.assertEquals(1, underTest.y, accuracy);

        underTest.fastRotate(degMinus135);
        Assert.assertEquals( c, underTest.x, accuracy);
        Assert.assertEquals(-c, underTest.y, accuracy);
    }

    @Test
    public void shouldFastRotatePointAfterAddition() {
        final double c = 1/Math.sqrt(2);

        final Point underTest = new Point();
        underTest.set(1, 0);

        final TrigAngle deg45Const = new TrigAngle();
        deg45Const.set(Math.PI/4);

        final TrigAngle deg45 = new TrigAngle();
        deg45.set(0);
        deg45.add(deg45Const);

        underTest.fastRotate(deg45);
        Assert.assertEquals(c, underTest.x, accuracy);
        Assert.assertEquals(c, underTest.y, accuracy);

        underTest.fastRotate(deg45);
        Assert.assertEquals(0, underTest.x, accuracy);
        Assert.assertEquals(1, underTest.y, accuracy);
    }

    @Test
    public void shouldFastRotatePointAfterSubtraction() {
        final double c = 1/Math.sqrt(2);

        final Point underTest = new Point();
        underTest.set(1, 0);

        final TrigValues degMinus90 = TestTrigAnglesFactory.instance(0, -1);

        final TrigAngle deg45 = new TrigAngle();
        deg45.set(3*Math.PI/4);
        deg45.add(degMinus90);

        underTest.fastRotate(deg45);
        Assert.assertEquals(c, underTest.x, accuracy);
        Assert.assertEquals(c, underTest.y, accuracy);

        underTest.fastRotate(deg45);
        Assert.assertEquals(0, underTest.x, accuracy);
        Assert.assertEquals(1, underTest.y, accuracy);
    }

    @Test
    public void shouldSetPixel() {
        final Point underTest = new Point(0.5, 0.5);

        underTest.setPixel(2, 2, 400, 400);
        Assert.assertEquals(300, underTest.pixel.x);
        Assert.assertEquals(100, underTest.pixel.y);
    }
}
