package uk.co.akm.test.sim.boatinpond.graph;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Thanos Mavroidis on 19/11/2017.
 */
public class PixelTest {

    @Test
    public void shouldSetPixel() {
        testPixel(0, 0, 2, 400, 200, 200);
        testPixel(1, 1, 2, 400, 400, 0);
        testPixel(1, -1, 2, 400, 400, 400);
        testPixel(-1, -1, 2, 400, 0, 400);
        testPixel(-1, 1, 2, 400, 0, 0);
        testPixel(0.5, 0.5, 2, 400, 300, 100);
        testPixel(0.5, -0.5, 2, 400, 300, 300);
        testPixel(-0.5, -0.5, 2, 400, 100, 300);
        testPixel(-0.5, 0.5, 2, 400, 100, 100);
    }

    private void testPixel(double xPoint, double yPoint, double boxSide, int screenSide, int xPixel, int yPixel) {
        final Point point = new Point();
        point.set(xPoint, yPoint);

        final Pixel underTest = new Pixel();
        underTest.fromPoint(point, boxSide, boxSide, screenSide, screenSide);
        Assert.assertEquals(xPixel, underTest.x);
        Assert.assertEquals(yPixel, underTest.y);
    }
}
