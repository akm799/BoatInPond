package uk.co.akm.test.sim.boatinpond.boat.impl.linear;


/**
 * This class calculates the boat motion constants based on some desired performance characteristics.
 *
 * Please note that the mass of the boat as well as its moment of inertia are both assumed to be 1.
 *
 * Created by Thanos Mavroidis on 16/12/2017.
 */
public final class BoatConstantsApprox implements LinearBoatConstants {
    // Resistance coefficient across the axis of the boat heading.
    private final double kLon;
    // Resistance coefficient perpendicular to the axis of the boat heading.
    private final double kLat;

    // Rudder deflection coefficient.
    private final double kRud;
    // Boat angular motion resistance coefficient.
    private final double kAng;

    /**
     * Constructor that takes some desired performance characteristics to derive the boat motion
     * constants. The desired performance characteristics cover 2 performance scenarios:
     *
     * 1) The boat achieves an initial speed v0 with an initial push and then its speed is left to
     *    decay to a final value. For this scenario 3 values are supplied:
     *      i)   the initial speed v0
     *      ii)  the final speed as a fraction of the initial speed v0
     *      iii) the time take for the initial speed to decay to the final speed
     *
     * 2) The boat is travelling at a constant speed v0 at a straight line when at t=0 full rudder
     *    deflection is applied, initiating a turn. The boat speed is assumed to be constant during
     *    that turn (by some means of propulsion cancelling the water resistance). The turn continues
     *    until some specified turn rate is reached. For this scenario 3 values are supplied:
     *      i)   the maximum turn rate that could be possibly achieved at this speed (v0) with the
     *           maximum rudder deflection
     *      ii)  the turn rate that is actually achieved as a fraction of the maximum possible turn
     *           rate
     *      iii) the time taken to acheive the specified turn rate
     *
     *
     * Please note that the mass of the boat as well as its moment of inertia are both assumed to be 1.
     *
     * @param v0 initial boat speed
     * @param frVFinal the fraction of the final boat speed over the initial boat speed
     * @param tv the time taken for the initial boat speed to fall to the final boat speed (due to
     *           water resistance)
     * @param kLatOverKLon the ratio of the boat resistance perpendicular to its longitudinal axis
     *                     to the boat resistance along its longitudinal axis
     * @param omgMax the maximum turn rate that can be reached at a speed #v0 (the initial boat speed
     *               supplied)
     * @param frOmgFinal the fraction of the turn rate achieved, after some time at full rudder, over
     *                   the maximum possible turn rate #omgMax.
     * @param tOmg the time taken to reach the reach the turn rate, derived from #frOmgFinal, starting
     *             from a zero turn rate and applying full maximum rudder at a speed #v0
     */
    public BoatConstantsApprox(double v0, double frVFinal, double tv, double kLatOverKLon, double omgMax, double frOmgFinal, double tOmg) {
        checkArguments(v0, frVFinal, tv, kLatOverKLon, omgMax, frOmgFinal, tOmg);

        kLon = -Math.log(frVFinal)/tv;
        kLat = kLatOverKLon*kLon;

        kAng = -Math.log(1 - frOmgFinal)/tOmg;
        kRud = kAng*omgMax/v0;

        checkResults();
    }

    private void checkArguments(double v0, double frVFinal, double tv, double kLatOverKLon, double omgMax, double frOmgFinal, double tOmg) {
        ensurePositive(v0, "initial speed v0");
        checkFractionValue(frVFinal, "ratio frVFinal of the final speed to the initial speed");
        ensurePositive(tv, "speed decay time tv");
        ensurePositive(kLatOverKLon, "lateral to longitudinal resistance ratio kLatOverKLon");

        ensurePositive(omgMax, "maximum possible turn rate omgMax");
        checkFractionValue(frOmgFinal, "ratio frOmgFinal of the final turn rate to the maximul possible turn rate");
        ensurePositive(tv, "time tOmg taken to reach the turn rate (derived by the frOmgFinal fraction)");
    }

    private void checkResults() {
        ensurePositive(kLon, "longitudinal resistance coefficient");
        ensurePositive(kLat, "lateral resistance coefficient");
        ensurePositive(kAng, "angular motion resistance coefficient");
        ensurePositive(kRud, "rudder deflection coefficient");
    }

    private void ensurePositive(double d, String name) {
        if (d <= 0) {
            throw new IllegalArgumentException("The " + name + " (" + d + ") cannot be zero or negative.");
        }
    }

    private void checkFractionValue(double d, String name) {
        if (d <= 0 || d >= 1) {
            throw new IllegalArgumentException("The " + name + " (" + d + ") must be greater than zero and less than one.");
        }
    }

    @Override
    public double getkLon() {
        return kLon;
    }

    @Override
    public double getkLat() {
        return kLat;
    }

    @Override
    public double getKLonReverse() {
        return 10*kLon;
    }

    @Override
    public double getkRud() {
        return kRud;
    }

    @Override
    public double getkAng() {
        return kAng;
    }

    @Override
    public double getLength() {
        return 4;
    }

    @Override
    public double getCentreOfMassFromStern() {
        return 1.5;
    }

    @Override
    public double getRudderAreaFraction() {
        return 0.05;
    }

    @Override
    public double getMaximumRudderAngle() {
        return Math.PI/4;
    }

    @Override
    public double timeToMaximumRudderAnge() {
        return 2;
    }
}