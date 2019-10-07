package ru.alexeylisyutenko.ai.connectfour.console.gamelistener;

import ru.alexeylisyutenko.ai.connectfour.Board;
import ru.alexeylisyutenko.ai.connectfour.runner.GameEventListener;
import ru.alexeylisyutenko.ai.connectfour.runner.GameResult;
import ru.alexeylisyutenko.ai.connectfour.runner.GameRunner;
import ru.alexeylisyutenko.ai.connectfour.console.visualizer.BoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.console.visualizer.ConsoleBoardVisualizer;

/**
 * A game event listener which prints game process into console.
 */
public class ConsoleGameEventListener implements GameEventListener {
    private final BoardVisualizer boardVisualizer = new ConsoleBoardVisualizer();

    @Override
    public void gameStarted(GameRunner gameRunner, Board board) {
        boardVisualizer.visualize(board);
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
        System.out.println();
        System.out.println(String.format("Player %d puts a token in column %d", board.getOtherPlayerId(), column));
        System.out.println();
        boardVisualizer.visualize(board);
    }

    @Override
    public void gameFinished(GameRunner gameRunner, GameResult gameResult) {
        System.out.println();
        if (gameResult.getType() == GameResult.Type.NORMAL_VICTORY || gameResult.getType() == GameResult.Type.TIMEOUT_VICTORY) {
            System.out.println(String.format("Win for player %d!", gameResult.getWinnerId()));
        } else if (gameResult.getType() == GameResult.Type.TIE) {
            System.out.println("It's a tie! No winner is declared.");
        } else {
            System.out.println("Game was stopped!");
        }
    }
}
