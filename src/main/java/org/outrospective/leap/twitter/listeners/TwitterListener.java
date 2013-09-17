package org.outrospective.leap.twitter.listeners;

import com.leapmotion.leap.*;
import org.outrospective.leap.twitter.jfx.TwitterController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;
import static java.util.Comparator.naturalOrder;
import static org.outrospective.leap.twitter.listeners.TwitterListener.GestureDir.*;

/**
 * Listen to Swipes with the Leap controller
 *
 * User: kon
 * Date: 25/08/13
 * Time: 5:31 AM
 */
public class TwitterListener extends Listener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private TwitterController javafxController;

    public TwitterController getJavafxController() {
        return javafxController;
    }

    public void setJavafxController(TwitterController javafxController) {
        this.javafxController = javafxController;
    }

    @Override
    public void onConnect(Controller controller) {
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        logger.debug("Swipe gesture enabled");
    }


    @Override
    public void onFrame(Controller controller) {
        // The frame is what leap motion has seen & includes gestures, hands, fingers, screens, etc
        Frame frame = controller.frame();
        GestureList gestures = frame.gestures();

        for (Gesture gesture : gestures) {

            // only interested in frames and gestures that have valid tracking data AND...
            if (gesture.isValid()
                    // the gesture type is swipe AND
                    && gesture.type() == Gesture.Type.TYPE_SWIPE
                    // the swipe has completed.
                    // Other states are START & UPDATE - we dont want to repeat this action multiple times for the one swipe
                    && gesture.state() == Gesture.State.STATE_STOP) {

                // These are Leap Motion classes/methods
                SwipeGesture sg = new SwipeGesture(gesture);
                // not a java.util.Vector, a Leap Motion xyz one
                Vector direction = sg.direction();

                // Look at the vector to find its largest x y z value
                GestureDir dir = guess(direction);
                logger.info(gesture.id() + " ("+ gesture.state()+"): You are swiping in "+ dir + " " + direction);

                if (dir.equals(LEFT) || dir.equals(UP)) {

                    if (javafxController != null) {
//                        javafxController.previousTweet();
                    }

                } else if (dir.equals(RIGHT) || dir.equals(DOWN)) {
                    if (javafxController != null) {
//                        javafxController.nextTweet();
                    }
                }

            }
        }
    }

    enum GestureDir { EMPTY, LEFT, RIGHT, UP, DOWN, FORWARD, BACKWARD}

    private GestureDir guess(Vector direction) {
        if (Vector.zero().equals(direction)) return EMPTY;

        // find the biggest component of the vector
        float[] fromArray = direction.toFloatArray();

        // taking an indulgence with some Java8 streams
        List<Float> floats = Arrays.asList(abs(fromArray[0]), abs(fromArray[1]), abs(fromArray[2]));
        Optional<Float> max = floats.stream().max(naturalOrder());
        int largestComponent = floats.indexOf(max.get());

        // use the magnitude to determine the direction of the swipe
        switch (largestComponent) {
            case 0:
                return fromArray[0] < 0 ? LEFT : RIGHT;
            case 1:
                return fromArray[1] > 0 ? UP : DOWN;
            case 2:
                return fromArray[2] < 0 ? FORWARD : BACKWARD;
        }

        throw new IllegalStateException("Unexpected Vector "+direction);
    }


    @Override
    public void onExit(Controller controller) {
        logger.info("Leap has been exited");
        controller.removeListener(this);
        super.onExit(controller);
    }


}
