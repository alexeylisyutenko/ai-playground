package ru.alexeylisyutenko.ai.connectfour.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.apache.commons.lang3.RandomUtils;
import ru.alexeylisyutenko.ai.connectfour.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.game.*;
import ru.alexeylisyutenko.ai.connectfour.player.RandomPlayer;

import javax.swing.*;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class JavaFxGame extends Application {
    public static final Color PLAYER_1_TOKEN_COLOR = Color.DARKORANGE;
    public static final Color PLAYER_2_TOKEN_COLOR = Color.RED;
    public static final int AFTER_MOVE_DELAY_MS = 300;

    private GameRunner gameRunner;

    private BoardControl boardControl;
    private Button startGameButton;
    private Button stopGameButton;
    private Label gameStateLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Player player1 = new GuiPlayer();
        Player player2 = new RandomPlayer();
        gameRunner = new DefaultGameRunner(player1, player2, new GuiGameEventListener());

        primaryStage.setTitle("Connect Four Game");
        primaryStage.setScene(new Scene(createContent(), BoardControl.SCENE_WIDTH, BoardControl.SCENE_HEIGHT));
        startGameButton.setOnMouseClicked(event -> gameRunner.startGame());
        stopGameButton.setOnMouseClicked(event -> gameRunner.stopGame());
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        gameRunner.shutdown();
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
        vBox.setPadding(new Insets(10));
        return vBox;
    }

    private Parent createGameArea() {
        StackPane boardStackPane = new StackPane();

        boardControl = new BoardControl();
        boardStackPane.getChildren().add(boardControl);
        StackPane.setAlignment(boardControl, Pos.CENTER);

        gameStateLabel = new Label("Press 'Start Game' to start.");
        gameStateLabel.setFont(new Font(24));
        gameStateLabel.setPadding(new Insets(10));
        boardStackPane.getChildren().add(gameStateLabel);
        StackPane.setAlignment(gameStateLabel, Pos.TOP_CENTER);

        return boardStackPane;
    }

    private void displayBoard(Board board) {
        boardControl.hideAll();
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                int cellPlayerId = board.getCellPlayerId(row, column);
                if (cellPlayerId != 0) {
                    Color tokenColor = getTokenColorByPlayerId(cellPlayerId);
                    boardControl.displayToken(row, column, tokenColor);
                }
            }
        }
    }

    private Color getTokenColorByPlayerId(int playerId) {
        if (playerId == 1) {
            return PLAYER_1_TOKEN_COLOR;
        } else {
            return PLAYER_2_TOKEN_COLOR;
        }
    }

    /**
     *
     */
    private class GuiPlayer implements Player {
        private EventHandler<BoardControl.ColumnClickEvent> eventHandler;

        @Override
        public void gameStarted(int playerId) {
            // Do nothing
        }

        @Override
        public void requestMove(GameContext gameContext) {
            eventHandler = event -> {
                int column = event.getColumn();
                gameContext.makeMove(column);
                removeBoardControlColumnClickedEventHandler();
            };
            boardControl.addEventHandler(BoardControl.ColumnClickEvent.COLUMN_CLICKED, eventHandler);
        }

        private void removeBoardControlColumnClickedEventHandler() {
            if (eventHandler != null) {
                boardControl.removeEventHandler(BoardControl.ColumnClickEvent.COLUMN_CLICKED, eventHandler);
                eventHandler = null;
            }
        }

        @Override
        public void gameFinished(GameResult gameResult) {
            removeBoardControlColumnClickedEventHandler();
        }
    }

    /**
     *
     */
    private class GuiGameEventListener implements GameEventListener {
        @Override
        public void gameStarted(GameRunner gameRunner, Board board) {
            Platform.runLater(() -> displayBoard(board));
        }

        @Override
        public void illegalMoveAttempted(GameRunner gameRunner, int playerId, int column, Board board) {
            // Do nothing.
        }

        @Override
        public void moveRequested(GameRunner gameRunner, int playerId, Board board) {
            Platform.runLater(() -> gameStateLabel.setText(String.format("Player %d's move.", playerId)));
        }

        @Override
        public void moveMade(GameRunner gameRunner, int playerId, int column, Board board) {
            int row = board.getHeightOfColumn(column) + 1;
            Color tokenColor = getTokenColorByPlayerId(playerId);
            Platform.runLater(() -> boardControl.displayTokenWithAnimation(row, column, tokenColor));
            silentSleep(AFTER_MOVE_DELAY_MS);
        }

        @Override
        public void gameFinished(GameRunner gameRunner, GameResult gameResult) {
            String gameStateLabelText;
            switch (gameResult.getType()) {
                case NORMAL_VICTORY:
                    gameStateLabelText = String.format("Win for player %d (%s)!", gameResult.getWinnerId(), getPlayerColorString(gameResult.getWinnerId()));
                    break;
                case TIMEOUT_VICTORY:
                    gameStateLabelText = String.format("Win for player %d (%s) because of timeout!", gameResult.getWinnerId(), getPlayerColorString(gameResult.getWinnerId()));
                    break;
                case TIE:
                    gameStateLabelText = "It's a tie! No winner is declared.";
                    break;
                case STOPPED_GAME:
                    gameStateLabelText = "Game was stopped by user.";
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + gameResult.getType());
            }
            Platform.runLater(() -> gameStateLabel.setText(gameStateLabelText));
        }

        private String getPlayerColorString(int playerId) {
            if (playerId == 1) {
                return "yellow";
            } else {
                return "red";
            }
        }

        private void silentSleep(long millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                // Do nothing.
            }
        }
    }

}
