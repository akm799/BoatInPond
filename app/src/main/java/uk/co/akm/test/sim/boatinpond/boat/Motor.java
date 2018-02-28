package uk.co.akm.test.sim.boatinpond.boat;

import uk.co.akm.test.sim.boatinpond.phys.Updatable;

/**
 * Created by Thanos Mavroidis on 27/02/2018.
 */
public interface Motor extends Updatable {
    int DECREASE = -1;
    int NONE = 0;
    int INCREASE = 1;

    boolean isOn();

    void turnOn();

    void turnOff();

    void noControlInput();

    void increaseControlInput();

    void decreaseControlInput();

    /**
     * @return the current propulsion force exerted on the boat by the motor
     */
    double getForce();

    /**
     * @return the maximum propulsion force supplied by the motor
     */
    double getMaxForce();
}
