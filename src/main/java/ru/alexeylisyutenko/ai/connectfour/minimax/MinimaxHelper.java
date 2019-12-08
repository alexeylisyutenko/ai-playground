package ru.alexeylisyutenko.ai.connectfour.minimax;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

// TODO: Test this class.

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
     * Returns an iterator which iterates over all possible moves that the current player could take from this position.
     *
     * @param board current position board
     * @return all possible move iterator
     */
    public static Iterator<Pair<Integer, Board>> getAllNextMovesIterator(Board board) {
        return new AllNextMovesIterator(board);
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

    /**
     * Iterator which iterates over all possible moves that the current player could take from this position
     */
    private static class AllNextMovesIterator implements Iterator<Pair<Integer, Board>> {
        private final Board board;
        private int column;

        public AllNextMovesIterator(Board board) {
            this.board = board;
            this.column = 0;
        }

        @Override
        public boolean hasNext() {
            while (column < BOARD_WIDTH && board.getHeightOfColumn(column) == -1) {
                column++;
            }
            return column != BOARD_WIDTH;
        }

        @Override
        public Pair<Integer, Board> next() {
            Pair<Integer, Board> nextMove = Pair.of(column, board.makeMove(column));
            column++;
            return nextMove;
        }
    }
}
