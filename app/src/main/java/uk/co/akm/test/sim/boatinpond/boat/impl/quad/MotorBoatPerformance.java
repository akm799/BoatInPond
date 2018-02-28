package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

/**
 * Created by Thanos Mavroidis on 28/02/2018.
 */
public class MotorBoatPerformance extends BoatPerformance {
    public final double maxSpeed;

    public MotorBoatPerformance(double launchSpeed, double distanceLimit, double turnRate, double turningSpeed, double maxSpeed) {
        super(launchSpeed, distanceLimit, turnRate, turningSpeed);

        this.maxSpeed = maxSpeed;
    }
}
