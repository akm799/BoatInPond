package uk.co.akm.test.sim.boatinpond.boat.performance.motor;

import junit.framework.Assert;

import org.junit.Test;

import uk.co.akm.test.sim.boatinpond.boat.Motor;
import uk.co.akm.test.sim.boatinpond.boat.MotorBoat;
import uk.co.akm.test.sim.boatinpond.boat.Rudder;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatConstantsImpl2;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatImpl2;
import uk.co.akm.test.sim.boatinpond.boat.impl.quad.MotorBoatPerformance;
import uk.co.akm.test.sim.boatinpond.boat.performance.BoatPerformanceTestHelper;
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
    private final double turnRate = 57*Math.PI/180; //TODO Check minimum value so we can have a realistic value.

    private final double maxRudderAngle = Math.PI/4;
    private final double timeToMaxRudderDeflection = 2;
    private final double boatToRudderLengthRatio = 20;

    private final double maxSpeed = 9.26; // 18 Knots

    private final double timeToMaxPower = 4;

    private final MotorBoatPerformance performance = new MotorBoatPerformance(launchSpeed, distanceLimit, turnRate, turningSpeed, timeToMaxRudderDeflection, maxSpeed, timeToMaxPower);
    private final MotorBoatConstantsImpl2 constants = new MotorBoatConstantsImpl2(performance, kLatOverKLon, kLonReverseOverKLon, boatLength, cogDistanceFromStern, rudderAreaFraction, maxRudderAngle, boatToRudderLengthRatio);

    private final double mass = 1;
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
        Assert.assertTrue(data.v > 0);
        Assert.assertTrue(data.radius > 0);
        Assert.assertEquals(data.period, twoPi/data.omega, 1E-04);
        System.out.println("v=" + data.v + " omega*radius=" + (data.omega*data.radius));
    }

    private BoatTurnData updateAndRecordTurnData(UpdatableState boat, double dt, double secs) {
        double vSum = 0;
        double vSumSq = 0;
        double period = 0;
        double omegaSum = 0;
        double omegaSumSq = 0;
        double xMin = Double.MAX_VALUE;
        double xMax = Double.MIN_VALUE;
        double yMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;

        double t = 0;
        final double headingStart = boat.hdn();

        final long n = Math.round(secs/dt);
        for (int i=0 ; i<n ; i++) {
            boat.update(dt);

            final double x = boat.x();
            final double y = boat.y();
            final double v = boat.v();
            final double omega = Math.abs(boat.omgHdn());

            t += dt;

            vSum += v;
            vSumSq += v*v;

            omegaSum += omega;
            omegaSumSq += omega*omega;

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
                period = t;
            }
        }

        final double vMean = vSum/n;
        final double vStDev = Math.sqrt(Math.abs( (vSumSq - 2*vMean*vSum + n*vMean*vMean)/n ));

        final double omegaMean = omegaSum/n;
        final double omegaStDev = Math.sqrt(Math.abs( (omegaSumSq - 2*omegaMean*omegaSum + n*omegaMean*omegaMean)/n ));

        return new BoatTurnData(xMin, xMax, yMin, yMax, vMean, vStDev, period, omegaMean, omegaStDev);
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
        public final double period;
        public final double omega;
        public final double omegaStDev;

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
                double period,
                double omega,
                double omegaStDev
        ) {
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
            this.v = v;
            this.vStDev = vStDev;
            this.period = period;
            this.omega = omega;
            this.omegaStDev = omegaStDev;

            if (xMin > xMax) {
                throw new IllegalArgumentException("xMin=" + xMin + " cannot be greater than xMax=" + xMax);
            }

            if (yMin > yMax) {
                throw new IllegalArgumentException("yMin=" + yMin + " cannot be greater than yMax=" + yMax);
            }

            dx = xMax - xMin;
            dy = yMax - yMin;
            radius = (dx + dy)/2;
        }

        void assertTurnIsCircle() {
            Assert.assertEquals(dx, dy, 1E-9);
            Assert.assertTrue(vStDev < 1E-4);
            Assert.assertTrue(omegaStDev < 1E-5);
        }
    }
}
