package uk.co.akm.test.sim.boatinpond.boat.impl.foil;

import uk.co.akm.test.sim.boatinpond.math.MathConstants;

/**
 * Drag coefficient as a function of the angle of attack in radians. The angle range if from
 * zero (no deflection) to pi/2 (full deflection) radians. The behaviour of the drag coefficient
 * function modelled here follows approximately the CDMS curve in the Lift/Drag Coefficient
 * figure in http://www.japanham.com/en/service/mariner/
 *
 * More specifically the function is modelled into 3 parts:
 *
 * 1) Parabolic ascent from a minimum value at zero degrees
 * 2) Linear ascent after some transition angle
 * 3) Parabolic ascent after some second transition angle such that the peak is reached at pi/2 radians
 *
 * The initial parabolic ascend a*x^2 + c coefficients are derived from https://www.researchgate.net/figure/Lift-and-drag-coefficients-for-the-rudder-and-the-keel-The-area-of-the-rudder-A-R-is_fig8_308674535
 * The other 2 parameters are the two transition points: the angle where the linear ascend starts and
 * the angle where it ends. The coefficients of the linear and second parabolic ascent and functions
 * are chosen so that the whole function and its first derivative are continuous.
 *
 * All input angles of attack are in degrees.
 */
final class Drag {
    // Transition points marking the limits of the linear ascend.
    private final double xLinearStart;
    private final double xLinearEnd;

    // Coefficients of the initial parabolic ascend function.
    private final double a;
    private final double c;

    // Coefficients of the middle, linear ascend function.
    private final double s;
    private final double k;

    // Coefficients of the final parabolic ascend function, which plateaus at 90 degrees.
    private final double ae;
    private final double be;
    private final double ce;

    /**
     *
     * @param a quadratic coefficient of initial parabolic ascend function.
     * @param dragMin minimum drag coefficient value at zero degrees angle of attack, which is also equal to the constant coefficient of initial parabolic ascend function.
     * @param xLinearStart angle in radians at which the transition to the middle, linear ascent occurs.
     * @param xLinearEnd angle in radians at which the transition to the final, parabolic ascent, which plateaus at pi/2 radians, occurs.
     */
    Drag(double a, double dragMin, double xLinearStart, double xLinearEnd) {
        this.a = a;
        this.c = dragMin;
        this.xLinearStart = xLinearStart;
        this.xLinearEnd = xLinearEnd;

        final double x90Deg = MathConstants.PI_OVER_TWO;

        s = 2*a*xLinearStart;
        k = a*xLinearStart*xLinearStart - s*xLinearStart + dragMin;

        ae = s/(2 * (xLinearEnd - x90Deg));
        be = -2 * ae * x90Deg;
        ce = s*xLinearEnd + k - ae*xLinearEnd*xLinearEnd - be*xLinearEnd;
    }

    double getDragCoefficient(double x) {
        if (x < xLinearStart) {
            return a*x*x + c;
        } else if (xLinearStart <= x && x <= xLinearEnd) {
            return s*x + k;
        } else {
            return ae*x*x + be*x + ce;
        }
    }
}
