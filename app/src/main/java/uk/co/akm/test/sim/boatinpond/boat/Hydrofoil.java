package uk.co.akm.test.sim.boatinpond.boat;

/**
 * Implementations of this interface should model the lift and drag coefficients of a hydrofoil
 * for a range of flow incidence angles or angles of attack.
 */
public interface Hydrofoil {

    /**
     * Returns the drag coefficient for the given angle of attack.
     *
     * @param angleOfAttack the angle of attack in radians
     *
     * @return the drag coefficient for the given angle of attack
     */
    double getDragCoefficient(double angleOfAttack);

    /**
     * Returns the lift coefficient for the given angle of attack.
     *
     * @param angleOfAttack the angle of attack in radians
     *
     * @return the lift coefficient for the given angle of attack
     */
    double getLiftCoefficient(double angleOfAttack);

    /**
     * Returns the angle of attack at which the lift coefficient is maximised
     *
     * @return the angle of attack at which the lift coefficient is maximised
     */
    double getMaxLiftAngleOfAttack();
}
