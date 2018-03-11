package uk.co.akm.test.sim.boatinpond.boat.factory;

import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;

/**
 * Created by Thanos Mavroidis on 11/03/2018.
 */
public interface BoatConstantsFactory extends IndicatorValues {

    /**
     * Returns a #BoatConstants instance with a rudder sized according to the input, arbitrary, rudder
     * size indicator. This indicator can only take values from {@link #MIN_INDICATOR_SIZE} to
     * {@link #MAX_INDICATOR_SIZE} and is an arbitrary indicator of the rudder size. Higher values will
     * result in greater turning performance and increased longitudinal resistance while turning.
     * Lower values will have the opposite effect.
     *
     * @param rudderSizeIndicator an arbitrary indicator, between {@link #MIN_INDICATOR_SIZE} and
     * {@link #MAX_INDICATOR_SIZE}, denoting the size of the rudder
     * @return a #BoatConstants instance with a rudder sized according to the input, arbitrary, rudder
     * size indicator
     */
    BoatConstants instance(int rudderSizeIndicator);
}
