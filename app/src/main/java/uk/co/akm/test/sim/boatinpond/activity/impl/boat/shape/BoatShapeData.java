package uk.co.akm.test.sim.boatinpond.activity.impl.boat.shape;

/**
 * Holds constants that define the proportions of a simple boat shape. It is basic boat shape, when
 * viewed from above, consisting of a rectangular main body with a triangular bow section.
 *
 * Created by Thanos Mavroidis on 17/03/2018.
 */
public final class BoatShapeData {

    /**
     * The ratio of the length overall over the beam.
     */
    final float lengthOverBeamRatio;

    /**
     * The ratio of the length of the boat body excluding the bow section over the length overall.
     */
    final float mainBodyFraction = 0.75f;

    /**
     * The ratio of the rudder length over the lengh overall.
     */
    final float rudderLengthOverTotalLengthRatio = 0.1f;

    /**
     * The ratio of the width of the line drawing the boat shape outline over the actual beam.
     */
    final float outlineWidthOverBeamRatio = 0.1f;

    BoatShapeData() {
        final double loa = 27;
        final double beam = Math.pow(loa, 2.0/3.0) + 1; // https://en.wikipedia.org/wiki/Beam_(nautical)
        lengthOverBeamRatio = (float)(loa/beam);
    }
}
