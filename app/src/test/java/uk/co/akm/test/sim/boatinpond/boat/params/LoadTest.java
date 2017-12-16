package uk.co.akm.test.sim.boatinpond.boat.params;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Thanos Mavroidis on 15/12/2017.
 */
@Deprecated
public class LoadTest {
    // Approximate parameters for a Bosun dinghy.
    private final double length = 4.27;
    private final double beam = 1.68;
    private final double height = 0.5;
    private final double mainBodyFraction = 0.75;
    private final double mass = 168;

    private BoatStructure underTest;

    @Before
    public void setUp() {
        underTest = new BoatStructure(length, beam, height, mainBodyFraction, mass);
    }

    @Test
    public void shouldComputeIncidenceAreas() {
        final double[] incidenceAreas = underTest.computeIncidenceAreas(150);
        Assert.assertNotNull(incidenceAreas);
        Assert.assertEquals(2, incidenceAreas.length);
        Assert.assertTrue(incidenceAreas[BoatStructure.LAT_INDEX] > 0);
        Assert.assertTrue(incidenceAreas[BoatStructure.LON_INDEX] > 0);
        Assert.assertTrue(incidenceAreas[BoatStructure.LAT_INDEX] > incidenceAreas[BoatStructure.LON_INDEX]);
    }

    @Test
    public void shouldComputeMaxLoad() {
        final double maxLoad = underTest.getMaxLoad();
        Assert.assertTrue(maxLoad > 0);
    }

    @Test
    public void shouldNotExceedLoad() {
        final double maxLoad = underTest.getMaxLoad();

        try {
            underTest.computeIncidenceAreas(maxLoad + 0.001);
            Assert.fail();
        } catch (IllegalStateException ise) {
            Assert.assertEquals("Max load exceeded.", ise.getMessage());
        }
    }

    private static final class BoatStructure {
        static final double WATER_DENSITY = 999.73; // http://www.open.edu/openlearn/science-maths-technology/the-oceans/content-section-3.2

        static final int LAT_INDEX = 0;
        static final int LON_INDEX = 1;

        private final double length;
        private final double beam;
        private final double height;
        private final double mass;

        private final double area;

        BoatStructure(double length, double beam, double height, double mainBodyFraction, double mass) {
            this.length = length;
            this.beam = beam;
            this.height = height;
            this.mass = mass;

            final double mainBodyLength = mainBodyFraction*length;
            final double bowSectionLength = length - mainBodyLength;

            this.area = beam*(mainBodyLength + bowSectionLength/2);
        }

        double getMaxLoad() {
            return height*WATER_DENSITY*area - mass;
        }

        double[] computeIncidenceAreas(double load) {
            final double draught = (mass + load) / (WATER_DENSITY*area);
            if (draught > height) {
                throw new IllegalStateException("Max load exceeded.");
            }

            return new double[]{draught*length, draught*beam};
        }
    }
}

/**
 * V(d) = A*d
 *
 * rw*V(d) = m + ld => rw*A*d = m + ld => d = (m + ld) / (rw*A)
 *
 * d:  boat draught
 * m:  boat mass
 * rw: water density
 * A:  boat horizontal cross-sectional area (i.e. when viewed vertically from above).
 */
