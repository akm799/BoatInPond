package uk.co.akm.test.sim.boatinpond.boat.performance;

public final class Distribution {
    private int n = 0;
    private double sum = 0;
    private double sumSq = 0;

    public void add(double x) {
        n++;
        sum += x;
        sumSq += x*x;
    }

    public double getAverage() {
        if (n == 0) {
            return 0;
        }

        return sum / n;
    }

    public double getStandardDeviation() {
        if (n == 0) {
            return 0;
        }

        final double mean = getAverage();
        // Take the absolute value because for near-constant distributions, numerical errors can result in a negative number.
        final double standardDeviationSq = Math.abs( (sumSq - 2*mean*sum + n*mean*mean)/n );

        return Math.sqrt(standardDeviationSq);
    }
}
