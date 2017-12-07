package uk.co.akm.test.sim.boatinpond.phys;

import uk.co.akm.test.sim.boatinpond.math.Angles;
import uk.co.akm.test.sim.boatinpond.math.TrigAngle;
import uk.co.akm.test.sim.boatinpond.math.TrigValues;

/**
 * Created by Thanos Mavroidis on 01/12/2017.
 */
final class StateData implements State {
    private double aHdn; // angular acceleration for the heading  angle
    private double aAzm; // angular acceleration for the azimuth angle
    private double aRll; // angular acceleration for the roll angle

    private double omgHdn; // angular velocity for the heading angle
    private double omgAzm; // angular velocity for the azimuth angle
    private double omgRll; // angular velocity for the roll angle

    private double hdn; // heading angle
    private double azm; // azimuth angle
    private double rll; // roll angle

    private double ax; // x-axis acceleration component
    private double ay; // y-axis acceleration component
    private double az; // z-axis acceleration component

    private double vx; // x-axis velocity
    private double vy; // y-axis velocity
    private double vz; // z-axis velocity

    private double x; // x-coordinate
    private double y; // y-coordinate
    private double z; // z-coordinate

    private double t; // time

    private final TrigAngle hdnTrig = new TrigAngle(); // the cosine and sine values of the heading angle
    private final TrigAngle azmTrig = new TrigAngle(); // the cosine and sine values of the azimuth angle
    private final TrigAngle rllTrig = new TrigAngle(); // the cosine and sine values of the roll angle

    StateData() {}

    void set(State state) {
        aHdn = state.aHdn();
        aAzm = state.aAzm();
        aRll = state.aRll();

        omgHdn = state.omgHdn();
        omgAzm = state.omgAmz();
        omgRll = state.omgRll();

        hdn = state.hdn();
        azm = state.azm();
        rll = state.rll();

        hdnTrig.copy(state.hdnTrig());
        azmTrig.copy(state.azmTrig());
        rllTrig.copy(state.rllTrig());

        ax = state.ax();
        ay = state.ay();
        az = state.az();

        vx = state.vx();
        vy = state.vy();
        vz = state.vz();

        x = state.x();
        y = state.y();
        z = state.z();
    }

    @Override
    public double aHdn() {
        return aHdn;
    }

    @Override
    public double aAzm() {
        return aAzm;
    }

    @Override
    public double aRll() {
        return aRll;
    }

    @Override
    public double omgHdn() {
        return omgHdn;
    }

    @Override
    public double omgAmz() {
        return omgAzm;
    }

    @Override
    public double omgRll() {
        return omgRll;
    }

    @Override
    public double hdn() {
        return hdn;
    }

    @Override
    public double hdnP() {
        return Angles.toProperAngle(hdn);
    }

    @Override
    public TrigValues hdnTrig() {
        return hdnTrig;
    }

    @Override
    public double azm() {
        return azm;
    }

    @Override
    public double azmP() {
        return Angles.toProperAngle(azm);
    }

    @Override
    public TrigValues azmTrig() {
        return azmTrig;
    }

    @Override
    public double rll() {
        return rll;
    }

    @Override
    public double rllP() {
        return Angles.toProperAngle(rll);
    }

    @Override
    public TrigValues rllTrig() {
        return rllTrig;
    }

    @Override
    public double ax() {
        return ax;
    }

    @Override
    public double ay() {
        return ay;
    }

    @Override
    public double az() {
        return az;
    }

    @Override
    public double vx() {
        return vx;
    }

    @Override
    public double vy() {
        return vy;
    }

    @Override
    public double vz() {
        return vz;
    }

    @Override
    public double v() {
        return Math.sqrt(vx*vx + vy*vy + vz*vz);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double t() {
        return t;
    }
}
