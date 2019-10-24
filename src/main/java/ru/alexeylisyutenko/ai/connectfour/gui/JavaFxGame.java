package ru.alexeylisyutenko.ai.connectfour.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.lang3.RandomUtils;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class JavaFxGame extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Connect Four Game");
        primaryStage.setScene(new Scene(createContent(), BoardControl.SCENE_WIDTH, BoardControl.SCENE_HEIGHT));
        primaryStage.show();
    }

    private Parent createContent() {
        StackPane root = new StackPane();
        BoardControl boardControl = new BoardControl();
        boardControl.addEventHandler(BoardControl.ColumnClickEvent.COLUMN_CLICKED, event -> {
            int row = RandomUtils.nextInt(0, BOARD_HEIGHT);
            int column = event.getColumn();
            boardControl.displayTokenWithAnimation(row, column, randomTokenColor());
        });
        root.getChildren().add(boardControl);
        StackPane.setAlignment(boardControl, Pos.CENTER);
        return root;
    }

    private Color randomTokenColor() {
        Color color;
        boolean red = RandomUtils.nextBoolean();
        if (red) {
            color = Color.RED;
        } else {
            color = Color.DARKORANGE;
        }
        return color;
    }

}
