package uk.co.akm.test.sim.boatinpond.graph;


import java.util.Arrays;

/**
 * Created by Thanos Mavroidis on 17/11/2017.
 */
public final class ViewBox implements ViewBoxLines {
    private final double horizontalSide;
    private final double verticalSide;
    private final double lineSpacing;
    private final int screenWidth;
    private final int screenHeight;

    private final Point bottomLeft = new Point();
    private final Point bottomRight = new Point();
    private final Point topRight = new Point();
    private final Point topLeft = new Point();
    private final Point[] points = {bottomLeft, bottomRight, topRight, topLeft};

    private final Line left = new Line(bottomLeft, topLeft);
    private final Line right = new Line(bottomRight, topRight);
    private final Line bottom = new Line(bottomLeft, bottomRight);
    private final Line top = new Line(topLeft, topRight);
    private final Line[] sides = {left, right, bottom, top};

    private final int maxDiv;

    private double xMax;
    private double xMin;
    private double yMax;
    private double yMin;

    private int nVertical;
    private int nHorizontal;
    private final double[] vertical;
    private final double[] horizontal;

    private int nLines;
    private final Line[] lines;

    public ViewBox(double horizontalSide, double lineSpacing, int screenWidth, int screenHeight) {
        checkScreenDimensions(screenWidth, screenHeight);

        this.horizontalSide = horizontalSide;
        this.lineSpacing = lineSpacing;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.verticalSide = (screenHeight*horizontalSide)/screenWidth;

        maxDiv = (int)(Math.sqrt(horizontalSide*horizontalSide + verticalSide*verticalSide)/lineSpacing) + 1;
        vertical = new double[maxDiv];
        horizontal = new double[maxDiv];
        lines = initLines(maxDiv);
    }

    private void checkScreenDimensions(int screenWidth, int screenHeight) {
        if (screenWidth <= 0 || screenHeight <= 0) {
            throw new IllegalArgumentException("Invalid sceen dimensions: (" + screenWidth + ", " + screenHeight + ")");
        }
    }

    private Line[] initLines(int maxDiv) {
        final Line[] lines = new Line[2*maxDiv]; // Double (1 for vertical + 1 for horizontal) the number.
        for (int i=0 ; i<lines.length ; i++) {
            lines[i] = new Line();
        }

        return lines;
    }

    @Override
    public int numberOfSetLines() {
        return nLines;
    }

    @Override
    public Line[] allLines() {
        return lines;
    }

    @Override
    public int buildLines(double x, double y, double a) {
        placeAtOrigin();
        rotateAndTranslateBox(a, x, y);

        setDivs();
        findIntercepts();

        translateAndRotateLines(-x, -y, -a);
        setLinePixels();

        return nLines;
    }

    private void placeAtOrigin() {
        final double halfHorizontalSide = horizontalSide/2;
        final double halfVerticalSide = verticalSide/2;

        bottomLeft.set(-halfHorizontalSide, -halfVerticalSide);
        bottomRight.set(halfHorizontalSide, -halfVerticalSide);
        topRight.set(halfHorizontalSide, halfVerticalSide);
        topLeft.set(-halfHorizontalSide, halfVerticalSide);
    }

    private void rotateAndTranslateBox(double a, double dx, double dy) {
        rotateBox(a);
        translateBox(dx, dy);
    }

    private void translateBox(double dx, double dy) {
        for (Point p : points) {
            p.translate(dx, dy);
        }

        for (Line side : sides) {
            side.resetParameters();
        }
    }

    private void rotateBox(double a) {
        for (Point p : points) {
            p.rotate(a);
        }

        for (Line side : sides) {
            side.resetParameters();
        }
    }

    private void setDivs() {
        setMinAndMax();
        findDivs();
    }

    private void setMinAndMax() {
        setMin();
        setMax();
    }

    private void setMin() {
        final double xMinTop = Math.min(topLeft.x, topRight.x);
        final double xMinBottom = Math.min(bottomLeft.x, bottomRight.x);
        xMin = Math.min(xMinTop, xMinBottom);

        final double yMinTop = Math.min(topLeft.y, topRight.y);
        final double yMinBottom = Math.min(bottomLeft.y, bottomRight.y);
        yMin = Math.min(yMinTop, yMinBottom);
    }

    private void setMax() {
        final double xMaxTop = Math.max(topLeft.x, topRight.x);
        final double xMaxBottom = Math.max(bottomLeft.x, bottomRight.x);
        xMax = Math.max(xMaxTop, xMaxBottom);

        final double yMaxTop = Math.max(topLeft.y, topRight.y);
        final double yMaxBottom = Math.max(bottomLeft.y, bottomRight.y);
        yMax = Math.max(yMaxTop, yMaxBottom);
    }

    private void findDivs() {
        findVerticals();
        findHorizontals();
        nLines = nVertical + nHorizontal;
    }

    private void findVerticals() {
        Arrays.fill(vertical, Double.NaN);
        final double xStart = lineSpacing*Math.floor(xMin/lineSpacing);

        int i = 0;
        double x = xStart + lineSpacing;
        while (x < xMax) {
            if (x < xMax) {
                vertical[i++] = x;
                x += lineSpacing;
            }
        }

        nVertical = i;
    }

    private void findHorizontals() {
        Arrays.fill(horizontal, Double.NaN);
        final double yStart = lineSpacing*Math.floor(yMin/lineSpacing);

        int i = 0;
        double y = yStart + lineSpacing;
        while (y < yMax) {
            if (y < yMax) {
                horizontal[i++] = y;
                y += lineSpacing;
            }
        }

        nHorizontal = i;
    }

    private void findIntercepts() {
        setLinesToNull();
        findVerticalIntercepts();
        findHorizontalIntercepts();
    }

    private void setLinesToNull() {
        for (Line line : lines) {
            line.setNull();
        }
    }

    private void findVerticalIntercepts() {
        for (int i=0 ; i<nVertical ; i++) {
            final Line line = getFirstNullLine();
            if (line != null) {
                findVerticalIntercept(vertical[i], line);
            }
        }
    }

    private void findVerticalIntercept(double x, Line line) {
        Point intercept = line.start;
        for (int i=0 ; i<sides.length ; i++) {
            sides[i].verticalIntercept(x, intercept);
            if (intercept.isNotNull()) {
                if (line.start.isNotNull() && line.end.isNotNull()) {
                    return;
                } else if (line.end.isNull()) {
                    intercept = line.end;
                }
            }
        }
    }

    private void findHorizontalIntercepts() {
        for (int i=0 ; i<nHorizontal ; i++) {
            final Line line = getFirstNullLine();
            if (line != null) {
                findHorizontalIntercept(horizontal[i], line);
            }
        }
    }

    private void findHorizontalIntercept(double y, Line line) {
        Point intercept = line.start;
        for (int i=0 ; i<sides.length ; i++) {
            sides[i].horizontalIntercept(y, intercept);
            if (intercept.isNotNull()) {
                if (line.start.isNotNull() && line.end.isNotNull()) {
                    return;
                } else if (line.end.isNull()) {
                    intercept = line.end;
                }
            }
        }
    }

    private Line getFirstNullLine() {
        for (Line line : lines) {
            if (line.isNull()) {
                return line;
            }
        }

        return null;
    }

    private void translateAndRotateLines(double dx, double dy, double a) {
        translateLines(dx, dy);
        rotateLines(a);
    }

    private void rotateLines(double a) {
        for (Line line : lines) {
            line.rotate(a);
        }
    }

    private void translateLines(double dx, double dy) {
        for (Line line : lines) {
            line.translate(dx, dy);
        }
    }

    private void setLinePixels() {
        for (Line line : lines) {
            if (line.isNotNull()) {
                line.setPixels(horizontalSide, verticalSide, screenWidth, screenHeight);
            }
        }
    }

    @Override
    public String toString() {
        return ("[Bottom-Left: " + bottomLeft + "] [Bottom-Right: " + bottomRight + "] [Top-Right: " + topRight + "] [Top-Left: " + topLeft + "]");
    }
}
