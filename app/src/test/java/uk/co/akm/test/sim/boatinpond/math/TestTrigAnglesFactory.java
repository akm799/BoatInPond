package uk.co.akm.test.sim.boatinpond.math;

/**
 * Created by Thanos Mavroidis on 06/12/2017.
 */
public class TestTrigAnglesFactory {
    private static final double ACCURACY = 0.0000000000000003;

    public static TrigValues instance(final double cosa, final double sina) {
        checkArgs(cosa, sina);

        return new TrigValues() {
            @Override
            public double cos() {
                return cosa;
            }

            @Override
            public double sin() {
                return sina;
            }
        };
    }

    private static void checkArgs(double cosa, double sina) {
        checkArg(cosa, "cosine");
        checkArg(sina, "sine");

        final double smSq = sina*sina + cosa*cosa;
        if (Math.abs(1 - smSq) > ACCURACY) {
            throw new IllegalArgumentException("Illegal sine and cosine combination.");
        }
    }

    private static void checkArg(double cs, String name) {
        if (-1 > cs || cs > 1) {
            throw new IllegalArgumentException("Illegal " + name + " value: " + cs);
        }
    }

    private TestTrigAnglesFactory() {}
}
