package ru.alexeylisyutenko.ai.connectfour.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.lang3.RandomUtils;
import ru.alexeylisyutenko.ai.connectfour.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.game.*;
import ru.alexeylisyutenko.ai.connectfour.player.RandomPlayer;

import javax.swing.*;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class JavaFxGame extends Application {
    private BoardControl boardControl;
    private Button startGameButton;
    private Button stopGameButton;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Connect Four Game");
        primaryStage.setScene(new Scene(createContent(), BoardControl.SCENE_WIDTH, BoardControl.SCENE_HEIGHT));

        //
        startGameButton.setOnMouseClicked(event -> startGame());


        primaryStage.show();
    }

    private Parent createContent() {
        HBox root = new HBox();

        Parent controlArea = createControlArea();
        root.getChildren().add(controlArea);

        Parent gameArea = createGameArea();
        root.getChildren().add(gameArea);
        HBox.setHgrow(gameArea, Priority.ALWAYS);

        return root;
    }

    private Parent createControlArea() {
        startGameButton = new Button("Start Game");
        startGameButton.setPrefWidth(200);
        stopGameButton = new Button("Stop Game");
        stopGameButton.setPrefWidth(200);

        VBox vBox = new VBox(startGameButton, stopGameButton);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(10);
        vBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
//        vBox.setStyle("-fx-background-color: gray");
        vBox.setPadding(new Insets(10));

        return vBox;
    }

    private Parent createGameArea() {
        StackPane boardStackPane = new StackPane();
        boardControl = new BoardControl();
        boardControl.addEventHandler(BoardControl.ColumnClickEvent.COLUMN_CLICKED, event -> {
            int row = RandomUtils.nextInt(0, BOARD_HEIGHT);
            int column = event.getColumn();
            boardControl.displayTokenWithAnimation(row, column, randomTokenColor());
        });
        boardStackPane.getChildren().add(boardControl);
        StackPane.setAlignment(boardControl, Pos.CENTER);
        return boardStackPane;
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

    private void startGame() {
        Player player1 = new RandomPlayer();
        Player player2 = new RandomPlayer();
        GameRunner gameRunner = new DefaultGameRunner(player1, player2, new GuiGameEventListener());
        gameRunner.startGame();
    }

    private void displayBoard(Board board) {
        boardControl.hideAll();
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                int cellPlayerId = board.getCellPlayerId(row, column);
                if (cellPlayerId == 1) {
                    boardControl.displayToken(row, column, Color.RED);
                } else if (cellPlayerId == 2) {
                    boardControl.displayToken(row, column, Color.DARKORANGE);
                }
            }
        }
    }

    private class GuiPlayer implements Player {
        @Override
        public void gameStarted(int playerId) {
            // Do nothing
        }

        @Override
        public void requestMove(GameContext gameContext) {
            // Wait for a click event? How???
        }

        @Override
        public void gameFinished(GameResult gameResult) {

        }
    }

    private class GuiGameEventListener implements GameEventListener {
        @Override
        public void gameStarted(GameRunner gameRunner, Board board) {
            displayBoard(board);
        }

        @Override
        public void illegalMoveAttempted(GameRunner gameRunner, int playerId, int column, Board board) {

        }

        @Override
        public void moveRequested(GameRunner gameRunner, int playerId, Board board) {
            System.out.println(String.format("Move requested! PlayerId: '%d', thread: '%s'", playerId, Thread.currentThread()));
        }

        @Override
        public void moveMade(GameRunner gameRunner, int playerId, int column, Board board) {
            Color tokenColor;
            if (playerId == 1) {
                tokenColor = Color.RED;
            } else {
                tokenColor = Color.DARKORANGE;
            }
            int heightOfColumn = board.getHeightOfColumn(column);
            int row = heightOfColumn + 1;


            boardControl.displayTokenWithAnimation(row, column, tokenColor);

            ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();
            consoleBoardVisualizer.visualize(board);
            System.out.println(String.format("Move made! PlayerId: '%d', column: '%d', row: '%d', thread: '%s'", playerId, column, row, Thread.currentThread()));

            System.out.println("enterNestedEventLoop");
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> Platform.exitNestedEventLoop("some-key", null));
                System.out.println("exitNestedEventLoop");
            }).start();

            Platform.enterNestedEventLoop("some-key");
            System.out.println("Left nestedEventLoop");
        }

        @Override
        public void gameFinished(GameRunner gameRunner, GameResult gameResult) {
            System.out.println("Game finished: " + gameResult);
        }
    }

}
