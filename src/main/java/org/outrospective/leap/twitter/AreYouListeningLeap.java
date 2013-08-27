package org.outrospective.leap.twitter;

import com.leapmotion.leap.Controller;
import org.outrospective.leap.twitter.listeners.LoggingListener;
import org.outrospective.leap.twitter.listeners.TwitterListener;

import java.io.IOException;

/**
 * User: kon
 * Date: 25/08/13
 * Time: 2:42 AM
 */
public class AreYouListeningLeap {
//    private final LoggingListener listener = new LoggingListener();
    Controller controller = new Controller();

    public static void main(String[] args) {
        new AreYouListeningLeap().go();

    }

    private void go() {
        // Have the sample listener receive events from the controller
//        controller.addListener(listener);
        TwitterListener twitterListener = new TwitterListener();
        controller.addListener(twitterListener);


        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
//        controller.removeListener(twitterListener);
    }
}
