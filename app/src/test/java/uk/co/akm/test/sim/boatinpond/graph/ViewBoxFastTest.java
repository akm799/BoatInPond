package uk.co.akm.test.sim.boatinpond.graph;

import uk.co.akm.test.sim.boatinpond.env.Environment;

/**
 * Created by Thanos Mavroidis on 06/12/2017.
 */

public final class ViewBoxFastTest extends AbstractViewBoxTest {

    @Override
    protected ViewBoxFeatures buildViewBox(double horizontalSide, double lineSpacing, int screenWidth, int screenHeight) {
        return new ViewBoxFast(horizontalSide, lineSpacing, screenWidth, screenHeight);
    }

    @Override
    protected ViewBoxFeatures buildViewBox(Environment environment, double horizontalSide, double lineSpacing, int screenWidth, int screenHeight) {
        return new ViewBoxFast(environment, horizontalSide, lineSpacing, screenWidth, screenHeight);
    }
}
