package ru.alexeylisyutenko.ai.connectfour.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JavaFxGame extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene( new StackPane(new Label("JavaFX 11")), 300, 200);
        primaryStage.setTitle("Connect Four Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
