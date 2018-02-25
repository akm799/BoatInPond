package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

/**
 * Created by Thanos Mavroidis on 25/02/2018.
 */
public final class BoatPerformance {
    public final double launchSpeed;
    public final double distanceLimit;

    public final double turnRate;
    public final double turningSpeed;

    public BoatPerformance(double launchSpeed, double distanceLimit, double turnRate, double turningSpeed) {
        this.launchSpeed = launchSpeed;
        this.distanceLimit = distanceLimit;
        this.turnRate = turnRate;
        this.turningSpeed = turningSpeed;
    }
}
