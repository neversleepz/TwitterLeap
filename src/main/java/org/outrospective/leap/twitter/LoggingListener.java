package org.outrospective.leap.twitter;

import com.leapmotion.leap.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: kon
 * Date: 25/08/13
 * Time: 2:19 AM
 */
public class LoggingListener extends Listener {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onInit(Controller controller) {
        logger.info("Init called");
        super.onInit(controller);
    }

    @Override
    public void onConnect(Controller controller) {
        logger.info("Leap is now connected");
        super.onConnect(controller);

        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
//        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
//        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }

    @Override
    public void onDisconnect(Controller controller) {
        logger.info("Leap is disconnected");
        super.onDisconnect(controller);
    }

    @Override
    public void onExit(Controller controller) {
        logger.info("Leap has been detached");
        super.onExit(controller);
    }

    @Override
    public void onFrame(Controller controller) {
//        logger.info("A frame of motion is ready ot consume");
        super.onFrame(controller);

        Frame frame = controller.frame();
        if (!frame.isValid()) {
            logger.warn("Invalid Frame Detected {}", frame.id());
        } else {
            GestureList gestures = frame.gestures();
            logger.debug ("gestures received {} in frame {}", gestures.count(), frame.id());
            for (Gesture gesture : gestures) {
                if (gesture.isValid()) {
                    switch (gesture.type()) {
                        case TYPE_KEY_TAP:
                            KeyTapGesture keyTapGesture = new KeyTapGesture(gesture);
                            logger.info("Keytap detected - Frame {} Gesture {}", frame.id(), gesture.id());
                            break;
                        case TYPE_SWIPE:
                            logger.info("Swipe detected - Frame {} Gesture {}", frame.id(), gesture.id());
                            break;
                    }
                } else {
                    logger.warn("Gesture invalid: {}", gesture.id());
                }
            }
        }
    }

    @Override
    public void onFocusGained(Controller controller) {
        logger.info("application focus gained");
        super.onFocusGained(controller);
    }

    @Override
    public void onFocusLost(Controller controller) {
        logger.info("application focus lost");
        super.onFocusLost(controller);
    }
}
