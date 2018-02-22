package uk.co.akm.test.sim.boatinpond.boat.impl.linear;

import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;

/**
 * Created by Thanos Mavroidis on 22/02/2018.
 */

public interface LinearBoatConstants extends BoatConstants {

    /**
     * Returns the total torque resistance coefficient of the boat. This coefficient multiplied by the
     * square of the angular velocity (of the heading angle) gives the total magnitude of the torque
     * resisting the turn of the boat.
     *
     * @return the total torque resistance coefficient of the boat
     */
    double getkAng();
}
