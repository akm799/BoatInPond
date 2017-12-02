package uk.co.akm.test.sim.boatinpond.boat;

import uk.co.akm.test.sim.boatinpond.phys.Updatable;

/**
 * Created by Thanos Mavroidis on 02/12/2017.
 */
public interface Rudder extends Updatable {
    int LEFT  =  1;
    int NONE  =  0;
    int RIGHT = -1;

    void noControlInput();

    void leftControlInput();

    void rightControlInput();

    double getRudderAngle();

    double getMaxRudderAngle();
}
