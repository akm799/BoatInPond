package uk.co.akm.test.sim.boatinpond.math.root;

public final class NewtonRaphsonRootFinder {
    private final int nMax;

    public NewtonRaphsonRootFinder() {
        this(1000);
    }

    public NewtonRaphsonRootFinder(int nMax) {
        if (nMax <= 0) {
            throw new IllegalArgumentException("Illegal nMax=" + nMax + " argument which is not greater than zero.");
        }

        this.nMax = nMax;
    }

    public double findRoot(FunctionAndDerivative function, double x0, double improvementThreshold) {
        checkArguments(function, improvementThreshold);

        int n = 0;
        double xn = x0;
        boolean notConverged = true;
        while (notConverged && n < nMax) {
            final double fpxn = function.fp(xn);
            if (fpxn == 0) {
                throw new IllegalStateException("First derivative evaluated to zero at x=" + xn);
            }

            final double xnp1 = xn - function.f(xn)/fpxn;
            notConverged = (Math.abs(xnp1 - xn) >= improvementThreshold);
            xn = xnp1;
            n++;
        }

        return xn;
    }

    private void checkArguments(FunctionAndDerivative function, double improvementThreshold) {
        if (function == null) {
            throw new IllegalArgumentException("Null input function.");
        }

        if (improvementThreshold <= 0) {
            throw new IllegalArgumentException("improvementThreshold=" + improvementThreshold + " must be positive.");
        }
    }
}
