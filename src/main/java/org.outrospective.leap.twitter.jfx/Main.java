package org.outrospective.leap.twitter.jfx;

import com.leapmotion.leap.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.outrospective.leap.twitter.AreYouListeningLeap;
import org.outrospective.leap.twitter.listeners.TwitterListener;

import java.net.URL;

public class Main extends Application {

    private final TwitterListener twitterListener = new TwitterListener();
    private final Controller controller = new Controller(twitterListener);

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL resource = getClass().getResource("twittermockup.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent root = (Parent) fxmlLoader.load();
        TwitterController twitterController = fxmlLoader.getController();
        twitterListener.setJavafxController(twitterController);
        primaryStage.setTitle("MelbJVM Tweets");
        primaryStage.setScene(new Scene(root, 710, 480));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

//    @Override
//    public void stop() throws Exception {
//        controller.removeListener(twitterListener);
//    }
}
