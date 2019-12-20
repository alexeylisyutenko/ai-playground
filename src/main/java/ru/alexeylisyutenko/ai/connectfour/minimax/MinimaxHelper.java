package ru.alexeylisyutenko.ai.connectfour.minimax;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

// TODO: Test this class.

/**
 * Helper methods used in minimax algorithms.
 */
public final class MinimaxHelper {
    private static final int[] DEFAULT_ORDER = {3, 4, 2, 5, 1, 6, 0};

    private MinimaxHelper() {
    }

    /**
     * Returns all possible moves that the current player could take from this position.
     *
     * @param board current position board
     * @return list of all possible moves from this position
     */
    public static List<Pair<Integer, Board>> getAllNextMoves(Board board) {
        return getAllNextMovesWithOrder(board, DEFAULT_ORDER);
    }

    /**
     * Returns all possible moves that the current player could take from this position.
     * <p>
     * Column indicated by the bestMove argument is used as the first move returned by the method (if such move is possible).
     * </p>
     *
     * @param board current position board
     * @param bestMove best move
     * @return list of all possible moves from this position
     */
    public static List<Pair<Integer, Board>> getAllNextMoves(Board board, int bestMove) {
        return getAllNextMovesWithOrder(board, constructOrder(bestMove));
    }

    private static List<Pair<Integer, Board>> getAllNextMovesWithOrder(Board board, int[] order) {
        ArrayList<Pair<Integer, Board>> moves = new ArrayList<>();
        for (int position = 0; position < BOARD_WIDTH; position++) {
            if (board.getHeightOfColumn(order[position]) != -1) {
                Board boardAfterMove = board.makeMove(order[position]);
                moves.add(Pair.of(order[position], boardAfterMove));
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
        return new ShuffledAllNextMovesIterator(board, DEFAULT_ORDER);
    }


    /**
     * Returns an iterator which iterates over all possible moves that the current player could take from this position.
     * <p>
     * Column indicated by the bestMove argument is used as the first move returned by the iterator (if such move is possible).
     * </p>
     *
     * @param board    current position board
     * @param bestMove best move
     * @return all possible move iterator
     */
    public static Iterator<Pair<Integer, Board>> getAllNextMovesIterator(Board board, int bestMove) {
        Objects.requireNonNull(board, "board cannot be null");
        if (bestMove < 0 || bestMove > BOARD_WIDTH) {
            throw new IllegalArgumentException("Illegal bestMove argument");
        }
        return new ShuffledAllNextMovesIterator(board, constructOrder(bestMove));
    }

    public static int[] constructOrder(int bestMove) {
        int[] order = new int[BOARD_WIDTH];
        order[0] = bestMove;
        int index = 1;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            if (DEFAULT_ORDER[i] != bestMove) {
                order[index] = DEFAULT_ORDER[i];
                index++;
            }
        }
        return order;
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
        private final int[] order;
        private int position;

        public ShuffledAllNextMovesIterator(Board board, int[] order) {
            this.board = board;
            this.order = order;
            this.position = 0;
        }

        @Override
        public boolean hasNext() {
            while (position < BOARD_WIDTH && board.getHeightOfColumn(order[position]) == -1) {
                position++;
            }
            return position != BOARD_WIDTH;
        }

        @Override
        public Pair<Integer, Board> next() {
            Pair<Integer, Board> nextMove = Pair.of(order[position], board.makeMove(order[position]));
            position++;
            return nextMove;
        }
    }
}
