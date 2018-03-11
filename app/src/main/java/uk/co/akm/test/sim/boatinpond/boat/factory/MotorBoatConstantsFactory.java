package uk.co.akm.test.sim.boatinpond.boat.factory;

import uk.co.akm.test.sim.boatinpond.boat.MotorBoatConstants;

/**
 * Created by Thanos Mavroidis on 11/03/2018.
 */
public interface MotorBoatConstantsFactory extends IndicatorValues {

    /**
     * Returns a #MotorBoatConstants instance with a rudder sized and maximum motor power according
     * to the input, arbitrary, rudder size and motor power indicators. Both indicators can only take
     * values from {@link #MIN_INDICATOR_SIZE} to {@link #MAX_INDICATOR_SIZE}. Higher rudder size
     * indicator values will result in greater turning performance and increased longitudinal resistance
     * while turning. While motor power indicator values will result in higher available maximum motor
     * power. Lower values will have the opposite effects.
     *
     * @param rudderSizeIndicator an arbitrary indicator, between {@link #MIN_INDICATOR_SIZE} and
     * {@link #MAX_INDICATOR_SIZE}, denoting the size of the rudder
     * @param maxMotorPowerIndicator an arbitrary indicator, between {@link #MIN_INDICATOR_SIZE} and
     * {@link #MAX_INDICATOR_SIZE}, denoting the maximum available motor power
     * @return a #MotorBoatConstants instance with a rudder sized according to the input, arbitrary, rudder
     * size and maximum motor power indicators
     */
    MotorBoatConstants instance(int rudderSizeIndicator, int maxMotorPowerIndicator);
}
