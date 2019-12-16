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

    private static final int[] ORDER = {3, 4, 2, 5, 1, 6, 0};

    /**
     * Returns all possible moves that the current player could take from this position.
     *
     * @param board current position board
     * @return list of all possible moves from this position
     */
    public static List<Pair<Integer, Board>> getAllNextMoves(Board board) {
        ArrayList<Pair<Integer, Board>> moves = new ArrayList<>();
        for (int position = 0; position < BOARD_WIDTH; position++) {
            if (board.getHeightOfColumn(ORDER[position]) != -1) {
                Board boardAfterMove = board.makeMove(ORDER[position]);
                moves.add(Pair.of(ORDER[position], boardAfterMove));
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
        return new ShuffledAllNextMovesIterator(board);
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
    private static class ShuffledAllNextMovesIterator implements Iterator<Pair<Integer, Board>> {
        private final Board board;
        private int position;

        public ShuffledAllNextMovesIterator(Board board) {
            this.board = board;
            this.position = 0;
        }

        @Override
        public boolean hasNext() {
            while (position < BOARD_WIDTH && board.getHeightOfColumn(ORDER[position]) == -1) {
                position++;
            }
            return position != BOARD_WIDTH;
        }

        @Override
        public Pair<Integer, Board> next() {
            Pair<Integer, Board> nextMove = Pair.of(ORDER[position], board.makeMove(ORDER[position]));
            position++;
            return nextMove;
        }
    }
}
