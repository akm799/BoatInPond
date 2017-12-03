package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;
import uk.co.akm.test.sim.boatinpond.graph.Line;
import uk.co.akm.test.sim.boatinpond.graph.Point;
import uk.co.akm.test.sim.boatinpond.graph.ViewBox;
import uk.co.akm.test.sim.boatinpond.graph.ViewBoxFeatures;
import uk.co.akm.test.sim.boatinpond.math.Angle;
import uk.co.akm.test.sim.boatinpond.math.Angles;
import uk.co.akm.test.sim.boatinpond.phys.State;
import uk.co.akm.test.sim.boatinpond.view.ViewData;

/**
 * Created by Thanos Mavroidis on 29/11/2017.
 */
final class BoatViewBox implements ViewData<Boat> {
    private static final double EARTH_RADIUS = 6371000;
    private static final double METRES_PER_SEC_TO_KNOTS = 1.94384;

    private final ViewBoxFeatures viewBox;

    private String coordinates;
    private String compassHeading;
    private String speed;
    private final float[] rudderPlotFractions = new float[2];

    private NumberFormat latLongFormat = new DecimalFormat("0.0000");
    private NumberFormat speedFormat = new DecimalFormat("0.00");
    private NumberFormat compassFormat = new DecimalFormat("000");

    BoatViewBox(double horizontalSide, double lineSpacing, int screenWidth, int screenHeight) {
        viewBox = new ViewBox(horizontalSide, lineSpacing, screenWidth, screenHeight);
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
    public void additionalData(Boat state) {
        setRudderPlotFractions(state.getRudder().getRudderAngle());

        final Angle longitude = new Angle(state.x()/EARTH_RADIUS);
        final Angle latitude = new Angle(state.y()/EARTH_RADIUS);
        coordinates = (latitude.toLat(latLongFormat) + "   " + longitude.toLong(latLongFormat));

        compassHeading = compassFormat.format(Math.round(Angles.toCompassHeading(Angles.toDeg(state.hdnP()))));
        speed = speedFormat.format(METRES_PER_SEC_TO_KNOTS*state.v());
    }

    private void setRudderPlotFractions(double rudderAngle) {
        rudderPlotFractions[0] = (float)Math.sin(rudderAngle);
        rudderPlotFractions[1] = (float)Math.cos(rudderAngle);
    }

    public float[] getRudderPlotFractions() {
        return rudderPlotFractions;
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