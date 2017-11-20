package uk.co.akm.test.sim.boatinpond.graph;

/**
 * Created by Thanos Mavroidis on 17/11/2017.
 */
public final class Point implements Transformable {
    double x;
    double y;

    Point() {
        this(Double.NaN, Double.NaN);
    }

    private Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    boolean isNull() {
        return (Double.isNaN(x) || Double.isNaN(y));
    }

    boolean isNotNull() {
        return !(isNull());
    }

    void setNull() {
        x = Double.NaN;
        y = Double.NaN;
    }

    void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void translate(double dx, double dy) {
        if (isNotNull()) {
            this.x += dx;
            this.y += dy;
        }
    }

    public void rotate(double a) {
        if (isNotNull()) {
            final double cosa = Math.cos(a);
            final double sina = Math.sin(a);

            final double xr = x*cosa - y*sina;
            final double yr = x*sina + y*cosa;

            this.x = xr;
            this.y = yr;
        }
    }

    @Override
    public String toString() {
        return ("(" + x + ", " + y + ")");
    }
}