package uk.co.akm.test.sim.boatinpond.graph;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Thanos Mavroidis on 19/11/2017.
 */
public class ViewBoxTest {
    private final double accuracy = 0.000000000000001;

    @Test
    public void shouldBuildLines() {
        final ViewBoxLines underTest = new ViewBox(2, 5, 400, 400);

        final int nLines = underTest.buildLines(0, 0, 0);
        Assert.assertEquals(2, nLines);
        assertNumberOfSetLines(nLines, underTest);
        Assert.assertTrue(lineExists(-1, 0, 1, 0, underTest.allLines()));
        Assert.assertTrue(lineExists(0, -1, 0, 1, underTest.allLines()));
    }

    @Test
    public void shouldBuildMultipleLines() {
        final ViewBoxLines underTest = new ViewBox(3, 1, 400, 400);

        final int nLines = underTest.buildLines(0, 0, 0);
        Assert.assertEquals(6, nLines);
        assertNumberOfSetLines(nLines, underTest);

        Assert.assertTrue(lineExists(-1.5, -1, 1.5, -1, underTest.allLines()));
        Assert.assertTrue(lineExists(-1.5, 0, 1.5, 0, underTest.allLines()));
        Assert.assertTrue(lineExists(-1.5, 1, 1.5, 1, underTest.allLines()));

        Assert.assertTrue(lineExists(-1, -1.5, -1, 1.5, underTest.allLines()));
        Assert.assertTrue(lineExists(0, -1.5, 0, 1.5, underTest.allLines()));
        Assert.assertTrue(lineExists(1, -1.5, 1, 1.5, underTest.allLines()));
    }

    @Test
    public void shouldBuildMultipleLinesWhenBoxIsNotSquared() {
        final ViewBoxLines underTest = new ViewBox(3, 1, 400, 800);

        final int nLines = underTest.buildLines(0, 0, 0);
        Assert.assertEquals(8, nLines);
        assertNumberOfSetLines(nLines, underTest);

        Assert.assertTrue(lineExists(-1.5, -2, 1.5, -2, underTest.allLines()));
        Assert.assertTrue(lineExists(-1.5, -1, 1.5, -1, underTest.allLines()));
        Assert.assertTrue(lineExists(-1.5, 0, 1.5, 0, underTest.allLines()));
        Assert.assertTrue(lineExists(-1.5, 1, 1.5, 1, underTest.allLines()));
        Assert.assertTrue(lineExists(-1.5, 2, 1.5, 2, underTest.allLines()));

        Assert.assertTrue(lineExists(-1, -3, -1, 3, underTest.allLines()));
        Assert.assertTrue(lineExists(0, -3, 0, 3, underTest.allLines()));
        Assert.assertTrue(lineExists(1, -3, 1, 3, underTest.allLines()));
    }

    @Test
    public void shouldBuildLinesAfterRotation() {
        final ViewBoxLines underTest = new ViewBox(2, 5, 400, 400);

        final int nLines = underTest.buildLines(0, 0, Math.PI/2);
        Assert.assertEquals(2, nLines);
        assertNumberOfSetLines(nLines, underTest);
        Assert.assertTrue(lineExists(-1, 0, 1, 0, underTest.allLines()));
        Assert.assertTrue(lineExists(0, -1, 0, 1, underTest.allLines()));
    }

    @Test
    public void shouldBuildLinesAfter45DegreeRotation() {
        final ViewBoxLines underTest = new ViewBox(2, 5, 400, 400);

        final int nLines = underTest.buildLines(0, 0, Math.PI/4);
        Assert.assertEquals(2, nLines);
        assertNumberOfSetLines(nLines, underTest);
        Assert.assertTrue(lineExists(1, -1, -1, 1, underTest.allLines()));
        Assert.assertTrue(lineExists(-1, -1, 1, 1, underTest.allLines()));
    }

    @Test
    public void shouldBuildLinesAfterTranslation() {
        final ViewBoxLines underTest = new ViewBox(2, 5, 400, 400);

        final int nLines = underTest.buildLines(5, 5, 0);
        Assert.assertEquals(2, nLines);
        assertNumberOfSetLines(nLines, underTest);
        Assert.assertTrue(lineExists(-1, 0, 1, 0, underTest.allLines()));
        Assert.assertTrue(lineExists(0, -1, 0, 1, underTest.allLines()));
    }

    @Test
    public void shouldBuildLinesAfterRotationAndRotation() {
        final ViewBoxLines underTest = new ViewBox(2, 5, 400, 400);

        final int nLines = underTest.buildLines(5, 5, Math.PI/2);
        Assert.assertEquals(2, nLines);
        assertNumberOfSetLines(nLines, underTest);
        Assert.assertTrue(lineExists(-1, 0, 1, 0, underTest.allLines()));
        Assert.assertTrue(lineExists(0, -1, 0, 1, underTest.allLines()));
    }

    private void assertNumberOfSetLines(int expected, ViewBoxLines underTest) {
        Assert.assertEquals(expected, underTest.numberOfSetLines());

        final Line[] lines = underTest.allLines();
        Assert.assertNotNull(lines);

        int actual = 0;
        for (Line line : lines) {
            if (line.isNotNull()) {
                actual++;
            }
        }

        Assert.assertEquals(expected, actual);
    }

    private boolean lineExists(double xStart, double yStart, double xEnd, double yEnd, Line[] lines) {
        for (Line line : lines) {
            if (line.isNotNull() && isLine(xStart, yStart, xEnd, yEnd, line)) {
                return true;
            }
        }

        return false;
    }

    private boolean isLine(double xStart, double yStart, double xEnd, double yEnd, Line line) {
        final boolean startToEnd =  isCloseEnough(xStart, line.start.x) &&
                isCloseEnough(yStart, line.start.y) &&
                isCloseEnough(xEnd, line.end.x) &&
                isCloseEnough(yEnd, line.end.y);

        final boolean endToStart =  isCloseEnough(xStart, line.end.x) &&
                isCloseEnough(yStart, line.end.y) &&
                isCloseEnough(xEnd, line.start.x) &&
                isCloseEnough(yEnd, line.start.y);

        return (startToEnd || endToStart);
    }

    private boolean isCloseEnough(double expected, double actual) {
        return Math.abs(actual - expected) <= accuracy;
    }
}
