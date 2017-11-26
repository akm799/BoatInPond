package uk.co.akm.test.sim.boatinpond.graph;


/**
 * Created by Thanos Mavroidis on 17/11/2017.
 */
public final class Pixel {
    public int x;
    public int y;

    void fromPoint(Point p, double boxHorizontalSide, double boxVerticalSide, int screenWidth, int screenHeight) {
        final double halfHorizontalSide = boxHorizontalSide/2; //TODO Move to constants for optimization.
        final double fx = screenWidth/boxHorizontalSide; //TODO Move to constants for optimization.
        x = (int)Math.floor(fx*(p.x + halfHorizontalSide));

        final double halfVerticalSide = boxVerticalSide/2; //TODO Move to constants for optimization.
        final double fy = screenHeight/boxVerticalSide; //TODO Move to constants for optimization.
        y = screenHeight - (int)Math.floor(fy*(p.y + halfVerticalSide));
    }
}
