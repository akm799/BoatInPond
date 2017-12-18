package uk.co.akm.test.sim.boatinpond.boat;

/**
 * Interface that gives access to all boat dependent constants required to calculate the boat kinematics.
 *
 * Created by Thanos Mavroidis on 16/12/2017.
 */
public interface BoatConstants {

    /**
     * Returns the total resistance coefficient kLon along the boat direction such that if vLon is
     * the boat velocity along that direction, then the resistance force frLon along the boat direction
     * is frLon = -kLon*vLon^2
     *
     * @return the total resistance coefficient along the boat direction
     */
    double getkLon();

    /**
     * Returns the total resistance coefficient kLat perpendicular to the boat direction such that if
     * vLat is the boat velocity along that direction, then the resistance force frLat perpendicular
     * to the boat direction is frLat = -kLat*vLat^2
     *
     * @return the total resistance coefficient perpendicular to the boat direction
     */
    double getkLat();

    /**
     * Returns the total mass of the boat (empty mass plus load).
     *
     * @return the total mass of the boat (empty mass plus load)
     */
    double getMass();



    @Deprecated
    double getkRud();

    @Deprecated
    double getkAng();
}
