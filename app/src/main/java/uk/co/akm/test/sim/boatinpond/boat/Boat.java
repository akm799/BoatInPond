package uk.co.akm.test.sim.boatinpond.boat;


import uk.co.akm.test.sim.boatinpond.phys.Body;
import uk.co.akm.test.sim.boatinpond.phys.State;

/**
 * Simulates a propulsion-less boat gliding in a pond after some initial push. The simulation assumes
 * that the boat is pushed from the centre of the pond with some initial speed along some heading.
 * Before this initial push, the boat rudder is fixed at some angle. Due to the water resistance, the
 * boat is expected to, eventually, come to a standstill at some point in the pond, with some final
 * heading.
 *
 * Please note that the mass of the boat as well as its moment of inertia are both assumed to be 1.
 *
 * Created by Thanos Mavroidis on 11/11/2017.
 */
public final class Boat extends Body {
    // Resistance coefficient across the axis of the boat heading.
    private final double kLon;
    // Resistance coefficient perpendicular to the axis of the boat heading.
    private final double kLat;

    // Rudder deflection coefficient.
    private final double kRud;
    // Boat angular motion resistance coefficient.
    private final double kAng;

    // Rudder angle.
    private final double ra;

    // Variables for the update steps, held here for optimization purposes.
    private double cosa; // The cosine of the heading angle.
    private double sina; // The sine of the heading angle.

    private double v;    // The boat speed.
    private double vx;   // The x-axis velocity component.
    private double vy;   // The y-axis velocity component.
    private double vLon; // The component of the velocity vector along the boat axis.

    /**
     * Boat constructor specifying the boat characteristics and the initial conditions.
     *
     * @param constants the boat performance constants
     * @param hdn0 the heading along which the boat is pushed
     * @param v0 the speed at which the boat is initially pushed
     * @param ra the constant rudder angle
     */
    public Boat(BoatConstants constants, double hdn0, double v0, double ra) {
        super(0, 0, 0, hdn0, 0, 0, v0*Math.cos(hdn0), v0*Math.sin(hdn0), 0, 0, 0, 0);

        this.kLon = constants.kLon;
        this.kLat = constants.kLat;
        this.kRud = constants.kRud;
        this.kAng = constants.kAng;

        this.ra = ra;
    }

    @Override
    protected void initUpdate(State start, double dt) {
        final double a = start.hdn();
        cosa = Math.cos(a);
        sina = Math.sin(a);

        v = start.v();
        vx = start.vx();
        vy = start.vy();
        vLon = vx*cosa + vy*sina;
    }

    @Override
    protected void updateAngularAcceleration(State start, double dt) {
        aHdn = kRud*vLon*Math.sin(2*ra) - kAng*omgHdn(); // Moment of inertia is 1.
    }

    @Override
    protected void updateAcceleration(State start, double dt) {
        final double vLat = -vx*sina + vy*cosa;

        //TODO Simulate increased longitudinal resistance due to rudder use (kLonEffective = kLon*kRat*sin(ra) where kRat ~ rudderArea/totalBoatFrontalArea)

        // Evaluate the acceleration wrt the boat heading.
        final double aLon = -kLon*vLon; // Mass is 1
        final double aLat = -kLat*vLat; // Mass is 1

        // Rotate the acceleration wrt the boat heading an angle a (i.e. the reverse of our previous rotation) to get the acceleration wrt our normal coordinate system.
        ax = aLon*cosa - aLat*sina; // Mass is 1
        ay = aLon*sina + aLat*cosa; // Mass is 1
    }

    public double getRudderAngle() {
        return ra;
    }
}
