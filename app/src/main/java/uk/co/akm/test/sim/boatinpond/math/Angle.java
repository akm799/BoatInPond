package uk.co.akm.test.sim.boatinpond.math;

import java.text.NumberFormat;

/**
 * Created by Thanos Mavroidis on 27/11/2017.
 */
public final class Angle {
    public final boolean positive;
    public final int degrees;
    public final int minutes;
    public final double seconds;

    public Angle(double rad) {
        positive = (rad >= 0);
        final double deg = Angles.toDeg(Math.abs(rad));
        degrees = (int)Math.floor(deg);

        final double min = (deg - degrees)*60;
        minutes = (int)Math.floor(min);

        seconds = (min - minutes)*60;
    }

    @Override
    public String toString() {
        return ((positive ? "" : "- ") + degrees + " " + minutes + "' " + seconds + "''");
    }

    public String toLat(NumberFormat secondsFormat) {
        return toLatOrLong(secondsFormat, (positive ? 'N' : 'S'));
    }

    public String toLong(NumberFormat secondsFormat) {
        return toLatOrLong(secondsFormat, (positive ? 'E' : 'W'));
    }

    private String toLatOrLong(NumberFormat secondsFormat, char direction) {
        return (degrees + " " + minutes + "' " + secondsFormat.format(seconds) + "'' " + direction);
    }
}
