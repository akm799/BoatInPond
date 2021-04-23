package uk.co.akm.test.sim.boatinpond.activity.impl.boat;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.game.GameConstants;
import uk.co.akm.test.sim.boatinpond.graph.Line;
import uk.co.akm.test.sim.boatinpond.graph.Point;
import uk.co.akm.test.sim.boatinpond.graph.ViewBoxFast;
import uk.co.akm.test.sim.boatinpond.graph.ViewBoxFeatures;
import uk.co.akm.test.sim.boatinpond.math.Angle;
import uk.co.akm.test.sim.boatinpond.math.Angles;
import uk.co.akm.test.sim.boatinpond.phys.State;
import uk.co.akm.test.sim.boatinpond.view.ViewData;

/**
 * Created by Thanos Mavroidis on 29/11/2017.
 */
final class BoatViewBox implements ViewData<Boat> {
    private static final String DOUBLE_ZERO = "00";
    private static final char DEGREES_CHAR = '\u00b0';
    private static final double EARTH_RADIUS = 6371000;
    private static final double METRES_PER_SEC_TO_KNOTS = 1.94384;

    private final NumberFormat latLongFormat = new DecimalFormat("0.00");
    private final NumberFormat speedFormat = new DecimalFormat("0.00 Kts");
    private final NumberFormat compassFormat = new DecimalFormat("000" + DEGREES_CHAR);
    private final NumberFormat rudderAngleFormat = new DecimalFormat(DOUBLE_ZERO);

    private final ViewBoxFeatures viewBox;

    private String coordinates;
    private String compassHeading;
    private String speed;
    private String leftRudderDeflection;
    private String rightRudderDeflection;
    private final float[] rudderPlotFractions = new float[GameConstants.LEN_2D];

    BoatViewBox(double horizontalSide, double lineSpacing, int screenWidth, int screenHeight) {
        viewBox = new ViewBoxFast(horizontalSide, lineSpacing, screenWidth, screenHeight);
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
        final double rudderAngle = state.getRudder().getRudderAngle();
        setRudderText(rudderAngle);
        setRudderPlotFractions(rudderAngle);

        final Angle longitude = new Angle(state.x()/EARTH_RADIUS);
        final Angle latitude = new Angle(state.y()/EARTH_RADIUS);
        coordinates = (latitude.toLat(latLongFormat) + "   " + longitude.toLong(latLongFormat));

        compassHeading = compassFormat.format(computeCompassHeadingInDegrees(state));
        speed = speedFormat.format(METRES_PER_SEC_TO_KNOTS*state.v());
    }

    private void setRudderPlotFractions(double rudderAngle) {
        rudderPlotFractions[GameConstants.X_INDEX] = (float)Math.sin(rudderAngle);
        rudderPlotFractions[GameConstants.Y_INDEX] = (float)Math.cos(rudderAngle);
    }

    private void setRudderText(double rudderAngle) {
        final long deg = Math.round(Angles.toDeg(rudderAngle));

        if (deg > 0) {
            leftRudderDeflection = rudderAngleFormat.format(deg);
            rightRudderDeflection = DOUBLE_ZERO;
        } else if (deg < 0) {
            leftRudderDeflection = DOUBLE_ZERO;
            rightRudderDeflection = rudderAngleFormat.format(-deg);
        } else {
            rightRudderDeflection = DOUBLE_ZERO;
            leftRudderDeflection = DOUBLE_ZERO;
        }
    }

    private long computeCompassHeadingInDegrees(Boat state) {
        final long degrees = Math.round(Angles.toCompassHeading(Angles.toDeg(state.hdnP())));

        return (degrees == 360 ? 0 : degrees);
    }

    public String getLeftRudderDeflection() {
        return leftRudderDeflection;
    }

    public String getRightRudderDeflection() {
        return rightRudderDeflection;
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
