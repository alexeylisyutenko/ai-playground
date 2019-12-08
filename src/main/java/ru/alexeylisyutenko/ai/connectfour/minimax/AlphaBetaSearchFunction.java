package ru.alexeylisyutenko.ai.connectfour.minimax;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.util.Iterator;
import java.util.List;

public class AlphaBetaSearchFunction implements SearchFunction {
    @Override
    public Move search(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            throw new IllegalStateException("Search function was called on a terminal node");
        }

        List<Pair<Integer, Board>> nextMoves = MinimaxHelper.getAllNextMoves(board);
        int bestColumn = Integer.MIN_VALUE;
        int bestScore = Integer.MIN_VALUE;
        for (Pair<Integer, Board> nextMove : nextMoves) {
            int score = findMinValue(nextMove.getRight(), depth - 1, bestScore, Integer.MAX_VALUE, evaluationFunction);
            if (score > bestScore) {
                bestScore = score;
                bestColumn = nextMove.getLeft();
            }
        }

        return new Move(bestColumn, bestScore);
    }

    private int findMinValue(Board board, int depth, int alpha, int beta, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            return -evaluationFunction.evaluate(board);
        }

        int currentNodeScore = Integer.MAX_VALUE;

        Iterator<Pair<Integer, Board>> nextMovesIterator = MinimaxHelper.getAllNextMovesIterator(board);
        while (nextMovesIterator.hasNext()) {
            Pair<Integer, Board> nextMove = nextMovesIterator.next();

            boolean shouldPrune = currentNodeScore <= alpha;
            if (shouldPrune) {
                return currentNodeScore;
            }

            int score = findMaxValue(nextMove.getRight(), depth - 1, alpha, beta, evaluationFunction);
            if (score < currentNodeScore) {
                currentNodeScore = score;
            }
            if (score < beta) {
                beta = score;
            }
        }

        return currentNodeScore;
    }

    private int findMaxValue(Board board, int depth, int alpha, int beta, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            return evaluationFunction.evaluate(board);
        }

        int currentNodeScore = Integer.MIN_VALUE;

        Iterator<Pair<Integer, Board>> nextMovesIterator = MinimaxHelper.getAllNextMovesIterator(board);
        while (nextMovesIterator.hasNext()) {
            Pair<Integer, Board> nextMove = nextMovesIterator.next();

            boolean shouldPrune = currentNodeScore >= beta;
            if (shouldPrune) {
                return currentNodeScore;
            }

            int score = findMinValue(nextMove.getRight(), depth - 1, alpha, beta, evaluationFunction);
            if (score > currentNodeScore) {
                currentNodeScore = score;
            }
            if (score > alpha) {
                alpha = score;
            }
        }

        return currentNodeScore;
    }


//    private int findAlphaBetaBoardValue(Board board, int depth, int alpha, int beta, EvaluationFunction evaluationFunction) {
//        int currentNodeValue = Integer.MIN_VALUE;
//        return 0;
//    }

    /*
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
     */
}
