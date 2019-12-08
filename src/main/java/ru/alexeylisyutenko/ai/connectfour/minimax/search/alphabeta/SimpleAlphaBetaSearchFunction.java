package ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;

import java.util.Iterator;
import java.util.List;

public class SimpleAlphaBetaSearchFunction implements SearchFunction {
    @Override
    public Move search(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            throw new IllegalStateException("Search function was called on a terminal node");
        }

        List<Pair<Integer, Board>> nextMoves = MinimaxHelper.getAllNextMoves(board);
        int bestColumn = Integer.MIN_VALUE;
        int bestScore = Integer.MIN_VALUE + 1;

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

            int score = findMaxValue(nextMove.getRight(), depth - 1, alpha, beta, evaluationFunction);
            if (score < currentNodeScore) {
                currentNodeScore = score;
            }
            if (score <= alpha) {
                return currentNodeScore;
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

            int score = findMinValue(nextMove.getRight(), depth - 1, alpha, beta, evaluationFunction);
            if (score > currentNodeScore) {
                currentNodeScore = score;
            }
            if (score >= beta) {
                return currentNodeScore;
            }
            if (score > alpha) {
                alpha = score;
            }
        }

        return currentNodeScore;
    }
}
