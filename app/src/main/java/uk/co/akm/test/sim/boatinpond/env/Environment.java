package uk.co.akm.test.sim.boatinpond.env;

import uk.co.akm.test.sim.boatinpond.graph.Point;

/**
 * Created by Thanos Mavroidis on 28/11/2017.
 */
public interface Environment {

    int getNumberOfFixedPoints();

    Point[] getFixedPoints();
}
