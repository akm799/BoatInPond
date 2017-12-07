package uk.co.akm.test.sim.boatinpond.graph;

import uk.co.akm.test.sim.boatinpond.math.TrigValues;

/**
 * Created by Thanos Mavroidis on 19/11/2017.
 */
public interface Transformable {

    void translate(double x, double y);

    void rotate(double a);

    void fastRotate(TrigValues a);
}
