package uk.co.akm.test.sim.boatinpond.boat.performance.helper;


import uk.co.akm.test.sim.boatinpond.phys.Updatable;

public final class BoatPerformanceTestHelper {

    public static void update(Updatable updatable, double dt, double secs) {
        final long n = Math.round(secs/dt);
        for (int i=0 ; i<n ; i++) {
            updatable.update(dt);
        }
    }

    private BoatPerformanceTestHelper() {}
}
