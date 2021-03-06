package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

/**
 * Defines the linear motion and turning motion boat performance by specifying parameters for 2 scenarios:
 *
 * 1) The boat is launched with an initial speed V0 and is let to slow down by the water resistance.
 *    As a result there will be a theoretical distance D which the boat will always approach but never
 *    reach. The parameters V0 and D are used to define the linear motion performance characteristics.
 *
 * 2) The boat is turning at constant speed V describing a circle with radius R. This is with enough
 *    motor power to sustain a constant speed and under full rudder deflection. The parameters V and R
 *    are used to define the turning motion performance characteristics.
 *
 * The time taken for the rudder to reach maximum deflection is, also, defined.
 *
 * Created by Thanos Mavroidis on 25/02/2018.
 */
public class BoatPerformance {
    /**
     * The initial speed with which the boat must be launched to be able to (almost) traverse the distance defined by {@link #distanceLimit}
     */
    public final double launchSpeed;

    /**
     * The distance that the boat will be able to (almost) traverse if launched with an initial speed defined by {@link #launchSpeed}
     */
    public final double distanceLimit;



    /**
     * The turn radius that the boat will achieve when constantly turning, under most efficient rudder deflection, at a turning speed defined in {@link #turningSpeed}
     */
    public final double turnRadius;

    /**
     * The constant speed that the boat will be travelling when constantly turning, under most efficient rudder deflection, with a turn radius defined in {@link #turnRadius}
     */
    public final double turningSpeed;


    /**
     * The time it takes the rudder, starting from the central position, to reach the maximum deflection angle.
     */
    public final double timeToMaxRudderDeflection;

    public BoatPerformance(double launchSpeed, double distanceLimit, double turningSpeed, double turnRadius, double timeToMaxRudderDeflection) {
        this.launchSpeed = launchSpeed;
        this.distanceLimit = distanceLimit;
        this.turnRadius = turnRadius;
        this.turningSpeed = turningSpeed;
        this.timeToMaxRudderDeflection = timeToMaxRudderDeflection;
    }
}
