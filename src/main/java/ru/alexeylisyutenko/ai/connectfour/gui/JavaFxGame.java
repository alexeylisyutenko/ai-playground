package ru.alexeylisyutenko.ai.connectfour.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.lang3.RandomUtils;
import ru.alexeylisyutenko.ai.connectfour.game.*;
import ru.alexeylisyutenko.ai.connectfour.player.RandomPlayer;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class JavaFxGame extends Application {

    private BoardControl boardControl;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Connect Four Game");
        primaryStage.setScene(new Scene(createContent(), BoardControl.SCENE_WIDTH, BoardControl.SCENE_HEIGHT));
        primaryStage.show();

        //!!!
        startGame();
    }

    private Parent createContent() {
        StackPane root = new StackPane();
        boardControl = new BoardControl();
//        boardControl.addEventHandler(BoardControl.ColumnClickEvent.COLUMN_CLICKED, event -> {
//            int row = RandomUtils.nextInt(0, BOARD_HEIGHT);
//            int column = event.getColumn();
//            boardControl.displayTokenWithAnimation(row, column, randomTokenColor());
//        });
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
            System.out.println("Move requested: " + playerId);
        }

        @Override
        public void moveMade(GameRunner gameRunner, int playerId, int column, Board board) {
            System.out.println("Move made: playerid = " + playerId + " column = " + column);

            Color tokenColor;
            if (playerId == 1) {
                tokenColor = Color.RED;
            } else {
                tokenColor = Color.DARKORANGE;
            }
            int heightOfColumn = board.getHeightOfColumn(column);
            int row = heightOfColumn - 1;

            displayBoard(board);
//            boardControl.displayToken(row, column, tokenColor);
        }

        @Override
        public void gameFinished(GameRunner gameRunner, GameResult gameResult) {
            System.out.println("Game finished: " + gameResult);
        }
    }

}
