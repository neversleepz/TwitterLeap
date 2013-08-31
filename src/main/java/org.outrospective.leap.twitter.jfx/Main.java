package org.outrospective.leap.twitter.jfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL resource = getClass().getResource("twittermockup.fxml");
        Parent root = FXMLLoader.load(resource);
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("MelbJVM Tweets");
        primaryStage.setScene(new Scene(root, 1000, 900));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
