package uk.co.akm.test.sim.boatinpond.math;

/**
 * Created by Thanos Mavroidis on 29/11/2017.
 */
public class MathConstants {
    public static final double ROOT_TWO = Math.sqrt(2);
    public static final double PI_OVER_TWO = Math.PI/2;

    public static final TrigValues MINUS_PI_OVER_TWO = new TrigValues() {
        @Override
        public double cos() {
            return 0;
        }

        @Override
        public double sin() {
            return -1;
        }
    };

    private MathConstants() {}
}
