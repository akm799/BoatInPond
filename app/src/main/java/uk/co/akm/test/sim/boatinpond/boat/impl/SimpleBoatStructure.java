package uk.co.akm.test.sim.boatinpond.boat.impl;


import uk.co.akm.test.sim.boatinpond.boat.BoatConstants;
import uk.co.akm.test.sim.boatinpond.math.Function;
import uk.co.akm.test.sim.boatinpond.math.MathConstants;
import uk.co.akm.test.sim.boatinpond.phys.PhysicsConstants;


/**
 * Created by Thanos Mavroidis on 16/12/2017.
 */
public final class SimpleBoatStructure implements BoatConstants {
    private final double cf = 1 - 1/ MathConstants.ROOT_TWO;

    private final double rudderDeflectionCoefficient = 0.9;

    private final double length;
    private final double beam;
    private final double height;
    private final double mass;

    private final double mainBodyLength;
    private final double bowSectionLength;

    private final double longitudinalDragCoefficient;
    private final double lateralDragCoefficient;

    private final double area;
    private final double maxLoad;
    private final double centreOfMassFromStern;
    private final double momentOfInertia;

    private final double rudderCoefficient;

    private double load;
    private double sideIncidenceArea;
    private double frontalIncidenceArea;

    private double totalLongitudinalResistanceCoefficient;
    private double totalLateralResistanceCoefficient;

    public SimpleBoatStructure(
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

        this.mainBodyLength = mainBodyFraction*length;
        this.bowSectionLength = length - mainBodyLength;
        this.longitudinalDragCoefficient = estimateTheLongitudinalDragCoefficient();
        this.lateralDragCoefficient = estimateTheLateralDragCoefficient();
        final double mainSectionMassDensity = computeMainSectionMassDensity(length, beam, mainBodyFraction, mass);

        this.area = beam*(mainBodyLength + bowSectionLength/2);
        this.maxLoad = height* PhysicsConstants.WATER_DENSITY*area - mass;
        this.centreOfMassFromStern = centreOfMassLengthFromStern(length, beam, mainBodyFraction);
        this.momentOfInertia = momentOfInertia(mainSectionMassDensity, length, mainBodyFraction, centreOfMassFromStern);

        this.rudderCoefficient = 0.5*PhysicsConstants.WATER_DENSITY*rudderArea*rudderDeflectionCoefficient*rudderDeflectionCoefficient;

        setLoad(0);
    }

    private double estimateTheLongitudinalDragCoefficient() {
        final double noBow = estimateTheLongitudinalDragCoefficientWithoutBow();
        if (bowSectionLength == 0) {
            return noBow;
        }

        final double deg45ReductionFraction = 0.2;
        final double bowAngle = Math.atan(bowSectionLength/beam/2);
        final double reductionFraction = deg45ReductionFraction*MathConstants.ROOT_TWO*Math.sin(bowAngle);

        return (1 - reductionFraction)*noBow;
    }

    private double estimateTheLongitudinalDragCoefficientWithoutBow() {
        return estimateTheDragCoefficientWithoutBow(length/beam);
    }

    private double estimateTheLateralDragCoefficient() {
        final double mainSectionDragCoefficient = estimateTheLateralDragCoefficientWithoutBow();
        if (bowSectionLength == 0) {
            return mainSectionDragCoefficient;
        }

        final double deg45ReductionFraction = 0.238;
        final double bowAngle = Math.atan(bowSectionLength/beam/2);
        final double reductionFraction = deg45ReductionFraction*MathConstants.ROOT_TWO*Math.cos(bowAngle);
        final double bowSectionDragCoefficient = (1 - reductionFraction)*mainSectionDragCoefficient;

        return (mainSectionDragCoefficient*mainBodyLength + bowSectionDragCoefficient*bowSectionLength)/length;
    }

    private double estimateTheLateralDragCoefficientWithoutBow() {
        return estimateTheDragCoefficientWithoutBow(beam/length);
    }

    private double estimateTheDragCoefficientWithoutBow(double ratio) {
        final double max = 1.28; // No bow section and very short, i.e. a flat plate.
        final double mid = 1.05; // No bow section and square.
        final double min = 0.82; // No bow section and very long.
        final double maxDiff = max - min;
        final double a = Math.log(maxDiff/(mid - min));

        return maxDiff*Math.exp(-a*ratio) + min;
    }

    void setLoad(double load) {
        final double draught = computeDraught(load);
        this.load = load;
        computeIncidenceAreas(draught);
        computeTotalResistanceCoefficients();
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

    @Override
    public double getkAng() {
        throw new UnsupportedOperationException("Deprated method.");
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
