package uk.co.akm.test.sim.boatinpond.graph;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Thanos Mavroidis on 19/11/2017.
 */
public class LineTest {
    private final double accuracy = 0.000000000000001;

    @Test
    public void shouldCreateNullLine() {
        final Line underTest = new Line();
        Assert.assertTrue(underTest.isNull());
        Assert.assertFalse(underTest.isNotNull());

        Assert.assertNotNull(underTest.start);
        Assert.assertNotNull(underTest.end);
    }

    @Test
    public void shouldSetLine() {
        final Line underTest = new Line();

        underTest.set(1, 2, 3, 4);
        Assert.assertFalse(underTest.isNull());
        Assert.assertTrue(underTest.isNotNull());
        assertLineExact(1,2, 3, 4, underTest);
    }

    @Test
    public void shouldSetNullLine() {
        final Line underTest = new Line();

        underTest.set(1, 2, 3, 4);
        Assert.assertFalse(underTest.isNull());
        Assert.assertTrue(underTest.isNotNull());

        underTest.setNull();
        Assert.assertTrue(underTest.isNull());
        Assert.assertFalse(underTest.isNotNull());
    }

    @Test
    public void shouldTranslateLine() {
        final Line underTest = new Line();
        underTest.set(1, 2, 3, 4);

        underTest.translate(1, 2);
        assertLineExact(2,4, 4, 6, underTest);

        underTest.translate(-2, 1);
        assertLineExact(0,5, 2, 7, underTest);
    }

    @Test
    public void shouldRotateLine() {
        final double d = Math.sqrt(2);

        final Line underTest = new Line();
        underTest.set(-1, -1, 1, 1);

        underTest.rotate(Math.PI/4);
        assertLine(0, -d, 0, d, underTest);

        underTest.rotate(Math.PI/4);
        assertLine(1, -1, -1, 1, underTest);

        underTest.rotate(-3*Math.PI/4);
        assertLine(-d, 0, d, 0, underTest);
    }

    @Test
    public void shouldRotateVerticalLineByMinus90Degrees() {
        final Line underTest = new Line();
        underTest.set(0, -1, 0, 1);

        underTest.rotate(-Math.PI/2);
        assertLine(-1, 0, 1, 0, underTest);
    }

    @Test
    public void shouldFindVerticalIntercept() {
        final Line underTest = new Line();
        underTest.set(1, 1, 3, 3);

        final Point intercept = new Point();

        underTest.resetParameters();
        underTest.verticalIntercept(2, intercept);
        Assert.assertFalse(intercept.isNull());
        Assert.assertTrue(intercept.isNotNull());
        Assert.assertEquals(2, intercept.x, accuracy);
        Assert.assertEquals(2, intercept.y, accuracy);
    }

    @Test
    public void shouldFindVerticalInterceptWhenLineIsHorizontal() {
        final Line underTest = new Line();
        underTest.set(1, 1, 3, 1);

        final Point intercept = new Point();

        underTest.resetParameters();
        underTest.verticalIntercept(2, intercept);
        Assert.assertFalse(intercept.isNull());
        Assert.assertTrue(intercept.isNotNull());
        Assert.assertEquals(2, intercept.x, accuracy);
        Assert.assertEquals(1, intercept.y, accuracy);
    }

    @Test
    public void shouldNotFindVerticalIntercept() {
        final Line underTest = new Line();
        underTest.set(1, 1, 3, 3);

        final Point intercept = new Point();

        underTest.resetParameters();
        underTest.verticalIntercept(4, intercept);
        Assert.assertTrue(intercept.isNull());
        Assert.assertFalse(intercept.isNotNull());
    }

    @Test
    public void shouldNotFindVerticalInterceptWhenLineIsVertical() {
        final Line underTest = new Line();
        underTest.set(1, 1, 1, 3);

        final Point intercept = new Point();

        underTest.resetParameters();

        underTest.verticalIntercept(4, intercept);
        Assert.assertTrue(intercept.isNull());
        Assert.assertFalse(intercept.isNotNull());

        underTest.verticalIntercept(1, intercept); // Overlapping lines should be classed as a vertical intercept.
        Assert.assertTrue(intercept.isNull());
        Assert.assertFalse(intercept.isNotNull());
    }

    @Test
    public void shouldFindHorizontalIntercept() {
        final Line underTest = new Line();
        underTest.set(1, 1, 3, 3);

        final Point intercept = new Point();

        underTest.resetParameters();
        underTest.horizontalIntercept(2, intercept);
        Assert.assertFalse(intercept.isNull());
        Assert.assertTrue(intercept.isNotNull());
        Assert.assertEquals(2, intercept.x, accuracy);
        Assert.assertEquals(2, intercept.y, accuracy);
    }

    @Test
    public void shouldFindHorizontalInterceptWhenLineIsVertical() {
        final Line underTest = new Line();
        underTest.set(1, 1, 1, 3);

        final Point intercept = new Point();

        underTest.resetParameters();
        underTest.horizontalIntercept(2, intercept);
        Assert.assertFalse(intercept.isNull());
        Assert.assertTrue(intercept.isNotNull());
        Assert.assertEquals(1, intercept.x, accuracy);
        Assert.assertEquals(2, intercept.y, accuracy);
    }

    @Test
    public void shouldNotFindHorizontalIntercept() {
        final Line underTest = new Line();
        underTest.set(1, 1, 3, 3);

        final Point intercept = new Point();

        underTest.resetParameters();
        underTest.horizontalIntercept(0.5, intercept);
        Assert.assertTrue(intercept.isNull());
        Assert.assertFalse(intercept.isNotNull());
    }

    @Test
    public void shouldNotFindHorizontalInterceptWhenLineIsHorizontal() {
        final Line underTest = new Line();
        underTest.set(1, 1, 3, 1);

        final Point intercept = new Point();

        underTest.resetParameters();

        underTest.horizontalIntercept(4, intercept);
        Assert.assertTrue(intercept.isNull());
        Assert.assertFalse(intercept.isNotNull());

        underTest.horizontalIntercept(1, intercept); // Overlapping lines should be classed as a horizontal intercept.
        Assert.assertTrue(intercept.isNull());
        Assert.assertFalse(intercept.isNotNull());
    }

    @Test
    public void shouldResetPreviousIntercept() {
        final Line underTest = new Line();
        underTest.set(1, 1, 3, 3);

        final Point intercept = new Point();
        intercept.set(2, 2);
        Assert.assertFalse(intercept.isNull());
        Assert.assertTrue(intercept.isNotNull());

        underTest.resetParameters();
        underTest.horizontalIntercept(0.5, intercept);
        Assert.assertTrue(intercept.isNull());
        Assert.assertFalse(intercept.isNotNull());
    }

    @Test
    public void shouldFindIntercepts() {
        final Line underTest = new Line();
        underTest.set(1, 2, 3, 4);

        underTest.resetParameters();

        final Point horizontalIntercept = new Point();
        underTest.horizontalIntercept(3, horizontalIntercept);
        Assert.assertFalse(horizontalIntercept.isNull());
        Assert.assertTrue(horizontalIntercept.isNotNull());
        Assert.assertEquals(2, horizontalIntercept.x, accuracy);
        Assert.assertEquals(3, horizontalIntercept.y, accuracy);

        final Point verticalIntercept = new Point();
        underTest.verticalIntercept(2, verticalIntercept);
        Assert.assertFalse(verticalIntercept.isNull());
        Assert.assertTrue(verticalIntercept.isNotNull());
        Assert.assertEquals(2, verticalIntercept.x, accuracy);
        Assert.assertEquals(3, verticalIntercept.y, accuracy);
    }

    @Test
    public void shouldSetPixels() {
        final Line underTest = new Line();
        underTest.set(0.5, 0.5, 1.0, 1.0);

        underTest.setPixels(2, 2, 400, 400);
        Assert.assertEquals(300, underTest.startPixel.x);
        Assert.assertEquals(100, underTest.startPixel.y);
        Assert.assertEquals(400, underTest.endPixel.x);
        Assert.assertEquals(0, underTest.endPixel.y);
    }

    private void assertLineExact(double xStart, double yStart, double xEnd, double yEnd, Line underTest) {
        Assert.assertEquals(xStart, underTest.start.x);
        Assert.assertEquals(yStart, underTest.start.y);
        Assert.assertEquals(xEnd, underTest.end.x);
        Assert.assertEquals(yEnd, underTest.end.y);
    }

    private void assertLine(double xStart, double yStart, double xEnd, double yEnd, Line underTest) {
        Assert.assertEquals(xStart, underTest.start.x, accuracy);
        Assert.assertEquals(yStart, underTest.start.y, accuracy);
        Assert.assertEquals(xEnd, underTest.end.x, accuracy);
        Assert.assertEquals(yEnd, underTest.end.y, accuracy);
    }
}
