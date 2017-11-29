package uk.co.akm.test.sim.boatinpond.graph;

import uk.co.akm.test.sim.boatinpond.phys.State;

/**
 * Interface that builds contour lines for a state at coordinates (x,y) with a heading represented by
 * an angle a. The contour lines are equally spaced horizontal and vertical lines along the x-y plane.
 * The lines are viewed from above with the body represented always placed at the centre of the view
 * box. The horizontal and vertical contour lines will be viewed depending on the coordinates and
 * heading of the body. For example if the body heading is parallel to the x-axis or the y-axis, then
 * the contour line will be horizontal and vertical wrt the screen orientation. However, if the body
 * heading is at some other angle, then the contour lines will be tilted, in order to visually
 * represent that heading. If specified, a set number of fixed points, that are within the view box
 * in its current state, will also be build.
 *
 * Created by Thanos Mavroidis on 26/11/2017.
 */
public interface ViewBoxFeatures {

    /**
     * Builds the contour lines and, if specified, fixed points that can be drawn to represent a body
     * in the input state.
     *
     * @param state the state of the body which is to be represented
     */
    void buildFeatures(State state);

    /**
     * Returns the number of contour lines to be drawn.
     *
     * @return the number of contour lines to be drawn
     */
    int numberOfSetLines();

    /**
     * Returns all possible contour lines that can be drawn. The actual contour lines that can be
     * drawn consist of the first {@link #numberOfSetLines()} lines in this array.
     *
     * @return all possible contour lines that can be drawn. The actual contour lines that can be
     * drawn consist of the first {@link #numberOfSetLines()} lines in this array.
     */
    Line[] allLines();

    /**
     * Returns the number of fixed points set or zero if none are set or none are within the view l
     * ayout out by the contour lines in the current state.
     *
     * @return the number of fixed points set or zero if none are set or none are within the view l
     * ayout out by the contour lines in the current state
     */
    int numberOfSetFixedPoints();

    /**
     * Returns all the possible fixed points or null if none are set. The actual fixed points that
     * can be drawn consist of the first {@link #numberOfSetFixedPoints()} points in this array.
     *
     * @return all the possible fixed points or null if none are set. The actual fixed points that
     * can be drawn consist of the first {@link #numberOfSetFixedPoints()} points in this array.
     */
    Point[] allFixedPoints();
}
