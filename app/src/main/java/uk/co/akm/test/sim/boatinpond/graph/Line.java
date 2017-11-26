package uk.co.akm.test.sim.boatinpond.graph;


/**
 * Created by Thanos Mavroidis on 17/11/2017.
 */
public final class Line implements Transformable {
    private double slope;
    private double yIntercept;
    public final Point start;
    public final Point end;

    private double xMax;
    private double xMin;
    private double yMax;
    private double yMin;

    public final Pixel startPixel = new Pixel();
    public final Pixel endPixel = new Pixel();

    Line() {
        this.start = new Point();
        this.end = new Point();
    }

    Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    boolean isNull() {
        return (start.isNull() || end.isNull());
    }

    boolean isNotNull() {
        return !(isNull());
    }

    void setNull() {
        start.setNull();
        end.setNull();
        slope = Double.NaN;
        yIntercept = Double.NaN;
    }

    void set(double xStart, double yStart, double xEnd, double yEnd) {
        start.set(xStart, yStart);
        end.set(xEnd, yEnd);

        resetParameters();
    }

    void resetParameters() {
        setMinMax();
        setEquationParameters();
    }

    private void setMinMax() {
        xMin = Math.min(start.x, end.x);
        xMax = Math.max(start.x, end.x);
        yMin = Math.min(start.y, end.y);
        yMax = Math.max(start.y, end.y);
    }

    private boolean hasNoEquationParameters() {
        return (Double.isNaN(slope) || Double.isNaN(yIntercept));
    }

    private void setEquationParameters() {
        slope = (start.x == end.x) ? Double.NaN : ((end.y - start.y) / (end.x - start.x));
        yIntercept = Double.isNaN(slope) ? Double.NaN : start.y - slope*start.x;
    }

    public void translate(double dx, double dy) {
        start.translate(dx, dy);
        end.translate(dx, dy);

        resetParameters();
    }

    /**
     * Rotates the start and end points of the line.
     * @param a the angle of rotation
     */
    public void rotate(double a) {
        start.rotate(a);
        end.rotate(a);

        resetParameters();
    }

    void verticalIntercept(double x, Point intercept) {
        if (hasNoEquationParameters()) {
            intercept.setNull();
        } else {
            final double y = y(x);
            if (yMin <= y && y <= yMax) {
                intercept.set(x, y);
            } else {
                intercept.setNull();
            }
        }
    }

    private double y(double x) {
        return hasNoEquationParameters() ? Double.NaN : slope*x + yIntercept;
    }

    void horizontalIntercept(double y, Point intercept) {
        if (hasNoEquationParameters()) {
            intercept.set(start.x, y);
        } else if (slope == 0) {
            intercept.setNull();
        } else {
            final double x = x(y);
            if (xMin <= x && x <= xMax) {
                intercept.set(x, y);
            } else {
                intercept.setNull();
            }
        }
    }

    private double x(double y) {
        return hasNoEquationParameters() ? Double.NaN : (y - yIntercept)/slope;
    }

    void setPixels(double boxHorizontalSide, double boxVerticalSide, int screenWidth, int screenHeight) {
        startPixel.fromPoint(start, boxHorizontalSide, boxVerticalSide, screenWidth, screenHeight);
        endPixel.fromPoint(end, boxHorizontalSide, boxVerticalSide, screenWidth, screenHeight);
    }

    @Override
    public String toString() {
        return (start.toString() + " -> " + end.toString());
    }
}
