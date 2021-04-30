package uk.co.akm.test.sim.boatinpond.boat.performance.helper;


import uk.co.akm.test.sim.boatinpond.phys.Updatable;

public final class BoatPerformanceTestHelper {

    public static void update(Updatable updatable, double dt, double secs) {
        final long n = Math.round(secs/dt);
        for (int i=0 ; i<n ; i++) {
            updatable.update(dt);
        }
    }

    /**
     * Returns the angle between two vectors. This method does not work very well for very small angles.
     *
     * @param x1 the abscissa of the first vector
     * @param y1 the ordinate of the first vector
     * @param x2 the abscissa of the second vector
     * @param y2 the ordinate of the second vector
     * @return the angle between two vectors
     */
    public static double angleBetween(double x1, double y1, double x2, double y2) {
        return Math.acos( (x1*x2 + y1*y2)/(magnitude(x1, y1)*magnitude(x2, y2)) );
    }

    private static double magnitude(double x, double y) {
        return Math.sqrt(x*x + y*y);
    }

    private BoatPerformanceTestHelper() {}
}
