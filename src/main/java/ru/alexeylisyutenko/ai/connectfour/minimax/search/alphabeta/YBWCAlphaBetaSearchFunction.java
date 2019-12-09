package ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta;

import lombok.AllArgsConstructor;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;

import java.util.concurrent.RecursiveTask;

/**
 * Parallel version of the AlphaBeta Pruning algorithm which uses "Young Brothers Wait Concept" to parallelize the
 * algorithm.
 */
public class YBWCAlphaBetaSearchFunction implements SearchFunction {
    @Override
    public Move search(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            throw new IllegalStateException("Search function was called on a terminal node");
        }

        // How to parallelize? Young Brothers wait concept?

        return null;
    }

    @AllArgsConstructor
    private static class BoardValueSearchRecursiveTask extends RecursiveTask<Integer> {
        private final Board board;
        private final int depth;
        private final EvaluationFunction evaluationFunction;

        @Override
        protected Integer compute() {
//            if (MinimaxHelper.isTerminal(depth, board)) {
//                return evaluationFunction.evaluate(board);
//            }
//
//            List<Pair<Integer, Board>> nextMoves = MinimaxHelper.getAllNextMoves(board);
//            List<BoardValueSearchRecursiveTask> subtasks = createRecursiveTasks(nextMoves, depth, evaluationFunction);
//
//            for (BoardValueSearchRecursiveTask subtask : subtasks) {
//                subtask.fork();
//            }
//
//            int bestScore = Integer.MIN_VALUE;
//            for (BoardValueSearchRecursiveTask subtask : subtasks) {
//                int score = -1 * subtask.join();
//                if (score > bestScore) {
//                    bestScore = score;
//                }
//            }
//
//            return bestScore;
            return 0;
        }
    }
}
