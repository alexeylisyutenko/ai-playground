package ru.alexeylisyutenko.ai.connectfour.main.console.gamelistener;

import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.GameEventListener;
import ru.alexeylisyutenko.ai.connectfour.game.GameResult;
import ru.alexeylisyutenko.ai.connectfour.game.GameRunner;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.BoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;

import static ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer.BOARD_SYMBOL_MAPPING;

/**
 * A main event listener which prints main process into console.
 */
public class ConsoleGameEventListener implements GameEventListener {
    private final BoardVisualizer boardVisualizer = new ConsoleBoardVisualizer();

    @Override
    public void gameStarted(GameRunner gameRunner, Board board) {
        boardVisualizer.visualize(board);
        System.out.println();
    }

    @Override
    public void illegalMoveAttempted(GameRunner gameRunner, int playerId, int column, Board board) {
        System.out.println("Illegal move attempted. Please try again.");
    }

    @Override
    public void moveRequested(GameRunner gameRunner, int playerId, Board board) {
        // Do nothing.
    }

    @Override
    public void moveMade(GameRunner gameRunner, int playerId, int column, Board board) {
        System.out.println(String.format("Player %d (%c) puts a token in column %d", board.getOtherPlayerId(), BOARD_SYMBOL_MAPPING[board.getOtherPlayerId()], column));
        System.out.println();
        boardVisualizer.visualize(board);
        System.out.println();
    }

    @Override
    public void gameFinished(GameRunner gameRunner, GameResult gameResult) {
        System.out.println();
        if (gameResult.getType() == GameResult.Type.NORMAL_VICTORY || gameResult.getType() == GameResult.Type.TIMEOUT_VICTORY) {
            System.out.println(String.format("Win for player %d (%c)!", gameResult.getWinnerId(), BOARD_SYMBOL_MAPPING[gameResult.getWinnerId()]));
        } else if (gameResult.getType() == GameResult.Type.TIE) {
            System.out.println("It's a tie! No winner is declared.");
        } else {
            System.out.println("Game was stopped!");
        }
    }
}
