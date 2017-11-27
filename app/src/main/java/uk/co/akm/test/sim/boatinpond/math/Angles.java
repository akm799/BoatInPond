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
            return (ap - TWO_PI);
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

    /**
     * Converts an angle in degrees, which lies within the proper range (-180, 180) to the equivalent
     * compass heading from 0 to 180 degrees. The north heading (0 degrees) is given by a 90 degree input.
     * In general the input values correspond to the following headings:
     *
     *   90 --> North   (0)
     *    0 --> East   (90)
     *  -90 --> South (180)
     * -180 --> West  (270)
     *  180 --> West  (270)
     *
     * @param deg the input angle in degrees, laying within the proper range (-180, 180)
     * @return the equivalent compass heading
     */
    public static double toCompassHeading(double deg) {
        if (deg < 0) {
            return 90 - deg;
        } else {
            final double h = 90 - deg;

            return (h >= 0 ? h : 360 + h);
        }
    }

    private Angles() {}
}
