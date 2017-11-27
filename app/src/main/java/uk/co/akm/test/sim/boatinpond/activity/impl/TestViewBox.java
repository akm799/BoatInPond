package uk.co.akm.test.sim.boatinpond.activity.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import uk.co.akm.test.sim.boatinpond.graph.Line;
import uk.co.akm.test.sim.boatinpond.graph.ViewBox;
import uk.co.akm.test.sim.boatinpond.math.Angle;
import uk.co.akm.test.sim.boatinpond.math.Angles;
import uk.co.akm.test.sim.boatinpond.view.ViewData;

/**
 * Created by Thanos Mavroidis on 27/11/2017.
 */
final class TestViewBox implements ViewData<TestBody> {
    private static final double EARTH_RADIUS = 6371000;
    private static final double METRES_PER_SEC_TO_KNOTS = 1.94384;

    private final ViewBox viewBox;

    private String coordinates;
    private String compassHeading;
    private String speed;

    private NumberFormat latLongFormat = new DecimalFormat("0.0000");
    private NumberFormat speedFormat = new DecimalFormat("0.00");
    private NumberFormat compassFormat = new DecimalFormat("000");

    TestViewBox(double horizontalSide, double lineSpacing, int screenWidth, int screenHeight) {
        viewBox = new ViewBox(horizontalSide, lineSpacing, screenWidth, screenHeight);
    }

    @Override
    public int buildLines(double x, double y, double a) {
        return viewBox.buildLines(x, y, a);
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
}
