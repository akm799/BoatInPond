package uk.co.akm.test.sim.boatinpond.boat.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.math.helper.Function;
import uk.co.akm.test.sim.boatinpond.math.helper.Integrator;
import uk.co.akm.test.sim.boatinpond.math.helper.impl.SimpsonRuleIntegrator;

/**
 * Created by Thanos Mavroidis on 16/12/2017.
 */
public class MomentOfInertiaTest {
    private final double accuracy = 0.00000000000022;

    private final double length = 5;
    private final double beam = 1;
    private final double mainBodyFraction = 0.8;
    private final double mass = 168; // Mass of Bosun dinghy.
    private final double lambda0 = evaluateMainMassDensity(length, beam, mainBodyFraction, mass);

    private SimpleBoatStructure underTest;

    @Before
    public void setUp() {
        underTest = new SimpleBoatStructure(length, beam, 1, mainBodyFraction, mass);
    }

    @Test
    public void shouldCalculateMomentOfInertia() {
        final double moi = underTest.getMomentOfIntertia();
        Assert.assertTrue(moi > 0);
    }

    @Test
    public void shouldCalculateMomentOfInertiaAccurately() {
        final double ls = mainBodyFraction * length;
        final double h = length - ls;
        final double c = centreOfMassLengthFromStern(length, beam, mainBodyFraction);
        final Function moment = new MomentDerivative(lambda0, h, ls, c);
        final Integrator integrator = new SimpsonRuleIntegrator(1000);

        final double expected = integrator.integrate(moment, -c, length - c);
        Assert.assertTrue(expected > 0);

        final double actual = underTest.getMomentOfIntertia();
        Assert.assertTrue(actual > 0);

        Assert.assertEquals(expected, actual, accuracy);
    }

    @Test
    public void shouldCalculateMomentOfInertiaWithoutBowStructure() {
        final double moi = underTest.getMomentOfIntertia();
        Assert.assertTrue(moi > 0);

        final double lambda = mass/ length;
        final double moiNoBowExpected = momentOfInertiaWithoutBowStructure(lambda, length);
        Assert.assertTrue(moiNoBowExpected > 0);

        final SimpleBoatStructure underTestWithoutBow = new SimpleBoatStructure(length, beam, 1, 1, mass);
        final double moiNoBowActual = underTestWithoutBow.getMomentOfIntertia();
        Assert.assertTrue(moiNoBowActual > 0);

        Assert.assertTrue(moi < moiNoBowActual);
        Assert.assertEquals(moiNoBowExpected, moiNoBowActual, accuracy);
    }

    @Test
    public void shouldCalculateMomentOfInertiaForDifferentBowSizes() {
        final double f1 = 0.8;
        final SimpleBoatStructure underTest1 = new SimpleBoatStructure(length, beam, 1, f1, mass);
        final double moi1 = underTest1.getMomentOfIntertia();
        Assert.assertTrue(moi1 > 0);

        final double f2 = 0.5;
        final SimpleBoatStructure underTest2 = new SimpleBoatStructure(length, beam, 1, f2, mass);
        final double moi2 = underTest2.getMomentOfIntertia();
        Assert.assertTrue(moi2 > 0);

        Assert.assertTrue(moi2 < moi1);
    }

    // The mass density on the main section (i.e. before the bow section).
    private double evaluateMainMassDensity(double l, double b, double f, double mass) {
        final double ls = f*l;
        final double h = l - ls;
        final double mainArea = b*ls;
        final double bowArea = b*h/2;
        final double totalArea = mainArea + bowArea;

        return mass*(mainArea/totalArea)/ls;
    }

    private double momentOfInertiaWithoutBowStructure(double lambda, double l) {
        final Function main = new MainMoment(lambda);

        return evaluate(main, -l/2, l/2);
    }

    /**
     * Returns the distance of the centre of mass from the stern.
     *
     * @param l length overall
     * @param b beam
     * @param f fraction of the length of the main section (i.e. before the bow section) over the total length (i.e. length overall)
     * @return the distance of the centre of mass from the stern
     */
    private double centreOfMassLengthFromStern(double l, double b, double f) {
        final double ls = l*f;
        final double h = l*(1 - f);
        final double ho2 = h/2;
        final double cf = 1 - 1/Math.sqrt(2);

        final double bowArea = b*ho2;
        final double bowCoM = ls + h*cf;

        final double mainArea = b*ls;
        final double mainCoM = ls/2;

        return (bowCoM*bowArea + mainCoM*mainArea)/(bowArea + mainArea);
    }

    private double evaluate(Function function, double xMin, double xMax) {
        return function.f(xMax) - function.f(xMin);
    }

    private static final class MainMoment implements Function {
        private final double lambda0;

        MainMoment(double lambda0) {
            this.lambda0 = lambda0;
        }

        @Override
        public double f(double x) {
            return lambda0*x*x*x/3;
        }
    }

    /**
     * The function which needs to be integrated to give the moment of inertia.
     */
    private static final class MomentDerivative implements Function {
        private final Function massDensity;

        MomentDerivative(double lambda0, double h, double ls, double c) {
            massDensity = new MassDensity(lambda0, h, ls, c);
        }

        @Override
        public double f(double x) {
            return x*x*massDensity.f(x);
        }
    }

    /**
     * The mass density function for the whole boat (including the bow structure).
     */
    private static final class MassDensity implements Function {
        private final double h;
        private final double k;
        private final double lambda0;

        private final double p;

        MassDensity(double lambda0, double h, double ls, double c) {
            this.lambda0 = lambda0;
            this.h = h;
            this.p = ls - c;
            this.k = h + p;
        }

        @Override
        public double f(double x) {
            if (x <= p) {
                return lambda0; // Main section (constant).
            } else {
                return lambda0 * (k - x) / h; // Bow section.
            }
        }
    }
}
