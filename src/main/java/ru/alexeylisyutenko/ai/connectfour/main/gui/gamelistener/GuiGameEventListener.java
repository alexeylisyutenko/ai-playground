package ru.alexeylisyutenko.ai.connectfour.main.gui.gamelistener;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.GameEventListener;
import ru.alexeylisyutenko.ai.connectfour.game.GameResult;
import ru.alexeylisyutenko.ai.connectfour.game.GameRunner;
import ru.alexeylisyutenko.ai.connectfour.main.gui.boardcontrol.BoardControl;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.main.gui.GameConstants.*;

public class GuiGameEventListener implements GameEventListener {
    private final BoardControl boardControl;
    private final Label gameStateLabel;

    public GuiGameEventListener(BoardControl boardControl, Label gameStateLabel) {
        this.boardControl = boardControl;
        this.gameStateLabel = gameStateLabel;
    }

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
