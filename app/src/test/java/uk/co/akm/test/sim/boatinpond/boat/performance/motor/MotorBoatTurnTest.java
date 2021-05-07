package uk.co.akm.test.sim.boatinpond.boat.performance.motor;

import junit.framework.Assert;

import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.boat.Motor;
import uk.co.akm.test.sim.boatinpond.boat.MotorBoat;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatConstantsImpl2;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatImpl2;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatPerformance;
import uk.co.akm.test.sim.boatinpond.boat.performance.helper.BoatAngleFinder;
import uk.co.akm.test.sim.boatinpond.boat.performance.helper.BoatPerformanceTestHelper;
import uk.co.akm.test.sim.boatinpond.boat.performance.helper.Distribution;
import uk.co.akm.test.sim.boatinpond.phys.UpdatableState;

public class MotorBoatTurnTest {
    private final double kLatOverKLon = 50;
    private final double kLonReverseOverKLon = 10;
    private final double boatLength = 4;
    private final double cogDistanceFromStern = 1.5;
    private final double rudderAreaFraction = 0.075;

    private final double launchSpeed = 3.01; // 6 knots
    private final double distanceLimit = 75;
    private final double turningSpeed = 20.56; // 40 Knots
    private final double turnRadius = 20.66668818928022; // Equivalent to 57 degrees per second turning rate.

    private final double maxRudderAngle = Math.PI/4;
    private final double timeToMaxRudderDeflection = 2;
    private final double boatToRudderLengthRatio = 20;

    private final double maxSpeed = 9.26; // 18 Knots

    private final double timeToMaxPower = 4;

    private final MotorBoatPerformance performance = new MotorBoatPerformance(launchSpeed, distanceLimit, turningSpeed, turnRadius, timeToMaxRudderDeflection, maxSpeed, timeToMaxPower);
    private final MotorBoatConstantsImpl2 constants = new MotorBoatConstantsImpl2(performance, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern, rudderAreaFraction, maxRudderAngle, boatToRudderLengthRatio);

    private final double dt = 0.0001;
    private final double twoPi = 2*Math.PI;

    @Test
    public void shouldMakeSustainedTurn() {
        final MotorBoat underTest = new MotorBoatImpl2(constants, Math.PI/2, 0);
        Assert.assertEquals(0, underTest.v(), 0);

        final double threeMins = 3*60;
        final double rudderAngleDeg = 34.98;
        final double rudderAngle = rudderAngleDeg*Math.PI/180;
        applyMaximumPower(underTest.getMotor());
        applyLeftDeflection(underTest.getRudder(), rudderAngle);
        BoatPerformanceTestHelper.update(underTest, dt, threeMins);

        final double v = underTest.v();
        Assert.assertTrue(v > 0);

        final BoatTurnData data = updateAndRecordTurnData(underTest, dt, threeMins);
        data.assertTurnIsCircle();

        // Check the observed turning speed.
        Assert.assertEquals(v, data.v, 1E-09);

        // Check basic circular motion formulae.
        Assert.assertEquals(data.period, twoPi/data.omega, 1E-05);
        Assert.assertEquals(data.v, data.omega*data.radius, 1E-09);
        Assert.assertEquals(data.a, data.v*data.v/data.radius, 1E-10);

        // Check for a constant angle between the boat's velocity vector and actual heading.
        Assert.assertTrue(data.phi > 0);
        Assert.assertTrue(data.phiStDev < 1E-06);

        // Check the angle between the boat's heading and velocity vectors.
        final BoatAngleFinder angleFinder = new BoatAngleFinder(1, constants.getkLat(), data.radius, v);
        angleFinder.assertAngleEquals(data.phi, 1E-03);
    }

    private BoatTurnData updateAndRecordTurnData(UpdatableState boat, double dt, double secs) {
        final Distribution vDistribution = new Distribution();
        final Distribution aDistribution = new Distribution();
        final Distribution phiDistribution = new Distribution();
        final Distribution omegaDistribution = new Distribution();

        double period = 0;
        double xMin = Double.MAX_VALUE;
        double xMax = Double.MIN_VALUE;
        double yMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;

        double rotationTime = 0;
        double distancePrevious = 0;
        boolean distanceIncreasePrevious = false;

        final double headingStart = boat.hdn();

        final double x0 = boat.x();
        final double y0 = boat.y();

        double t = 0;
        final long n = Math.round(secs/dt);
        for (int i=0 ; i<n ; i++) {
            boat.update(dt);
            t += dt;

            final double x = boat.x();
            final double y = boat.y();
            final double v = boat.v();
            final double a = Math.sqrt(Math.pow(boat.ax(), 2) + Math.pow(boat.ay(), 2));
            final double omega = Math.abs(boat.omgHdn());

            vDistribution.add(v);
            aDistribution.add(a);
            omegaDistribution.add(omega);

            final double distance = distance(x, y, x0, y0);
            final boolean distanceIncrease = (distance > distancePrevious);
            if (i > 0 && distanceIncrease && !distanceIncreasePrevious && rotationTime == 0) {
                rotationTime = t - dt/2;
            }
            distancePrevious = distance;
            distanceIncreasePrevious = distanceIncrease;

            final double vx = boat.vx();
            final double vy = boat.vy();
            final double heading = boat.hdnP();
            final double cosHeading = Math.cos(heading);
            final double sinHeading = Math.sin(heading);
            final double phi = Math.abs(BoatPerformanceTestHelper.angleBetween(vx, vy, cosHeading, sinHeading));
            phiDistribution.add(phi);

            if (x < xMin) {
                xMin = x;
            }

            if (x > xMax) {
                xMax = x;
            }

            if (y < yMin) {
                yMin = y;
            }

            if (y > yMax) {
                yMax = y;
            }

            if (Math.abs(boat.hdn() - headingStart) > twoPi && period == 0) {
                period = t - dt/2;
            }
        }

        final double vMean = vDistribution.getAverage();
        final double vStDev = vDistribution.getStandardDeviation();

        final double aMean = aDistribution.getAverage();
        final double aStDev = aDistribution.getStandardDeviation();

        final double omegaMean = omegaDistribution.getAverage();
        final double omegaStDev = omegaDistribution.getStandardDeviation();

        final double phi = phiDistribution.getAverage();
        final double phiStDev = phiDistribution.getStandardDeviation();

        return new BoatTurnData(
                xMin,
                xMax,
                yMin,
                yMax,
                vMean,
                vStDev,
                aMean,
                aStDev,
                period,
                rotationTime,
                omegaMean,
                omegaStDev,
                phi,
                phiStDev
        );
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }

    private void applyMaximumPower(Motor motor) {
        motor.turnOn();
        motor.increaseControlInput();
        increaseToMaximumPower(motor);
        motor.noControlInput();
    }

    private void increaseToMaximumPower(Motor motor) {
        double increase = Double.MAX_VALUE;
        while (increase > 0) {
            final double beforeIncrease = motor.getForce();
            motor.update(dt);
            increase = motor.getForce() - beforeIncrease;
        }
    }

    private void applyLeftDeflection(Rudder rudder, double angle) {
        rudder.leftControlInput();
        increaseToDeflection(rudder, angle);
        rudder.noControlInput();
    }

    private void increaseToDeflection(Rudder rudder, double angle) {
        double diffAfter = -1;
        double diffBefore = 0;
        while (diffAfter < diffBefore) {
            diffBefore = Math.abs(angle - rudder.getRudderAngle());
            rudder.update(dt);
            diffAfter = Math.abs(angle - rudder.getRudderAngle());
        }
    }

    private static final class BoatTurnData {
        public final double xMin;
        public final double xMax;
        public final double yMin;
        public final double yMax;
        public final double v;
        public final double vStDev;
        public final double a;
        public final double aStDev;
        public final double period;
        public final double rotationTime;
        public final double omega;
        public final double omegaStDev;
        public final double phi;
        public final double phiStDev;

        public final double dx;
        public final double dy;
        public final double radius;

        BoatTurnData(
                double xMin,
                double xMax,
                double yMin,
                double yMax,
                double v,
                double vStDev,
                double a,
                double aStDev,
                double period,
                double rotationTime,
                double omega,
                double omegaStDev,
                double phi,
                double phiStDev
        ) {
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
            this.v = v;
            this.vStDev = vStDev;
            this.a = a;
            this.aStDev = aStDev;
            this.period = period;
            this.rotationTime = rotationTime;
            this.omega = omega;
            this.omegaStDev = omegaStDev;
            this.phi = phi;
            this.phiStDev = phiStDev;

            if (xMin > xMax) {
                throw new IllegalArgumentException("xMin=" + xMin + " cannot be greater than xMax=" + xMax);
            }

            if (yMin > yMax) {
                throw new IllegalArgumentException("yMin=" + yMin + " cannot be greater than yMax=" + yMax);
            }

            dx = xMax - xMin;
            dy = yMax - yMin;
            radius = (dx + dy)/4;
        }

        void assertTurnIsCircle() {
            Assert.assertTrue(v > 0);
            Assert.assertTrue(a > 0);
            Assert.assertTrue(omega > 0);
            Assert.assertTrue(radius > 0);
            Assert.assertTrue(period > 0);

            Assert.assertEquals(dx, dy, 1E-09); // Circle described.
            Assert.assertTrue(vStDev < 1E-04); // Constant turning speed.
            Assert.assertTrue(aStDev < 1E-06); // Constant acceleration.
            Assert.assertTrue(omegaStDev < 1E-05); // Constant angular speed.
            Assert.assertTrue(Math.abs(rotationTime - period) < 1E-03); // Circle period cross calculation check.
        }
    }
}
