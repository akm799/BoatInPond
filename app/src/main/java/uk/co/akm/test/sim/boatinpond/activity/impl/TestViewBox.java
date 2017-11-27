package uk.co.akm.test.sim.boatinpond.activity.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import uk.co.akm.test.sim.boatinpond.graph.Line;
import uk.co.akm.test.sim.boatinpond.graph.ViewBox;
import uk.co.akm.test.sim.boatinpond.view.ViewData;

/**
 * Created by Thanos Mavroidis on 27/11/2017.
 */
final class TestViewBox implements ViewData<TestBody> {
    private static final double METRES_PER_SEC_TO_KNOTS = 1.94384;

    private final ViewBox viewBox;

    private String longitude;
    private String latitude;
    private String compassHeading;
    private String speed;

    private NumberFormat speedFormat = new DecimalFormat("#.00");

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
        longitude = speedFormat.format(state.x());
        latitude = speedFormat.format(state.y());
        compassHeading = speedFormat.format(toCompassHeading(180*state.hdnP()/Math.PI));
        speed = speedFormat.format(METRES_PER_SEC_TO_KNOTS*state.v());
    }

    private double toCompassHeading(double deg) {
        if (deg < 0) {
            return 90 - deg;
        } else {
            final double h = 90 - deg;

            return (h >= 0 ? h : 360 + h);
        }
    }

    public String getCoordinates() {
        return ("(" + longitude + ", " + latitude + ")");
    }

    public String getCompassHeading() {
        return compassHeading;
    }

    public String getSpeed() {
        return speed;
    }
}
