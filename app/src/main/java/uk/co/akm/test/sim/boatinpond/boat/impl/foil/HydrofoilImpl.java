package uk.co.akm.test.sim.boatinpond.boat.impl.foil;

import uk.co.akm.test.sim.boatinpond.boat.Hydrofoil;

/**
 * Models the drag and lift forces acting on  hydrofoil using the {@code Drag} and {@code Lift}
 * classes respectively. All constructor parameters of this class are the constructor parameters
 * required to instanciate the {@code Drag} and {@code Lift} classes. All angles are in radians.
 */
public final class HydrofoilImpl implements Hydrofoil {
    private final Drag drag;
    private final Lift lift;
    private final double maxLiftAngle;

    public HydrofoilImpl() {
        this(
                1.035441999,
                0.00913,
                Math.toRadians(40),
                Math.toRadians(74),
                0.93125/Math.toRadians(14.973),
                Math.toRadians(35),
                Math.toRadians(5)
        );
    }

    /**
     *
     * @param aDrag the quadratic drag force coefficient modelling the rudder's drag at low deflection angles
     * @param dragMin the minimum drag force coefficient at zero deflection
     * @param xDragLinearStart the angle (in radians) at which the drag force coefficient model function transitions to a linear ascend
     * @param xDragLinearEnd the angle (in radians) at which the drag force coefficient model function transitions from a linear back to a parabolic ascent which plateaus at pi/2 radians
     * @param sLift the gradient of the initial linear ascend model function for the lift force coefficient of the rudder
     * @param xMaxLift the angle (in radians) where the lift force coefficient is maximised
     * @param dxLiftParabolicAscend the distance from the maximum lift angle (in radians) from which the lift force coefficient model function transitions from a linear to a parabolic ascend
     */
    public HydrofoilImpl(
            double aDrag,
            double dragMin,
            double xDragLinearStart,
            double xDragLinearEnd,
            double sLift,
            double xMaxLift,
            double dxLiftParabolicAscend
    ) {
        drag = new Drag(aDrag, dragMin, xDragLinearStart, xDragLinearEnd);
        lift = new Lift(sLift, xMaxLift, dxLiftParabolicAscend);
        maxLiftAngle = xMaxLift;
    }

    @Override
    public double getDragCoefficient(double angleOfAttack) {
        return drag.getDragCoefficient(angleOfAttack);
    }

    @Override
    public double getLiftCoefficient(double angleOfAttack) {
        return lift.getLiftCoefficient(angleOfAttack);
    }

    @Override
    public double getMaxLiftAngleOfAttack() {
        return maxLiftAngle;
    }
}
