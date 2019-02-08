package uk.co.akm.test.sim.boatinpond.phys;

import uk.co.akm.test.sim.boatinpond.math.Angles;
import uk.co.akm.test.sim.boatinpond.math.TrigAngle;
import uk.co.akm.test.sim.boatinpond.math.TrigValues;

/**
 * Abstraction of physical body with two evolving states:
 *
 *  1) Position and velocity in a 3D right-handed coordinate system
 *  2) Angles and angular velocity for 3 angles: heading, azimuth and roll.
 *
 *  The body is considered to have an internal axis running through it.
 *  This body-axis will, usually, be parallel to the body velocity vector
 *  and will, thus, define the body direction. The heading, azimuth and roll
 *  angles are defined as follows:
 *
 *  1) heading: the angle of the body-axis projection on the xy-plane with the x-axis (+ve for +ve y-axis projection values).
 *  2) azimuth: the angle of the body-axis with the xy-plane (+ve for +ve z-axis projection values).
 *  3) roll: the angle of the body when rotated around its body axis.
 *
 * Created by Thanos Mavroidis on 11/11/2017.
 */
public abstract class Body implements UpdatableState {
    protected double aHdn; // current angular acceleration for the heading  angle
    protected double aAzm; // current angular acceleration for the azimuth angle
    protected double aRll; // current angular acceleration for the roll angle

    private double omgHdn; // current angular velocity for the heading angle
    private double omgAzm; // current angular velocity for the azimuth angle
    private double omgRll; // current angular velocity for the roll angle

    private double hdn; // current heading angle
    private double azm; // current azimuth angle
    private double rll; // current roll angle

    protected double ax; // current x-axis acceleration component
    protected double ay; // current y-axis acceleration component
    protected double az; // current z-axis acceleration component

    private double vx; // current x-axis velocity
    private double vy; // current y-axis velocity
    private double vz; // current z-axis velocity

    private double x; // current x-coordinate
    private double y; // current y-coordinate
    private double z; // current z-coordinate

    private double t; // current time

    private final TrigAngle hdnTrig = new TrigAngle(); // the cosine and sine values of the current heading angle
    private final TrigAngle azmTrig = new TrigAngle(); // the cosine and sine values of the current azimuth angle
    private final TrigAngle rllTrig = new TrigAngle(); // the cosine and sine values of the current roll angle

    // The body state before the integration step, i.e. the starting point of the integration step.
    private final StateData startState = new StateData();

    /**
     * Creates a body in some initial state defined by the constructor arguments.
     *
     * @param omgHdn  the initial angular velocity for the heading angle
     * @param omgAzm  the initial angular velocity for the azimuth angle
     * @param omgRll  the initial angular velocity for the roll angle
     * @param hdn     the initial heading angle
     * @param azm     the initial azimuth angle
     * @param rll     the initial roll angle
     * @param vx0   the initial velocity along the x-axis
     * @param vy0   the initial velocity along the y-axis
     * @param vz0   the initial velocity along the z-axis
     * @param x0    the initial x-axis position
     * @param y0    the initial y-axis position
     * @param z0    the initial z-axis position
     */
    protected Body(double omgHdn, double omgAzm, double omgRll, double hdn, double azm, double rll, double vx0, double vy0, double vz0, double x0, double y0, double z0) {
        this.aHdn = 0;
        this.aAzm = 0;
        this.aRll = 0;
        this.omgHdn = omgHdn;
        this.omgAzm = omgAzm;
        this.omgRll = omgRll;
        this.hdn = hdn;
        this.azm = azm;
        this.rll = rll;
        this.ax = 0;
        this.ay = 0;
        this.az = 0;
        this.vx = vx0;
        this.vy = vy0;
        this.vz = vz0;
        this.x = x0;
        this.y = y0;
        this.z = z0;
        this.t = 0;

        hdnTrig.set(hdn);
        azmTrig.set(azm);
        rllTrig.set(rll);
    }

    /**
     * Updates the body state over a small time increment.
     *
     * @param dt the small time increment
     */
    public final void update(double dt) {
        startState.set(this); // Save the state before we start the integration step.
        initUpdate(startState, dt);

        updateAngularAcceleration(startState, dt);
        updateAngularVelocityAndAngles(dt);

        updateAcceleration(startState, dt);
        updateVelocityAndPosition(dt);

        t += dt;
    }

    /**
     * This method can be used for optimization purposes. Common variables, that are used repeatedly,
     * for the linear and angular acceleration updates, can be calculated if this method is overridden.
     *
     * @param start the body state before any angular or linear acceleration update (i.e. the
     *              starting point for this update)
     * @param dt the small time increment
     */
    protected void initUpdate(State start, double dt) {}

    /**
     * Implemented by the user to update angular acceleration data over a small time increment.
     * The angular velocity and angle information will be updated subsequently with a very
     * basic numerical integration. Please note that the angular acceleration is wrt to some
     * fixed axis in the body and not wrt to the coordinate system. In many cases, the velocity
     * vector can be used as a reference wrt which we will define the axes of angular velocities.
     *
     * @param start the body state before any angular or linear acceleration update (i.e. the
     *              starting point for this update)
     * @param dt the small time increment
     */
    protected abstract void updateAngularAcceleration(State start, double dt);

    /**
     * Implemented by the user to update acceleration data over a small time increment.
     * The velocity and position information will be updated subsequently with a very
     * basic numerical integration.
     *
     * @param start the body state before any angular or linear acceleration update (i.e. the
     *              starting point for this update)
     * @param dt the small time increment
     */
    protected abstract void updateAcceleration(State start, double dt);

    // Updates the angular velocity and angles of the body assuming a constant angular acceleration exerted over the (small) input time increment dt.
    private void updateAngularVelocityAndAngles(double dt) {
        omgHdn += aHdn * dt;
        omgAzm += aAzm * dt;
        omgRll += aRll * dt;

        hdn += omgHdn * dt;
        azm += omgAzm * dt;
        rll += omgRll * dt;

        if (omgHdn != 0.0) {
            hdnTrig.set(hdn);
        }

        if (omgAzm != 0.0) {
            azmTrig.set(azm);
        }

        if (omgRll != 0.0) {
            rllTrig.set(rll);
        }
    }

    // Updates the velocity and position of the body assuming a constant acceleration exerted over the (small) input time increment dt.
    private void updateVelocityAndPosition(double dt) {
        vx += ax*dt;
        vy += ay*dt;
        vz += az*dt;

        x += vx*dt;
        y += vy*dt;
        z += vz*dt;
    }

    @Override
    public final double aHdn() {
        return aHdn;
    }

    @Override
    public final double aAzm() {
        return aAzm;
    }

    @Override
    public final double aRll() {
        return aRll;
    }

    @Override
    public final double omgHdn() {
        return omgHdn;
    }

    @Override
    public final double omgAmz() {
        return omgAzm;
    }

    @Override
    public final double omgRll() {
        return omgRll;
    }

    @Override
    public final double hdn() {
        return hdn;
    }

    @Override
    public final double hdnP() {
        return Angles.toProperAngle(hdn);
    }

    @Override
    public TrigValues hdnTrig() {
        return hdnTrig;
    }

    @Override
    public final double azm() {
        return azm;
    }

    @Override
    public final double azmP() {
        return Angles.toProperAngle(azm);
    }

    @Override
    public TrigValues azmTrig() {
        return azmTrig;
    }

    @Override
    public final double rll() {
        return rll;
    }

    @Override
    public final double rllP() {
        return Angles.toProperAngle(rll);
    }

    @Override
    public TrigValues rllTrig() {
        return rllTrig;
    }

    @Override
    public final double ax() {
        return ax;
    }

    @Override
    public final double ay() {
        return ay;
    }

    @Override
    public final double az() {
        return az;
    }

    @Override
    public final double vx() {
        return vx;
    }

    @Override
    public final double vy() {
        return vy;
    }

    @Override
    public final double vz() {
        return vz;
    }

    @Override
    public final double v() {
        return Math.sqrt(vx*vx + vy*vy + vz*vz);
    }

    @Override
    public final double x() {
        return x;
    }

    @Override
    public final double y() {
        return y;
    }

    @Override
    public final double z() {
        return z;
    }

    @Override
    public final double t() {
        return t;
    }
}
