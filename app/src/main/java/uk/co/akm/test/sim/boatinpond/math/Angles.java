package uk.co.akm.test.sim.boatinpond.math;

/**
 * Utility calculations for angles.
 *
 * Created by Thanos Mavroidis on 12/11/2017.
 */
public class Angles {
    private static final double TWO_PI = 2*Math.PI;

    public static double toDeg(double rad) {
        return 180*rad/Math.PI;
    }

    public static double toRad(double deg) {
        return Math.PI*deg/180;
    }

    /**
     * Converts an arbitrary angle in radians to the equivalent angle in the (proper) range -pi to pi.
     *
     * @param a the arbitrary angle in radians
     * @return the equivalent angle in the (proper) range -pi to pi
     */
    public static double toProperAngle(double a) {
        if (-Math.PI < a && a < Math.PI) {
            return a;
        }

        final double ap = zeroToTwoPi(a);
        if (ap > Math.PI) {
            return (Math.PI - ap);
        } else if (ap < -Math.PI) {
            return -(ap + Math.PI);
        } else {
            return ap;
        }
    }

    private static double zeroToTwoPi(double a) {
        final double f = Math.abs(a) / TWO_PI;
        final double ap = (f - Math.floor(f)) * TWO_PI;

        return (a > 0 ? ap : -ap);
    }

    private Angles() {}
}
