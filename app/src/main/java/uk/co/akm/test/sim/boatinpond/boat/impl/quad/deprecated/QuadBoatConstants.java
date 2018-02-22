package uk.co.akm.test.sim.boatinpond.boat.impl.quad.deprecated;

import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;

/**
 * Created by Thanos Mavroidis on 22/02/2018.
 */

@Deprecated
public interface QuadBoatConstants extends BoatConstants {

    /**
     * Returns the total mass of the boat (empty mass plus load).
     *
     * @return the total mass of the boat (empty mass plus load)
     */
    double getMass();

    /**
     * Returns the boat moment of inertia.
     *
     * @return the boat moment of inertia
     */
    double getMomentOfInertia();

    /**
     * Returns the total torque resistance coefficient of the boat. This coefficient multiplied by the
     * square of the angular velocity (of the heading angle) gives the total magnitude of the torque
     * resisting the turn of the boat.
     *
     * @return the total torque resistance coefficient of the boat
     */
    double getkAng();
}
