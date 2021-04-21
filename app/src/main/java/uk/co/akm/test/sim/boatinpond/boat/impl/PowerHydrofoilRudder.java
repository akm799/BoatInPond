package uk.co.akm.test.sim.boatinpond.boat.impl;

import uk.co.akm.test.sim.boatinpond.boat.Hydrofoil;
import uk.co.akm.test.sim.boatinpond.boat.HydrofoilRudder;
import uk.co.akm.test.sim.boatinpond.boat.impl.foil.HydrofoilImpl;

public final class PowerHydrofoilRudder extends PowerRudder implements HydrofoilRudder {
    private final double length;
    private final Hydrofoil hydrofoil = new HydrofoilImpl();

    public PowerHydrofoilRudder(double maxAngle, double timeToMaxAngle, double length) {
        super(maxAngle, timeToMaxAngle);

        //TODO Check argument.
        this.length = length;
    }

    @Override
    public double getLength() {
        return length;
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
