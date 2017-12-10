package uk.co.akm.test.sim.boatinpond.boat.params;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Thanos Mavroidis on 10/12/2017.
 */
public class MomentOfInertiaTest {

    @Test
    public void shouldCalculateMomentOfInertia() {
        final double l = 5;
        final double b = 1;
        final double f = 0.8;
        final double mass = 168; // Mass of Bosun dinghy.
        final double lambda0 = evaluateMainMassDensity(l, b, f, mass);

        final double moi = momentOfIntertia(lambda0, l, b, f);
        Assert.assertTrue(moi > 0);
    }

    @Test
    public void shouldCalculateMomentOfInertiaWithoutBowStructure() {
        final double l = 5;
        final double b = 1;
        final double f = 0.8;
        final double mass = 168; // Mass of Bosun dinghy.
        final double lambda0 = evaluateMainMassDensity(l, b, f, mass);

        final double moi = momentOfIntertia(lambda0, l, b, f);
        Assert.assertTrue(moi > 0);

        final double lambda = mass/l;
        final double moiNoBow = momentOfInertiaWithoutBowStructure(lambda, l);
        Assert.assertTrue(moiNoBow > 0);

        Assert.assertTrue(moi < moiNoBow);
    }

    @Test
    public void shouldCalculateMomentOfInertiaForDifferentBowSizes() {
        final double l = 5;
        final double b = 1;
        final double mass = 168; // Mass of Bosun dinghy.

        final double f1 = 0.8;
        final double lambda01 = evaluateMainMassDensity(l, b, f1, mass);
        final double moi1 = momentOfIntertia(lambda01, l, b, f1);
        Assert.assertTrue(moi1 > 0);

        final double f2 = 0.5;
        final double lambda02 = evaluateMainMassDensity(l, b, f2, mass);
        final double moi2 = momentOfIntertia(lambda02, l, b, f2);
        Assert.assertTrue(moi2 > 0);

        Assert.assertTrue(moi2 < moi1);
    }

    private double evaluateMainMassDensity(double l, double b, double f, double mass) {
        final double ls = f*l;
        final double h = l - ls;
        final double mainArea = b*ls;
        final double bowArea = b*h/2;
        final double totalArea = mainArea + bowArea;

        return mass*(mainArea/totalArea)/ls;
    }

    private double momentOfInertiaWithoutBowStructure(double lambda, double l) {
        final Fnc main = new MainMoment(lambda);

        return evaluate(main, -l/2, l/2);
    }

    /**
     * Returns the moment of inertia for heading change rotations.
     *
     * @param lambda0 the constant linear mass density from the stern up to the start of the bow section
     * @param l length overall
     * @param b beam
     * @param f fraction of the length of the main section (i.e. before the bow section) over the total length (i.e. length overall)
     * @return the moment of inertia for heading change rotations
     */
    private double momentOfIntertia(double lambda0, double l, double b, double f) {
        final double ls = f*l;
        final double h = l - ls;
        final double c = centreOfMassLengthFromStern(l, b, f);

        final Fnc main = new MainMoment(lambda0);
        final Fnc bow = new BowMoment(lambda0, h, ls, c);

        return evaluate(main, -c, ls - c) + evaluate(bow, ls - c, l - c);
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

    private double evaluate(Fnc function, double xMin, double xMax) {
        return function.f(xMax) - function.f(xMin);
    }

    interface Fnc {
        double f(double x);
    }

    private static final class MainMoment implements Fnc {
        private final double lambda0;

        MainMoment(double lambda0) {
            this.lambda0 = lambda0;
        }

        @Override
        public double f(double x) {
            return lambda0*x*x*x/3;
        }
    }

    private static final class BowMoment implements Fnc {
        private final double h;
        private final double ko3;
        private final double lambda0;

        public BowMoment(double lambda0, double h, double ls, double c) {
            this.lambda0 = lambda0;
            this.h = h;
            this.ko3 = (h + ls - c)/3;
        }

        @Override
        public double f(double x) {
            final double p = ko3 - x/4;
            return lambda0*p*x*x*x/h;
        }
    }
}
