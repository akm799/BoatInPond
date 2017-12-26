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

    /**
     * Returns the boat moment of inertia.
     *
     * @return the boat moment of inertia
     */
    double getMomentOfInertia();

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
     * Returns the product: 0.5*r*A*k*k where:
     * r: the water density
     * A: the rudder surface area exposed to the water flow
     * k: the ratio of the water speed after rudder deflection over the water speed before rudder deflection
     *
     * @return a coefficient used in the rudder force calculation.
     */
    double getkRud();

    /**
     * Returns the total torque resistance coefficient of the boat. This coefficient multiplied by the
     * square of the angular velocity (of the heading angle) gives the total magnitude of the torque
     * resisting the turn of the boat.
     *
     * @return the total torque resistance coefficient of the boat
     */
    double getkAng();
}
