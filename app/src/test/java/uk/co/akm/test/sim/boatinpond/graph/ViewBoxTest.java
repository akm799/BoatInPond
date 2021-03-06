package uk.co.akm.test.sim.boatinpond.graph;


import uk.co.akm.test.sim.boatinpond.env.Environment;

/**
 * Created by Thanos Mavroidis on 19/11/2017.
 */
public class ViewBoxTest extends AbstractViewBoxTest {

    @Override
    protected ViewBoxFeatures buildViewBox(double horizontalSide, double lineSpacing, int screenWidth, int screenHeight) {
        return new ViewBox(horizontalSide, lineSpacing, screenWidth, screenHeight);
    }

    @Override
    protected ViewBoxFeatures buildViewBox(Environment environment, double horizontalSide, double lineSpacing, int screenWidth, int screenHeight) {
        return new ViewBox(environment, horizontalSide, lineSpacing, screenWidth, screenHeight);
    }
}
