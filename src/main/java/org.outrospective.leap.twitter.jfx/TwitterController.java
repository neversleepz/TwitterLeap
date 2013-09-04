package org.outrospective.leap.twitter.jfx;
/**
 * Sample Skeleton for "twittermockup.fxml" Controller Class
 * You can copy and paste this code into your favorite IDE
 **/

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import org.outrospective.leap.twitter.TweetReader;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.net.URL;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class TwitterController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="avatar1"
    private ImageView avatar1; // Value injected by FXMLLoader

    @FXML // fx:id="avatar2"
    private ImageView avatar2; // Value injected by FXMLLoader

    @FXML // fx:id="avatar3"
    private ImageView avatar3; // Value injected by FXMLLoader

    @FXML // fx:id="avatar4"
    private ImageView avatar4; // Value injected by FXMLLoader

    @FXML // fx:id="handle1"
    private Hyperlink handle1; // Value injected by FXMLLoader

    @FXML // fx:id="handle2"
    private Hyperlink handle2; // Value injected by FXMLLoader

    @FXML // fx:id="handle3"
    private Hyperlink handle3; // Value injected by FXMLLoader

    @FXML // fx:id="handle4"
    private Hyperlink handle4; // Value injected by FXMLLoader

    @FXML // fx:id="stage"
    private AnchorPane stage; // Value injected by FXMLLoader

    @FXML // fx:id="tweet1"
    private HBox tweet1; // Value injected by FXMLLoader

    @FXML // fx:id="tweet2"
    private HBox tweet2; // Value injected by FXMLLoader

    @FXML // fx:id="tweet3"
    private HBox tweet3; // Value injected by FXMLLoader

    @FXML // fx:id="tweet4"
    private HBox tweet4; // Value injected by FXMLLoader

    @FXML // fx:id="tweettext1"
    private Label tweettext1; // Value injected by FXMLLoader

    @FXML // fx:id="tweettext2"
    private Label tweettext2; // Value injected by FXMLLoader

    @FXML // fx:id="tweettext3"
    private Label tweettext3; // Value injected by FXMLLoader

    @FXML // fx:id="tweettext4"
    private Label tweettext4; // Value injected by FXMLLoader


    // iterators over each of the 4 slots
    private ListIterator<ImageView> avatars;
    private ListIterator<Hyperlink> hyperlinks;
    private ListIterator<Label> tweetTexts;

    private ListIterator<Status> tweetIterator;


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws TwitterException {
        assert avatar1 != null : "fx:id=\"avatar1\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert avatar2 != null : "fx:id=\"avatar2\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert avatar3 != null : "fx:id=\"avatar3\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert avatar4 != null : "fx:id=\"avatar4\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert handle1 != null : "fx:id=\"handle1\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert handle2 != null : "fx:id=\"handle2\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert handle3 != null : "fx:id=\"handle3\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert handle4 != null : "fx:id=\"handle4\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert stage != null : "fx:id=\"stage\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweet1 != null : "fx:id=\"tweet1\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweet2 != null : "fx:id=\"tweet2\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweet3 != null : "fx:id=\"tweet3\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweet4 != null : "fx:id=\"tweet4\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweettext1 != null : "fx:id=\"tweettext1\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweettext2 != null : "fx:id=\"tweettext2\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweettext3 != null : "fx:id=\"tweettext3\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweettext4 != null : "fx:id=\"tweettext4\" was not injected: check your FXML file 'twittermockup.fxml'.";

        // Initialize your logic here: all @FXML variables will have been injected
        resetTweetSlotIterators();

        ResponseList<Status> melbjvmTweets = TweetReader.getMelbjvmTweets();

        // populate the first tweets
        melbjvmTweets.stream().limit(4).forEachOrdered(uiRefresh);

        tweetIterator = melbjvmTweets.listIterator();
    }


    private void resetTweetSlotIterators() {
        avatars = Arrays.asList(avatar1, avatar2, avatar3, avatar4).listIterator();
        hyperlinks = Arrays.asList(handle1, handle2, handle3, handle4).listIterator();
        tweetTexts = Arrays.asList(tweettext1, tweettext2, tweettext3, tweettext4).listIterator();
    }

    private Consumer<? super Status> uiRefresh = tweet -> {
        Image image = new Image(tweet.getUser().getBiggerProfileImageURL());
        avatars.next().setImage(image);

        hyperlinks.next().setText(tweet.getUser().getScreenName());
        tweetTexts.next().setText(tweet.getText());

        if (!avatars.hasNext()) { resetTweetSlotIterators(); }
    };

    // these two methods are terrible ways of updating a JFX UI.  Use ObservableLists instead

    public void nextTweet() {
        Stream.Builder<Status> upcomingTweets = Stream.builder();
        int position = 0;

        while (tweetIterator.hasNext() && position < 4) {
            upcomingTweets.add(tweetIterator.next());
            position++;
        }

        Platform.runLater(() -> upcomingTweets.build().forEachOrdered(uiRefresh));
    }

    public void previousTweet() {
        Stream.Builder<Status> upcomingTweets = Stream.builder();
        int position = 0;
        while (tweetIterator.hasPrevious() && position < 4) {
            upcomingTweets.add(tweetIterator.previous());
            position++;
        }

        Platform.runLater(() -> upcomingTweets.build().forEachOrdered(uiRefresh));
    }
}
