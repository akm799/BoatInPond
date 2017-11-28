package uk.co.akm.test.sim.boatinpond.graph;

/**
 * Interface that builds contour lines for a state at coordinates (x,y) with a heading represented by
 * an angle a. The contour lines are equally spaced horizontal and vertical lines along the x-y plane.
 * The lines are viewed from above with the body represented always placed at the centre of the view
 * box. The horizontal and vertical contour lines will be viewed depending on the coordinates and
 * heading of the body. For example if the body heading is parallel to the x-axis or the y-axis, then
 * the contour line will be horizontal and vertical wrt the screen orientation. However, if the body
 * heading is at some other angle, then the contour lines will be tilted, in order to visually
 * represent that heading.
 *
 * Created by Thanos Mavroidis on 26/11/2017.
 */
public interface ViewBoxLines {

    /**
     * Builds the contour lines that can be drawn to represent a body at coordinates (x,y) with a
     * heading represented by an angle a.
     *
     * @param x the body x-coordinate
     * @param y the body y-coordinate
     * @param a the body heading in radians
     * @return the number of contour lines to be drawn
     */
    int buildLines(double x, double y, double a);

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

    int numberOfSetFixedPoints();

    Point[] allFixedPoints();
}
