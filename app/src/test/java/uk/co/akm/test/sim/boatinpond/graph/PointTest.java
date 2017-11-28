package uk.co.akm.test.sim.boatinpond.graph;

import junit.framework.Assert;

import org.junit.Test;

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
    public void shouldSetPixel() {
        final Point underTest = new Point(0.5, 0.5);

        underTest.setPixel(2, 2, 400, 400);
        Assert.assertEquals(300, underTest.pixel.x);
        Assert.assertEquals(100, underTest.pixel.y);
    }
}
