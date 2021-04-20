package uk.co.akm.test.sim.boatinpond.boat.impl.foil;

import uk.co.akm.test.sim.boatinpond.math.MathConstants;

/**
 * Lift coefficient as a function of the angle of attack in radians. The angle range is from
 * zero to pi/2 (maximum angle of attack) radians. The lift coefficient function modelled
 * in this class is zero at zero radians, rises to a maximum at an angle of {@code xMaxLift}
 * radians and then falls back to zero at pi/2 radians.
 *
 * This behaviour is taken from https://en.wikipedia.org/wiki/File:Lift_drag_graph.JPG
 *
 * The lift coefficient function is split into 3 parts:
 *
 * 1) Linear ascend
 * 2) Parabolic ascend until the maximum lift deflection angle
 * 3) Parabolic descent to zero at a 90 degree deflection angle
 *
 * The linear ascend part gradient is derived from https://www.researchgate.net/figure/Lift-and-drag-coefficients-for-the-rudder-and-the-keel-The-area-of-the-rudder-A-R-is_fig8_308674535
 *
 * A point is picked at a certain distance {@code dxParabolicAscend} from the maximum list angle where the
 * transition from the linear to the parabolic ascend occurs. The coefficients of the parabolic ascent and
 * descent functions are chosen so that the whole function and its first derivative are continuous.
 *
 * All input angles of attack are in radians.
 */
final class Lift {
    private final double s;
    private final double xMaxLift;

    // Transition angle from linear to parabolic ascend.
    private final double xLinearEnd;

    // Coefficients of parabolic ascend function.
    private final double a;
    private final double b;
    private final double c;

    // Coefficients of parabolic descend.
    private final double ap;
    private final double bp;
    private final double cp;

    /**
     *
     * @param s linear ascent gradient
     * @param xMaxLift angle of attack in radians where the lift is maximised
     * @param dxParabolicAscend distance from maximum lift angle, in degrees, where transition from linear to parabolic ascend occurs.
     */
    public Lift(double s, double xMaxLift, double dxParabolicAscend) {
        this.s = s;
        this.xMaxLift = xMaxLift;

        final double x90Deg = MathConstants.PI_OVER_TWO;
        xLinearEnd = xMaxLift - dxParabolicAscend;

        a = s/(2*(xLinearEnd - xMaxLift));
        b = (s * xMaxLift)/(xMaxLift - xLinearEnd);
        c = xLinearEnd*(s - b) - a*xLinearEnd*xLinearEnd;

        final double m = a*xMaxLift*xMaxLift + b*xMaxLift + c;
        ap = -m/(x90Deg*(x90Deg - 2*xMaxLift) + xMaxLift*xMaxLift);
        bp = -2*ap*xMaxLift;
        cp = m + ap*xMaxLift*xMaxLift;
    }

    double getLiftCoefficient(double x) {
        if (x <= xLinearEnd) {
            return s*x;
        } else if (xLinearEnd < x && x <= xMaxLift) {
            return a*x*x + b*x + c;
        } else {
            return ap*x*x + bp*x + cp;
        }
    }
}
