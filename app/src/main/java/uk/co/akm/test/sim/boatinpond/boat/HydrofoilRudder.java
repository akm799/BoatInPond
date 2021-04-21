package uk.co.akm.test.sim.boatinpond.boat;

public interface HydrofoilRudder extends Rudder {

    /**
     * Returns half the length of the rudder measured across the boat bow-stern axis.
     *
     * @return half the length of the rudder measured across the boat bow-stern axis
     */
    double getHalfLength();

    /**
     * Returns the absolute of the rudders deflection angle.
     *
     * @return the absolute of the rudders deflection angle
     */
    double getAngleOfAttack();

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
