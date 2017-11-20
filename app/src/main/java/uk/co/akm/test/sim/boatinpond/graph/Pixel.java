package uk.co.akm.test.sim.boatinpond.graph;


/**
 * Created by Thanos Mavroidis on 17/11/2017.
 */
public final class Pixel {
    public int x;
    public int y;

    void fromPoint(Point p, double boxSide, int screenSide) {
        final double hb = boxSide/2; //TODO Move to constants for optimization.
        final double f = screenSide/boxSide; //TODO Move to constants for optimization.
        x = (int)Math.floor(f*(p.x + hb));
        y = screenSide - (int)Math.floor(f*(p.y + hb));
    }
}
