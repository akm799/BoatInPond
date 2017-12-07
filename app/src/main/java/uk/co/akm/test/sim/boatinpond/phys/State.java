package uk.co.akm.test.sim.boatinpond.phys;

import uk.co.akm.test.sim.boatinpond.math.TrigValues;

/**
 * Reflects the angular and positional state of a body.
 *
 * Created by Thanos Mavroidis on 11/11/2017.
 */
public interface State {

    /**
     * @return the current angular acceleration for the heading angle
     */
    double aHdn();

    /**
     * @return the current angular acceleration for the azimuth angle
     */
    double aAzm();

    /**
     * @return the current angular acceleration for the roll angle
     */
    double aRll();

    /**
     * @return the current angular velocity for the heading angle
     */
    double omgHdn();

    /**
     * @return the current angular velocity for the azimuth angle
     */
    double omgAmz();

    /**
     * @return the current angular velocity for the roll angle
     */
    double omgRll();

    /**
     * @return the current heading angle
     */
    double hdn();

    /**
     * @return the current heading angle converted to a (proper) range from -pi to pi
     */
    double hdnP();

    /**
     * @return the cosine and sine values of the current heading angle
     */
    TrigValues hdnTrig();

    /**
     * @return the current azimuth angle
     */
    double azm();

    /**
     * @return the current azimuth angle converted to a (proper) range from -pi to pi
     */
    double azmP();

    /**
     * @return the cosine and sine values of the current azimuth angle
     */
    TrigValues azmTrig();

    /**
     * @return the current roll angle
     */
    double rll();

    /**
     * @return the current roll angle converted to a (proper) range from -pi to pi
     */
    double rllP();

    /**
     * @return the cosine and sine values of the current roll angle
     */
    TrigValues rllTrig();

    /**
     * @return the current acceleration component along the x-axis
     */
    double ax();

    /**
     * @return the current acceleration component along the y-axis
     */
    double ay();

    /**
     * @return the current acceleration component along the z-axis
     */
    double az();

    /**
     * @return the current velocity component along the x-axis
     */
    double vx();

    /**
     * @return the current velocity component along the y-axis
     */
    double vy();

    /**
     * @return the current velocity component along the z-axis
     */
    double vz();

    /**
     * @return the current velocity magnitude
     */
    double v();

    /**
     * @return the current x-coordinate
     */
    double x();

    /**
     * @return the current y-coordinate
     */
    double y();

    /**
     * @return the current z-coordinate
     */
    double z();

    /**
     * @return the current time
     */
    double t();
}
