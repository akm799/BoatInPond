package uk.co.akm.test.sim.boatinpond.boat.factory.impl.quad;

import uk.co.akm.test.sim.boatinpond.boat.factory.IndicatorValues;

/**
 * Created by Thanos Mavroidis on 11/03/2018.
 */
public final class TargetValueCalculator implements IndicatorValues {
    private static final double indicatorRange = (MAX_INDICATOR_SIZE - MIN_INDICATOR_SIZE);

    TargetValueCalculator() {}

    void checkIndicator(int indicator, String name) {
        if (MIN_INDICATOR_SIZE > indicator || indicator > MAX_INDICATOR_SIZE) {
            throw new IllegalArgumentException("Illegal " + name + " indicator value: " + indicator + ". It is not within the range (0, 100).");
        }
    }

    double computeValue(int indicator, double minValue, double maxValue, String name) {
        checkValues(indicator, minValue, maxValue, name);
        final double valueRange = maxValue - minValue;
        final double fraction = indicator/indicatorRange;

        return (minValue + fraction*valueRange);
    }

    private void checkValues(int indicator, double minValue, double maxValue, String name) {
        checkIndicator(indicator, name);

        if (minValue >= maxValue) {
            throw new IllegalArgumentException("minValue=" + minValue + ", for " + name + " indicator, cannot be greater or equal to the maxValue=" + maxValue);
        }
    }
}
