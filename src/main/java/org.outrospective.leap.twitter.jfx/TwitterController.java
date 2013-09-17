package org.outrospective.leap.twitter.jfx;

import javafx.beans.property.adapter.ReadOnlyJavaBeanIntegerProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanIntegerPropertyBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import org.outrospective.leap.twitter.TweetReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import static javafx.collections.FXCollections.observableList;


public class TwitterController {

    public static final Pattern HREF = Pattern.compile("<a.+href=\"([^\"]+)\".*>");
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private ProgressIndicator pageProgress;

    @FXML
    private Slider slider;

    @FXML // fx:id="stage"
    private VBox stage; // Value injected by FXMLLoader

    @FXML
    private ListView<Status> textList;

    @FXML
    private Label tweetCount;

    private ReadOnlyJavaBeanIntegerProperty totalTweets;


    private void isFxmlInjectedPreconditions() {
        assert pageProgress != null : "fx:id=\"pageProgress\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert slider != null : "fx:id=\"slider\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert stage != null : "fx:id=\"stage\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert textList != null : "fx:id=\"textList\" was not injected: check your FXML file 'twittermockup.fxml'.";
        assert tweetCount != null : "fx:id=\"tweetCount\" was not injected: check your FXML file 'twittermockup.fxml'.";
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws TwitterException, ExecutionException, InterruptedException {
        isFxmlInjectedPreconditions();

        // Initialize your logic here: all @FXML variables will have been injected

        textList.setCellFactory(statusListView -> new TweetListCell());

        loadTweetsIntoList();


    }

    private void loadTweetsIntoList() {
        Task<ObservableList<Status>> task = new Task<ObservableList<Status>>() {
            @Override
            protected ObservableList<Status> call() throws Exception {
                updateMessage("Loading MelbJVM Tweets");
                ResponseList<Status> melbjvmTweets = TweetReader.getMyTweets(1);
                if (isCancelled()) {
                    updateMessage("Cancelled");
                    return FXCollections.emptyObservableList();
                } else {
                    updateMessage("Loaded " + melbjvmTweets.size() + " tweets");
                    return observableList(melbjvmTweets);
                }
            }

            // ??? This is now binding the task to the UI.  Should this go back to being an outside thread?
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
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        textList.itemsProperty().bind(task.valueProperty());
    }

}
