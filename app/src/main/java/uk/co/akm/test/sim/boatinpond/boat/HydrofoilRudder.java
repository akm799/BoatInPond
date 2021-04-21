package uk.co.akm.test.sim.boatinpond.boat;

public interface HydrofoilRudder extends Rudder {

    /**
     * Returns the length of the rudder measured across the boat bow-stern axis.
     *
     * @return the length of the rudder measured across the boat bow-stern axis
     */
    double getLength();

    /**
     * Returns the drag coefficient for the current rudder deflection angle.
     *
     * @return the drag coefficient for the current rudder deflection angle
     */
    double getDragCoefficient();

    /**
     * Returns the lift coefficient for the current rudder deflection angle.
     *
     * @return the lift coefficient for the current rudder deflection angle
     */
    double getLiftCoefficient();
}
