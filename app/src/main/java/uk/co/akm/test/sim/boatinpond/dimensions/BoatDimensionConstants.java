package uk.co.akm.test.sim.boatinpond.dimensions;

/**
 * Class holding constants that define the dimensions of the view box and the boat drawn at its centre.
 *
 * Created by Thanos Mavroidis on 18/03/2018.
 */
public class BoatDimensionConstants {
    /**
     * The width of the (rectangular) area shown in the (rectangular) view box, in metres.
     */
    public static final double VIEW_BOX_WIDTH = 30;

    /**
     * The spacing of the lines of the view box, in metres.
     */
    public static final double VIEW_BOX_LINE_SPACING = 5;

    /**
     * The beam of the boat shown, in metres. Please note that the length of the boat is defined in
     * terms of its beam by a fix analogy in #BoatShapeData.
     */
    public static final double BOAT_BEAM = 2.25;

    static {
        if (VIEW_BOX_LINE_SPACING >= VIEW_BOX_WIDTH) {
            throw new IllegalStateException("Incompatible constants defined in BoatDimensionConstants: view box line spacing cannot be greater than or equal to its total width.");
        }

        if (BOAT_BEAM >= VIEW_BOX_WIDTH) {
            throw new IllegalStateException("Incompatible constants defined in BoatDimensionConstants: boat beam cannot be greater than or equal to the total view box width.");
        }
    }

    private BoatDimensionConstants() {}
}
