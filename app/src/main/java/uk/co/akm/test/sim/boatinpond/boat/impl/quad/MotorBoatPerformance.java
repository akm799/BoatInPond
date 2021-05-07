package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

/**
 * Parameters defining the motor performance.
 *
 * Created by Thanos Mavroidis on 28/02/2018.
 */
public class MotorBoatPerformance extends BoatPerformance {
    /**
     * The maximum boat speed attainable under full motor power.
     */
    public final double maxSpeed;

    /**
     * The time taken for the motor, starting from idle, to reach maximum power.
     */
    public final double timeToMaxPower;

    public MotorBoatPerformance(double launchSpeed, double distanceLimit, double turningSpeed, double turnRadius, double timeToMaxRudderDeflection, double maxSpeed, double timeToMaxPower) {
        super(launchSpeed, distanceLimit, turningSpeed, turnRadius, timeToMaxRudderDeflection);

        this.maxSpeed = maxSpeed;
        this.timeToMaxPower = timeToMaxPower;
    }
}
