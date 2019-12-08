package ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;

import java.util.Iterator;
import java.util.List;

import static ru.alexeylisyutenko.ai.connectfour.util.Constants.NEGATIVE_INFINITY;
import static ru.alexeylisyutenko.ai.connectfour.util.Constants.POSITIVE_INFINITY;

public class AlphaBetaSearchFunction implements SearchFunction {
    @Override
    public Move search(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            throw new IllegalStateException("Search function was called on a terminal node");
        }

        List<Pair<Integer, Board>> nextMoves = MinimaxHelper.getAllNextMoves(board);
        int bestColumn = NEGATIVE_INFINITY;
        int bestScore = NEGATIVE_INFINITY;

        for (Pair<Integer, Board> nextMove : nextMoves) {
            int score = -findAlphaBetaBoardValue(nextMove.getRight(), depth - 1, -POSITIVE_INFINITY, -bestScore, evaluationFunction);
            if (score > bestScore) {
                bestScore = score;
                bestColumn = nextMove.getLeft();
            }
        }

        return new Move(bestColumn, bestScore);
    }

    private int findAlphaBetaBoardValue(Board board, int depth, int alpha, int beta, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            return evaluationFunction.evaluate(board);
        }

        int currentNodeScore = NEGATIVE_INFINITY;

        Iterator<Pair<Integer, Board>> nextMovesIterator = MinimaxHelper.getAllNextMovesIterator(board);
        while (nextMovesIterator.hasNext()) {
            Pair<Integer, Board> nextMove = nextMovesIterator.next();

            int score = -1 * findAlphaBetaBoardValue(nextMove.getRight(), depth - 1, -beta, -alpha, evaluationFunction);
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
