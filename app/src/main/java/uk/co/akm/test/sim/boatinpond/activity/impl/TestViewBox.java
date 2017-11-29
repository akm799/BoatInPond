package uk.co.akm.test.sim.boatinpond.activity.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import uk.co.akm.test.sim.boatinpond.env.Environment;
import uk.co.akm.test.sim.boatinpond.graph.Line;
import uk.co.akm.test.sim.boatinpond.graph.Point;
import uk.co.akm.test.sim.boatinpond.graph.ViewBox;
import uk.co.akm.test.sim.boatinpond.graph.ViewBoxLines;
import uk.co.akm.test.sim.boatinpond.math.Angle;
import uk.co.akm.test.sim.boatinpond.math.Angles;
import uk.co.akm.test.sim.boatinpond.phys.State;
import uk.co.akm.test.sim.boatinpond.view.ViewData;

/**
 * Created by Thanos Mavroidis on 27/11/2017.
 */
final class TestViewBox implements ViewData<TestBody> {
    private static final double EARTH_RADIUS = 6371000;
    private static final double METRES_PER_SEC_TO_KNOTS = 1.94384;

    private final ViewBoxLines viewBox;

    private String coordinates;
    private String compassHeading;
    private String speed;

    private NumberFormat latLongFormat = new DecimalFormat("0.0000");
    private NumberFormat speedFormat = new DecimalFormat("0.00");
    private NumberFormat compassFormat = new DecimalFormat("000");

    TestViewBox(double horizontalSide, double lineSpacing, int screenWidth, int screenHeight) {
        final Environment env = new TestEnvironment();
        viewBox = new ViewBox(env, horizontalSide, lineSpacing, screenWidth, screenHeight);
    }

    @Override
    public void buildFeatures(State state) {
        viewBox.buildFeatures(state);
    }

    @Override
    public int numberOfSetLines() {
        return viewBox.numberOfSetLines();
    }

    @Override
    public Line[] allLines() {
        return viewBox.allLines();
    }

    @Override
    public int numberOfSetFixedPoints() {
        return viewBox.numberOfSetFixedPoints();
    }

    @Override
    public Point[] allFixedPoints() {
        return viewBox.allFixedPoints();
    }

    @Override
    public void additionalData(TestBody state) {
        final Angle longitude = new Angle(state.x()/EARTH_RADIUS);
        final Angle latitude = new Angle(state.y()/EARTH_RADIUS);
        coordinates = (latitude.toLat(latLongFormat) + "   " + longitude.toLong(latLongFormat));

        compassHeading = compassFormat.format(Math.round(Angles.toCompassHeading(Angles.toDeg(state.hdnP()))));
        speed = speedFormat.format(METRES_PER_SEC_TO_KNOTS*state.v());
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getCompassHeading() {
        return compassHeading;
    }

    public String getSpeed() {
        return speed;
    }

    private static final class TestEnvironment implements Environment {
        private final Point[] fixedPoints = {new Point(-10, 0), new Point(-5, 0), new Point(0, 0), new Point(5, 0), new Point(10, 0)};

        @Override
        public int getNumberOfFixedPoints() {
            return fixedPoints.length;
        }

        @Override
        public Point[] getFixedPoints() {
            return fixedPoints;
        }
    }
}
