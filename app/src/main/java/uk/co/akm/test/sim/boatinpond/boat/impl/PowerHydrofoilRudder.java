package uk.co.akm.test.sim.boatinpond.boat.impl;

import uk.co.akm.test.sim.boatinpond.boat.Hydrofoil;
import uk.co.akm.test.sim.boatinpond.boat.HydrofoilRudder;
import uk.co.akm.test.sim.boatinpond.boat.impl.foil.HydrofoilImpl;

public final class PowerHydrofoilRudder extends PowerRudder implements HydrofoilRudder {
    private final double halfLength;
    private final Hydrofoil hydrofoil = new HydrofoilImpl();

    private final double dZeroAngle;
    private final double dZeroToMaxAngleRange;

    public PowerHydrofoilRudder(double maxAngle, double timeToMaxAngle, double length) {
        super(maxAngle, timeToMaxAngle);

        checkArguments(length);
        this.halfLength = length/2;

        final double dMaxAngle = hydrofoil.getDragCoefficient(getMaxRudderAngle());
        this.dZeroAngle = hydrofoil.getDragCoefficient(0);
        this.dZeroToMaxAngleRange = dMaxAngle - dZeroAngle;
    }

    private void checkArguments(double length) {
        if (length < 0) {
            throw new IllegalArgumentException("Negative boat length: " + length);
        }
    }

    @Override
    public double getHalfLength() {
        return halfLength;
    }

    @Override
    public double getAngleOfAttack() {
        return getAngleAbsolute();
    }

    @Override
    public double getDragCoefficient() {
        return hydrofoil.getDragCoefficient(getAngleOfAttack());
    }

    @Override
    public double normaliseDragCoefficient(double rawValue) {
        return (rawValue - dZeroAngle)/dZeroToMaxAngleRange;
    }

    @Override
    public double getLiftCoefficient() {
        return hydrofoil.getLiftCoefficient(getAngleOfAttack());
    }
}
