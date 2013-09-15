package org.outrospective.leap.twitter.jfx;

import javafx.application.Platform;
import javafx.beans.property.adapter.ReadOnlyJavaBeanIntegerProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanIntegerPropertyBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.outrospective.leap.twitter.TweetReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static javafx.collections.FXCollections.observableList;


public class TwitterController {

    public static final Pattern HREF = Pattern.compile("<a.+href=\"([^\"]+)\".*>");
    final Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @FXML
    private ProgressIndicator pageProgress;

    @FXML
    private Slider slider;

    @FXML // fx:id="stage"
    private AnchorPane stage; // Value injected by FXMLLoader

    @FXML
    private ListView<Status> textList;

    @FXML // fx:id="tweet1"
    private HBox tweet1; // Value injected by FXMLLoader

    @FXML // fx:id="tweet2"
    private HBox tweet2; // Value injected by FXMLLoader

    @FXML // fx:id="tweet3"
    private HBox tweet3; // Value injected by FXMLLoader

    @FXML // fx:id="tweet4"
    private HBox tweet4; // Value injected by FXMLLoader

    @FXML
    private Label tweetCount;

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
    private ReadOnlyJavaBeanIntegerProperty totalTweets;


    private void isFxmlInjectedPreconditions() {
        assert avatar1 != null : "fx:id=\"avatar1\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert avatar2 != null : "fx:id=\"avatar2\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert avatar3 != null : "fx:id=\"avatar3\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert avatar4 != null : "fx:id=\"avatar4\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert handle1 != null : "fx:id=\"handle1\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert handle2 != null : "fx:id=\"handle2\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert handle3 != null : "fx:id=\"handle3\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert handle4 != null : "fx:id=\"handle4\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert pageProgress != null : "fx:id=\"pageProgress\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert slider != null : "fx:id=\"slider\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert stage != null : "fx:id=\"stage\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert textList != null : "fx:id=\"textList\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweet1 != null : "fx:id=\"tweet1\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweet2 != null : "fx:id=\"tweet2\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweet3 != null : "fx:id=\"tweet3\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweet4 != null : "fx:id=\"tweet4\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweetCount != null : "fx:id=\"tweetCount\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweettext1 != null : "fx:id=\"tweettext1\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweettext2 != null : "fx:id=\"tweettext2\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweettext3 != null : "fx:id=\"tweettext3\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweettext4 != null : "fx:id=\"tweettext4\" was not injected: check your FXML file 'twittermockup.fxml'.";
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws TwitterException, ExecutionException, InterruptedException {
        isFxmlInjectedPreconditions();

        // Initialize your logic here: all @FXML variables will have been injected

        textList.setCellFactory(statusListView -> new TweetListCell());

        Task<ObservableList<Status>> task = new Task<ObservableList<Status>>() {
            @Override
            protected ObservableList<Status> call() throws Exception {
                updateMessage("Loading MelbJVM Tweets");
                ResponseList<Status> melbjvmTweets = TweetReader.getMelbjvmTweets();
                if (isCancelled()) {
                    updateMessage("Cancelled");
                    return FXCollections.emptyObservableList();
                } else {
                    updateMessage("Loaded " + melbjvmTweets.size() + " tweets");
                    return observableList(melbjvmTweets);
                }
            }

            // ??? This is now binding the task to the UI.  Should this go back to being an outside thread
            @Override
            protected void succeeded() {
                super.succeeded();

                try {
                    totalTweets = ReadOnlyJavaBeanIntegerPropertyBuilder.create()
                            .bean(textList.getItems())
                            .name("size")
                            .getter("size").build();
                } catch (NoSuchMethodException ignored) {
                }

                logger.info("Retrieved {} tweets", totalTweets.get());
                tweetCount.textProperty().bind(totalTweets.asString());

                ObservableList<Status> items = textList.itemsProperty().get();
                items.stream().limit(4).forEachOrdered(uiRefresh);
                tweetIterator = items.listIterator();
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        textList.itemsProperty().bind(task.valueProperty());

        // populate the first tweets
        resetTweetSlotIterators();


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

    // TODO: these two methods are terrible ways of updating a JFX UI.  Use ObservableLists instead
    public void nextTweet() {
        Stream.Builder<Status> upcomingTweets = Stream.builder();
        int position = 0;

        while (tweetIterator.hasNext() && position < 4) {
            upcomingTweets.add(tweetIterator.next());
            position++;
        }

        Platform.runLater(() -> {
            upcomingTweets.build().forEachOrdered(uiRefresh);

            updateControls();
        });

    }

    public void previousTweet() {
        List<Status> upcomingTweets = new LinkedList<>();
        int position = 0;
        while (tweetIterator.hasPrevious() && position < 4) {
            upcomingTweets.add(tweetIterator.previous());
            position++;
        }
        Collections.reverse(upcomingTweets);

        Platform.runLater(() -> {
            upcomingTweets.stream().forEachOrdered(uiRefresh);

            updateControls();
        });
    }

    private void updateControls() {
        slider.adjustValue(tweetIterator.nextIndex());
        System.out.println("Adjusting slider to " + tweetIterator.nextIndex());
        pageProgress.setProgress((float) tweetIterator.nextIndex() / totalTweets.get());
        System.out.println("Adjusting progress to " + (float) tweetIterator.nextIndex() / totalTweets.get());
    }

}
