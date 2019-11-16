package ru.alexeylisyutenko.ai.connectfour.minimax;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.util.ArrayList;
import java.util.List;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

// TODO: Test this class.
// TODO: Do we need a version of getAllNextMoves() method that returns Iterator<Board>?

/**
 * Helper methods used in minimax algorithms.
 */
public final class MinimaxHelper {
    private MinimaxHelper() {
    }

    /**
     * Returns all possible moves that the current player could take from this position.
     *
     * @param board current position board
     * @return list of all possible moves from this position
     */
    public static List<Pair<Integer, Board>> getAllNextMoves(Board board) {
        ArrayList<Pair<Integer, Board>> moves = new ArrayList<>();
        for (int column = 0; column < BOARD_WIDTH; column++) {
            try {
                Board boardAfterMove = board.makeMove(column);
                moves.add(Pair.of(column, boardAfterMove));
            } catch (InvalidMoveException ignored) {
            }
        }
        return moves;
    }

    /**
     * Generic terminal state check.
     *
     * @param depth current depth in the search tree
     * @param board current board
     * @return true when maximum depth is reached or the game has ended, otherwise false
     */
    public static boolean isTerminal(int depth, Board board) {
        return depth <= 0 || board.isGameOver();
    }
}
