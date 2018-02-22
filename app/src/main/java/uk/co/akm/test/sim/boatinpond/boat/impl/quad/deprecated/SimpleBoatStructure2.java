package uk.co.akm.test.sim.boatinpond.boat.impl.quad.deprecated;


import uk.co.akm.test.sim.boatinpond.math.Function;
import uk.co.akm.test.sim.boatinpond.math.MathConstants;
import uk.co.akm.test.sim.boatinpond.phys.PhysicsConstants;


/**
 * Created by Thanos Mavroidis on 30/12/2017.
 */
@Deprecated
public final class SimpleBoatStructure2 implements QuadBoatConstants {
    private final double cf = 1 - 1/ MathConstants.ROOT_TWO;
    private final double targetLongitudinalResistanceCoefficient = 6.438736859097161; // Gives, roughly, a 50 m stopping distance from an initial velocity of 2.5 m/s
    private final double lateralToLongitudinalResistanceCoefficientRatio = 5;

    private final double length;
    private final double beam;
    private final double height;
    private final double mass;
    private final double rudderArea;

    private final double mainBodyLength;
    private final double bowSectionLength;

    private final double area;
    private final double maxLoad;
    private final double centreOfMassFromStern;
    private final double momentOfInertia;

    private double load;
    private double sideIncidenceArea;
    private double frontalIncidenceArea;

    private double longitudinalDragCoefficient;
    private double lateralDragCoefficient;
    private double totalLongitudinalResistanceCoefficient;
    private double totalLateralResistanceCoefficient;

    private double rudderCoefficient;
    private double rudderOverFrontalIncidenceArea;

    // Approximate parameters for a Bosun dinghy.
    public SimpleBoatStructure2() {
        this(4.27, 1.68, 0.5, 0.75, 168, 0.35);
    }

    private SimpleBoatStructure2(
            double length,
            double beam,
            double height,
            double mainBodyFraction,
            double mass,
            double rudderArea) {
        this.length = length;
        this.beam = beam;
        this.height = height;
        this.mass = mass;
        this.rudderArea = rudderArea;

        this.mainBodyLength = mainBodyFraction*length;
        this.bowSectionLength = length - mainBodyLength;
        final double mainSectionMassDensity = computeMainSectionMassDensity(length, beam, mainBodyFraction, mass);

        this.area = beam*(mainBodyLength + bowSectionLength/2);
        this.maxLoad = height*PhysicsConstants.WATER_DENSITY*area - mass;
        this.centreOfMassFromStern = centreOfMassLengthFromStern(length, beam, mainBodyFraction);
        this.momentOfInertia = momentOfInertia(mainSectionMassDensity, length, mainBodyFraction, centreOfMassFromStern);

        setLoad(0, true);
    }

    public void setLoad(double load) {
        setLoad(load, false);
    }

    private void setLoad(double load, boolean computeDragCoefficients) {
        final double draught = computeDraught(load);
        this.load = load;

        computeIncidenceAreas(draught);
        if (computeDragCoefficients) {
            computeDragCoefficients();
        }

        computeTotalResistanceCoefficients();

        if (computeDragCoefficients) {
            // The rudder force coefficient estimation depends on the total lateral drag coefficient value.
            rudderCoefficient = estimateRudderForceCoefficient(1.047197551, 2.5); // ~ 60 degrees per second at 5 knots.
        }
    }

    private double computeDraught(double load) {
        final double draught = (mass + load) / (PhysicsConstants.WATER_DENSITY*area);
        if (draught > height) {
            throw new IllegalStateException("Max load exceeded.");
        }

        return draught;
    }

    private void computeIncidenceAreas(double draught) {
        frontalIncidenceArea = draught*beam;
        sideIncidenceArea = draught*length;
        rudderOverFrontalIncidenceArea = rudderArea/frontalIncidenceArea;
    }

    /**
     * https://en.wikipedia.org/wiki/Drag_coefficient
     */
    private void computeDragCoefficients() {
        final double halfWaterDensity = 0.5*PhysicsConstants.WATER_DENSITY;
        longitudinalDragCoefficient = targetLongitudinalResistanceCoefficient/(halfWaterDensity*frontalIncidenceArea);
        lateralDragCoefficient = lateralToLongitudinalResistanceCoefficientRatio*longitudinalDragCoefficient;
    }

    /**
     * https://en.wikipedia.org/wiki/Drag_coefficient
     */
    private void computeTotalResistanceCoefficients() {
        final double halfWaterDensity = 0.5*PhysicsConstants.WATER_DENSITY;
        totalLongitudinalResistanceCoefficient = halfWaterDensity*longitudinalDragCoefficient*frontalIncidenceArea;
        totalLateralResistanceCoefficient = halfWaterDensity*lateralDragCoefficient*sideIncidenceArea;
    }

    /**
     * Returns the distance of the centre of mass from the stern.
     *
     * @param length length overall
     * @param beam beam
     * @param mainBodyFraction fraction of the length of the main section (i.e. before the bow section) over the total length (i.e. length overall)
     * @return the distance of the centre of mass from the stern
     */
    private double centreOfMassLengthFromStern(double length, double beam, double mainBodyFraction) {
        final double ls = length*mainBodyFraction;
        final double h = length - ls;
        final double ho2 = h/2;

        final double bowArea = beam*ho2;
        final double bowCoM = ls + h*cf;

        final double mainArea = beam*ls;
        final double mainCoM = ls/2;

        return (bowCoM*bowArea + mainCoM*mainArea)/(bowArea + mainArea);
    }

    /**
     * Returns the linear mass density of the main boat section (i.e. excluding the bow section).
     *
     * @param length length overall
     * @param beam beam
     * @param mainBodyFraction fraction of the length of the main section (i.e. before the bow section over the total length (i.e. length overall)
     * @param mass the total boat mass
     * @return the linear mass density of the main boat section (i.e. excluding the bow section)
     */
    private double computeMainSectionMassDensity(double length, double beam, double mainBodyFraction, double mass) {
        final double mainSectionLength = mainBodyFraction*length;
        final double mainSectionArea = beam*mainSectionLength;
        final double bowSectionLength = length - mainSectionLength;
        final double bowSectionArea = beam*bowSectionLength/2;
        final double totalArea = mainSectionArea + bowSectionArea;
        final double mainSectionMass = mainSectionArea*mass/totalArea;

        return mainSectionMass/mainSectionLength;
    }

    /**
     * Returns the moment of inertia for heading change rotations.
     *
     * @param lambda0 the constant linear mass density from the stern up to the start of the bow section
     * @param length length overall
     * @param mainBodyFraction fraction of the length of the main section (i.e. before the bow section) over the total length (i.e. length overall)
     * @param centreOfMassLengthFromStern the distance of the boat centre of mass from the stern
     * @return the moment of inertia for heading change rotations
     */
    private double momentOfInertia(double lambda0, double length, double mainBodyFraction, double centreOfMassLengthFromStern) {
        final double ls = mainBodyFraction*length;
        final double h = length - ls;
        final double c = centreOfMassLengthFromStern;

        final Function main = new MainMoment(lambda0);
        final Function bow = new BowMoment(lambda0, h, ls, c);

        if (mainBodyFraction < 1) {
            return evaluate(main, -c, ls - c) + evaluate(bow, ls - c, length - c);
        } else {
            return evaluate(main, -c, ls - c);
        }
    }

    private double evaluate(Function function, double xMin, double xMax) {
        return function.f(xMax) - function.f(xMin);
    }

    // Rudder surface area is assumed to be constant wrt boat draught.
    private double estimateRudderForceCoefficient(double omgAbs, double v) {
        final double tr = computeTurningResistanceTorqueMagnitude(omgAbs);

        return tr/(v*v*centreOfMassFromStern);
    }

    private double computeTurningResistanceTorqueMagnitude(double omgAbs) {
        final double l = length;
        final double x = centreOfMassFromStern;
        final double f = (l - x);
        final double k = totalLateralResistanceCoefficient;

        final double cf = k*(Math.pow(x, 4) + Math.pow(f, 4))/(8*l);
        return cf*omgAbs*omgAbs;
    }

    // This method is only for test purposes.
    double getMaxLoad() {
        return maxLoad;
    }

    // This method is only for test purposes.
    double getSideIncidenceArea() {
        return sideIncidenceArea;
    }

    // This method is only for test purposes.
    double getFrontalIncidenceArea() {
        return frontalIncidenceArea;
    }

    // This method is only for test purposes.
    public double getLongitudinalDragCoefficient() {
        return longitudinalDragCoefficient;
    }

    // This method is only for test purposes.
    public double getLateralDragCoefficient() {
        return lateralDragCoefficient;
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

    private static final class BowMoment implements Function {
        private final double h;
        private final double ko3;
        private final double lambda0;

        BowMoment(double lambda0, double h, double ls, double c) {
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

    @Override
    public double getkLon() {
        return totalLongitudinalResistanceCoefficient;
    }

    @Override
    public double getkLat() {
        return totalLateralResistanceCoefficient;
    }

    @Override
    public double getKLonReverse() {
        return 10*totalLongitudinalResistanceCoefficient;
    }

    @Override
    public double getMass() {
        return (mass + load);
    }

    @Override
    public double getMomentOfInertia() {
        return momentOfInertia;
    }

    @Override
    public double getLength() {
        return length;
    }

    @Override
    public double getCentreOfMassFromStern() {
        return centreOfMassFromStern;
    }

    @Override
    public double getkRud() {
        return rudderCoefficient;
    }

    //TODO Rename this method.
    @Override
    public double getkAng() {
        return rudderOverFrontalIncidenceArea;
    }
}