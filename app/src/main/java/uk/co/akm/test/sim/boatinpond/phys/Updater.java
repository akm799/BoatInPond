package uk.co.akm.test.sim.boatinpond.phys;

/**
 * Created by Thanos Mavroidis on 11/11/2017.
 */
public class Updater {

    public static void update(Updatable updatable, double time, int nSteps) {
        final double dt = time/nSteps;
        for (int i=0 ; i<nSteps ; i++) {
            updatable.update(dt);
        }
    }

    private Updater() {}
}
