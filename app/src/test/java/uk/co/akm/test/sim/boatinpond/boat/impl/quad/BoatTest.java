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

    @Test
    public void shouldComeToRest() {
        final double slowingDownTime = 600; // 10 mins

        final UpdatableState underTest = new BoatImplNew(constants, 0, v0);
        Assert.assertEquals(v0, underTest.v(), accuracy);
        Assert.assertEquals(v0, underTest.vx(), accuracy);

        final double k = constants.getkLon();
        final double m = constants.getMass();
        final double xLimit = m*(1 + Math.log(v0))/k;
        final double xLimitApproach = 0.002; // How close we can get to the limit within the (long) time during which we are slowing down.

        Updater.update(underTest, slowingDownTime, nSteps);
        Assert.assertEquals(0.0, underTest.v(), accuracy);
        Assert.assertEquals(0.0, underTest.vx(), accuracy);
        Assert.assertTrue(underTest.x() > 0);
        Assert.assertTrue(underTest.x() < xLimit);
        Assert.assertTrue(xLimit - underTest.x() < xLimitApproach);
        Assert.assertEquals(0.0, underTest.y(), accuracy);
    }
}