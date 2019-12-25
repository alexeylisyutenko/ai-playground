package ru.alexeylisyutenko.ai.connectfour.main.gui.gamelistener;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;
import ru.alexeylisyutenko.ai.connectfour.game.*;
import ru.alexeylisyutenko.ai.connectfour.main.gui.boardcontrol.BoardControl;

import java.util.Comparator;
import java.util.List;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.main.gui.GameConstants.*;

public class GuiGameEventListener implements GameEventListener {
    private final BoardControl boardControl;
    private final Label gameStateLabel;
    private final Label timeLabel;
    private final Label movesLabel;
    private final Timeline timeline;

    private volatile long currentMoveStartMillis;

    public GuiGameEventListener(BoardControl boardControl, Label gameStateLabel, Label timeLabel, Label movesLabel) {
        this.boardControl = boardControl;
        this.gameStateLabel = gameStateLabel;
        this.timeLabel = timeLabel;
        this.movesLabel = movesLabel;
        this.timeline = createTimeline(timeLabel);
    }

    private Timeline createTimeline(Label timeLabel) {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(1),
                event -> {
                    String duration = DurationFormatUtils.formatDuration(System.currentTimeMillis() - currentMoveStartMillis, "mm:ss");
                    timeLabel.setText("Move time: " + duration);
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }

    @Override
    public void gameStarted(GameRunner gameRunner, Board board) {
        Platform.runLater(() -> displayBoard(board));
        updateMovesLabel(0);
    }

    @Override
    public void illegalMoveAttempted(GameRunner gameRunner, int playerId, int column, Board board) {
        // Do nothing.
    }

    @Override
    public void moveRequested(GameRunner gameRunner, int playerId, Board board) {
        Platform.runLater(() -> gameStateLabel.setText(String.format("Player %d's move.", playerId)));
        Platform.runLater(() -> timeLabel.setText("Move time: 00:00"));
        startCurrentMoveTimeUpdates();
    }

    private void updateMovesLabel(int moves) {
        Platform.runLater(() -> movesLabel.setText("Moves: " + moves));
    }

    private void startCurrentMoveTimeUpdates() {
        currentMoveStartMillis = System.currentTimeMillis();
        timeline.play();
    }

    private void stopCurrentMoveTimeUpdates() {
        timeline.stop();
    }

    @Override
    public void moveMade(GameRunner gameRunner, int playerId, int column, Board board) {
        int row = board.getHeightOfColumn(column) + 1;
        Color tokenColor = getTokenColorByPlayerId(playerId);
        Platform.runLater(() -> boardControl.displayTokenWithAnimation(row, column, tokenColor));
        silentSleep(AFTER_MOVE_DELAY_MS);
        stopCurrentMoveTimeUpdates();
        updateMovesLabel(board.getNumberOfTokensOnBoard());
    }

    @Override
    public void gameFinished(GameRunner gameRunner, GameResult gameResult, Board board) {
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

        if (gameResult.getType() == GameResult.Type.NORMAL_VICTORY) {
            List<Cell> winningChain = board.getChainCells(board.getWinnerId()).stream()
                    .max(Comparator.comparing(List::size))
                    .orElseThrow();

            Platform.runLater(() -> {
                for (Cell cell : winningChain) {
                    boardControl.displayCross(cell.getRow(), cell.getColumn());
                }
            });
        }

        stopCurrentMoveTimeUpdates();
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
