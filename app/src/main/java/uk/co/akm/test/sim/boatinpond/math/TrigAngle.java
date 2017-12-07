package uk.co.akm.test.sim.boatinpond.math;


/**
 * Created by Thanos Mavroidis on 03/12/2017.
 */
public final class TrigAngle implements TrigValues {
    private double cosa;
    private double sina;

    public TrigAngle() {
        cosa = Double.NaN;
        sina = Double.NaN;
    }

    public void set(double a) {
        cosa = Math.cos(a);
        sina = Math.sin(a);
    }

    public TrigAngle copy(TrigValues other) {
        cosa = other.cos();
        sina = other.sin();

        return this;
    }

    public TrigAngle negative() {
        sina = -sina;

        return this;
    }

    public TrigAngle add(TrigValues b) {
        final double cosAplusB = cosa*b.cos() - sina*b.sin();
        final double sinAplusB = sina*b.cos() + cosa*b.sin();

        cosa = cosAplusB;
        sina = sinAplusB;

        return this;
    }

    @Override
    public double cos() {
        return cosa;
    }

    @Override
    public double sin() {
        return sina;
    }

    @Override
    public String toString() {
        return ("(" + cosa + ", " + sina + ")");
    }
}
