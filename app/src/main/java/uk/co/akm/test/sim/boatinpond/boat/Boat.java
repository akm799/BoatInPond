package uk.co.akm.test.sim.boatinpond.boat;


import uk.co.akm.test.sim.boatinpond.phys.UpdatableState;

/**
 * Created by Thanos Mavroidis on 02/12/2017.
 */
public interface Boat extends UpdatableState {

    Rudder getRudder();
}
