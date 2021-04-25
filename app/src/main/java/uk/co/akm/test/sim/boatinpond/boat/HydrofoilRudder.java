package uk.co.akm.test.sim.boatinpond.boat;

/**
 * Implementations of this interface should model the hydrodynamic force applied on the rudder
 * and return the drag and lift coefficient components of that force. These components can be
 * used to estimate the hydrodynamic drag and lift forces acting on the rudder which, in turn,
 * can be used to calculate the torque that the rudder generates.
 */
public interface HydrofoilRudder extends Rudder {

    /**
     * Returns half the length of the rudder measured across the boat bow-stern axis. It is assumed
     * that the rudder is attached directly at the boats stern. The rudder depth at which the length
     * is measured corresponds to the rudders centroid.
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
     * Returns {@code (rawValue - d0)/(dMax - d0)} where {@code d0} is the
     * drag coefficient at zero deflection angle and {@code dMax} is the
     * drag coefficient at the maximum deflection angle
     *
     * @param rawValue the drag coefficient value to be normalised
     * @return a normalised value between 0 and 1, where 0 corresponds to the value
     * of the drag coefficient at zero deflection angle and 1 to the value at the
     * maximum deflection angle
     */
    double normaliseDragCoefficient(double rawValue);

    /**
     * Returns the lift coefficient for the current rudder deflection angle.
     *
     * @return the lift coefficient for the current rudder deflection angle
     */
    double getLiftCoefficient();
}
