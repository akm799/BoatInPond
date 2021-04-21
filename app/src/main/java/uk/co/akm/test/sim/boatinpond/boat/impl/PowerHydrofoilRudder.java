package uk.co.akm.test.sim.boatinpond.boat.impl;

import uk.co.akm.test.sim.boatinpond.boat.Hydrofoil;
import uk.co.akm.test.sim.boatinpond.boat.HydrofoilRudder;
import uk.co.akm.test.sim.boatinpond.boat.impl.foil.HydrofoilImpl;

public final class PowerHydrofoilRudder extends PowerRudder implements HydrofoilRudder {
    private final double halfLength;
    private final Hydrofoil hydrofoil = new HydrofoilImpl();

    public PowerHydrofoilRudder(double maxAngle, double timeToMaxAngle, double length) {
        super(maxAngle, timeToMaxAngle);

        //TODO Check argument.
        this.halfLength = length/2;
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
    public double getLiftCoefficient() {
        return hydrofoil.getLiftCoefficient(getAngleOfAttack());
    }
}
