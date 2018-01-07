package uk.co.akm.test.sim.boatinpond.boat.impl.quad;

import org.junit.Assert;
import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.boat.Boat;
import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;
import uk.co.akm.test.sim.boatinpond.boat.impl.linear.BoatConstantsApprox;
import uk.co.akm.test.sim.boatinpond.boat.impl.linear.BoatImpl;
import uk.co.akm.test.sim.boatinpond.math.Angles;
import uk.co.akm.test.sim.boatinpond.phys.State;
import uk.co.akm.test.sim.boatinpond.phys.UpdatableState;
import uk.co.akm.test.sim.boatinpond.phys.Updater;


/**
 * Created by Thanos Mavroidis on 08/01/2018.
 */
public class BoatTest {
    private final int nSteps = 1000000;
    private final double accuracy = 0.00001;

    private final double v0 = 2.5; // 9 km/h
    private final double tv = 600; // 10 mins

    private final BoatConstants constants = new SimpleBoatStructure2();

    @Test
    public void shouldSetInitialConditions() {
        final UpdatableState underTest = new BoatImplNew(constants, Math.PI/2, v0);
        Assert.assertEquals(Math.PI/2, underTest.hdn(), accuracy);
        Assert.assertEquals(Math.PI/2, underTest.hdnP(), accuracy);
        Assert.assertNotNull(underTest.hdnTrig());
        Assert.assertEquals(0, underTest.hdnTrig().cos(), accuracy);
        Assert.assertEquals(1, underTest.hdnTrig().sin(), accuracy);

        Assert.assertEquals(v0, underTest.v(), accuracy);
        Assert.assertEquals(0, underTest.vx(), accuracy);
        Assert.assertEquals(v0, underTest.vy(), accuracy);
    }

    //TODO Test expected distance covered.
    @Test
    public void shouldComeToRest() {
        final UpdatableState underTest = new BoatImplNew(constants, 0, v0);
        Assert.assertEquals(v0, underTest.v(), accuracy);
        Assert.assertEquals(v0, underTest.vx(), accuracy);

        Updater.update(underTest, tv, nSteps);
        Assert.assertEquals(0.0, underTest.v(), accuracy);
        Assert.assertEquals(0.0, underTest.vx(), accuracy);
        Assert.assertTrue(underTest.x() > 0);
        Assert.assertTrue(underTest.x() < 71);
        Assert.assertEquals(0.0, underTest.y(), accuracy);
    }
}