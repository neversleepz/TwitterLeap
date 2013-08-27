package org.outrospective.leap.twitter.listeners;

import com.leapmotion.leap.*;
import org.outrospective.leap.twitter.TweetReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import static java.lang.Math.abs;
import static java.util.Comparator.naturalOrder;

/**
 * Listen to Swipes
 *
 * User: kon
 * Date: 25/08/13
 * Time: 5:31 AM
 */
public class TwitterListener extends Listener {

    private ResponseList<Status> melbjvmTweets;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ListIterator<Status> tweeterator;
    private ThreadLocal<Integer> maxId = new ThreadLocal<>();

    @Override
    public void onInit(Controller controller) {
        try {
            melbjvmTweets = TweetReader.getMelbjvmTweets();
            tweeterator = melbjvmTweets.listIterator();
            logger.debug("Retrieved {} tweets", melbjvmTweets.size());
        } catch (TwitterException e) {
            logger.error("Exception hitting twitter", e);

        }
    }

    @Override
    public void onConnect(Controller controller) {
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        logger.debug("Swipe gesture enabled");
    }

    @Override
    public void onFrame(Controller controller) {
        Frame frame = controller.frame();
        if (frame.isValid()) {
            GestureList gestures = frame.gestures();
            logger.trace("gestures received: {} in frame {}", gestures.count(), frame.id());
            for (Gesture gesture : gestures) {
                if (gesture.isValid() && gesture.type() == Gesture.Type.TYPE_SWIPE && gesture.state() == Gesture.State.STATE_STOP) {
                    SwipeGesture sg = new SwipeGesture(gesture);
                    Vector direction = sg.direction();

                    logger.trace(gesture.id()+": Swipe is " + direction.toString() + " with magnitude " + direction.magnitude());
                    logger.trace("Vector.up().dot(direction)       "+Vector.up().dot(direction));
                    logger.trace("Vector.down().dot(direction)     "+Vector.down().dot(direction));
                    logger.trace("Vector.left().dot(direction)     "+Vector.left().dot(direction));
                    logger.trace("Vector.right().dot(direction)    " + Vector.right().dot(direction));
                    logger.trace("Vector.forward().dot(direction)  "+Vector.forward().dot(direction));
                    logger.trace("Vector.backward().dot(direction) "+Vector.backward().dot(direction) +"\n");

                    String dir = guess(direction);
                    logger.info(gesture.id() + " ("+ gesture.state()+"): I think you are swiping "+ dir + " " + direction);

                    if (dir.equals("left") || dir.equals("up")) {
                        if (tweeterator.hasPrevious()) {
                            Status previous = tweeterator.previous();
                            logger.info("Previous Tweet: " + previous.getId() + " " + previous.getUser().getScreenName() + " - " + previous.getText());
                        } else {
                            logger.info("Beginning of tweets");
                        }
                    } else if (dir.equals("right") || dir.equals("down")) {
                        if (tweeterator.hasNext()) {
                            Status next = tweeterator.next();
                            logger.info("Next tweet: " + next.getId() + " " + next.getUser().getScreenName()  + " - " + next.getText());
                        } else {
                            logger.info("End of tweets");
                        }
                    }


                }
            }
        }
    }

    private String guess(Vector direction) {
        if (Vector.zero().equals(direction)) return "empty";

        // find the biggest component of the vector
        float[] fromArray = direction.toFloatArray();
        List<Float> floats = Arrays.asList(abs(fromArray[0]), abs(fromArray[1]), abs(fromArray[2]));
        Optional<Float> max = floats.stream().max(naturalOrder());
        int largestComponent = floats.indexOf(max.get());

        // use the magnitude to determine the direction of the swipe
        switch (largestComponent) {
            case 0:
                return fromArray[0] < 0 ? "left" : "right";
            case 1:
                return fromArray[1] > 0 ? "up" : "down";
            case 2:
                return fromArray[2] < 0 ? "forward" : "backward";
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
