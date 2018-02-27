package uk.co.akm.test.sim.boatinpond.boat;

/**
 * Interface that gives access to all boat dependent constants required to calculate the boat kinematics.
 *
 * Created by Thanos Mavroidis on 16/12/2017.
 */
public interface BoatConstants {
    /**
     * The transition speed at which we switch are resistance estimation from being proportional to
     * the boat water speed to being proportional to the square of the boat water speed.
     */
    double V_TRANSITION = 1;

    /**
     * Returns the total resistance coefficient kLon along the boat direction such that if vLon is
     * the boat velocity along that direction, then the resistance force frLon along the boat direction
     * has a magnitude frLon = kLon*vLon^2
     *
     * @return the total resistance coefficient along the boat direction
     */
    double getkLon();

    /**
     * Returns the total resistance coefficient kLat perpendicular to the boat direction such that if
     * vLat is the boat velocity along that direction, then the resistance force frLat perpendicular
     * to the boat direction has a magnitude frLat = kLat*vLat^2
     *
     * @return the total resistance coefficient perpendicular to the boat direction
     */
    double getkLat();

    /**
     * Returns the equivalent coefficient that {@link #getkLon()} does but for when the boat has a
     * negative velocity along its axis (i.e. it is reversing)
     *
     * @return the equivalent coefficient that {@link #getkLon()} does but for when the boat has a
     * negative velocity along its axis
     */
    double getKLonReverse();

    /**
     * Returns the boat length.
     *
     * @return the boat length
     */
    double getLength();

    /**
     * Returns the distance of the boat centre of mass from the stern.
     *
     * @return the distance of the boat centre of mass from the stern
     */
    double getCentreOfMassFromStern();

    /**
     * Returns the rudder coefficient k such that the rudder force at maximum deflection is k*v*v
     * where v is the boat (longitudinal) water speed.
     *
     * @return a coefficient used in the rudder force calculation
     */
    double getkRud();
}