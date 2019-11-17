package ru.alexeylisyutenko.ai.connectfour.minimax;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.util.List;

public class MinimaxSearchFunction implements SearchFunction {
    @Override
    public Move search(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            throw new IllegalStateException("Search function was called on a terminal node");
        }

        List<Pair<Integer, Board>> nextMoves = MinimaxHelper.getAllNextMoves(board);
        int bestColumn = Integer.MIN_VALUE;
        int bestScore = Integer.MIN_VALUE;
        for (Pair<Integer, Board> nextMove : nextMoves) {
            int score = -1 * findMinimaxBoardValue(nextMove.getRight(), depth - 1, evaluationFunction);
            if (score > bestScore) {
                bestScore = score;
                bestColumn = nextMove.getLeft();
            }
        }

        return new Move(bestColumn, bestScore);
    }

    /**
     * Estimates a minimax value of a particular board given a particular depth to estimate to.
     */
    private int findMinimaxBoardValue(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            return evaluationFunction.evaluate(board);
        }

        List<Pair<Integer, Board>> nextMoves = MinimaxHelper.getAllNextMoves(board);
        int bestScore = Integer.MIN_VALUE;
        for (Pair<Integer, Board> nextMove : nextMoves) {
            int score = -1 * findMinimaxBoardValue(nextMove.getRight(), depth - 1, evaluationFunction);
            if (score > bestScore) {
                bestScore = score;
            }
        }

        return bestScore;
    }
}
