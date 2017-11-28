package uk.co.akm.test.sim.boatinpond.graph;

import uk.co.akm.test.sim.boatinpond.env.Environment;

/**
 * Created by Thanos Mavroidis on 28/11/2017.
 */
public final class MockEnvironment implements Environment {
    private final Point[] fixedPoints;

    MockEnvironment(double... args) {
        if (args == null || args.length%2 != 0) {
            throw new IllegalArgumentException("Input coordinates must be an array with an even number of elements.");
        }

        fixedPoints = new Point[args.length/2];
        for (int i=0 ; i<fixedPoints.length ; i++) {
            final int j = 2*i;
            fixedPoints[i] = new Point(args[j], args[j + 1]);
        }
    }

    @Override
    public int getNumberOfFixedPoints() {
        return fixedPoints.length;
    }

    @Override
    public Point[] getFixedPoints() {
        return fixedPoints;
    }
}
