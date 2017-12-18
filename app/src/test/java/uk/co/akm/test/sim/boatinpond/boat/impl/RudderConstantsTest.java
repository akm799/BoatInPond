package uk.co.akm.test.sim.boatinpond.boat.impl;

import junit.framework.Assert;

import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.phys.PhysicsConstants;

/**
 * Created by mavroidt on 18/12/2017.
 */
public class RudderConstantsTest {
    private final double length = 4.27;
    private final double beam = 1.68;
    private final double height = 0.5;
    private final double mainBodyFraction = 0.75;
    private final double mass = 168;

    private final double cf = 0.5*0.9*0.9*PhysicsConstants.WATER_DENSITY;

    @Test
    public void shouldGetBoatLength() {
        final double rudderArea= 1;
        final BoatConstants underTest = new SimpleBoatStructure(length, beam, height, mainBodyFraction, mass, rudderArea);
        Assert.assertEquals(length, underTest.getLength());
    }

    @Test
    public void shouldCalculateTheRudderCoefficient() {
        final double rudderArea1= 1;
        final double expectedKRud1 = cf*rudderArea1;
        final BoatConstants underTest1 = new SimpleBoatStructure(length, beam, height, mainBodyFraction, mass, rudderArea1);
        final double kRud1 = underTest1.getkRud();
        Assert.assertEquals(expectedKRud1, kRud1);

        final double rudderArea2= 2;
        final double expectedKRud2 = cf*rudderArea2;
        final BoatConstants underTest2 = new SimpleBoatStructure(length, beam, height, mainBodyFraction, mass, rudderArea2);
        final double kRud2 = underTest2.getkRud();
        Assert.assertEquals(expectedKRud2, kRud2);
    }
}
