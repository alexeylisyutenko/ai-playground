package ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MultithreadedMinimaxSearchFunction;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class MultithreadedAlphaBetaSearchFunction implements SearchFunction {
    @Override
    public Move search(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            throw new IllegalStateException("Search function was called on a terminal node");
        }

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
