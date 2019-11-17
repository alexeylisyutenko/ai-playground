package ru.alexeylisyutenko.ai.connectfour.minimax;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class MultithreadedMinimaxSearchFunction implements SearchFunction {
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    private static List<BoardValueSearchRecursiveTask> createRecursiveTasks(List<Pair<Integer, Board>> nextMoves, int depth, EvaluationFunction evaluationFunction) {
        return nextMoves.stream()
                .map(Pair::getRight)
                .map(currentBoard -> new BoardValueSearchRecursiveTask(currentBoard, depth - 1, evaluationFunction))
                .collect(Collectors.toList());
    }

    @Override
    public BestMove search(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            throw new IllegalStateException("Search function was called on a terminal node");
        }

        List<Pair<Integer, Board>> nextMoves = MinimaxHelper.getAllNextMoves(board);
        List<BoardValueSearchRecursiveTask> recursiveTasks = createRecursiveTasks(nextMoves, depth, evaluationFunction);

        int bestColumn = Integer.MIN_VALUE;
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < recursiveTasks.size(); i++) {
            int score = -1 * forkJoinPool.invoke(recursiveTasks.get(i));
            if (score > bestScore) {
                bestScore = score;
                bestColumn = nextMoves.get(i).getLeft();
            }
        }

        return new BestMove(bestColumn, bestScore);
    }

    @AllArgsConstructor
    private static class BoardValueSearchRecursiveTask extends RecursiveTask<Integer> {
        private final Board board;
        private final int depth;
        private final EvaluationFunction evaluationFunction;

        @Override
        protected Integer compute() {
            if (MinimaxHelper.isTerminal(depth, board)) {
                return evaluationFunction.evaluate(board);
            }

            List<Pair<Integer, Board>> nextMoves = MinimaxHelper.getAllNextMoves(board);
            List<BoardValueSearchRecursiveTask> subtasks = createRecursiveTasks(nextMoves, depth, evaluationFunction);

            for (BoardValueSearchRecursiveTask subtask : subtasks) {
                subtask.fork();
            }

            int bestScore = Integer.MIN_VALUE;
            for (BoardValueSearchRecursiveTask subtask : subtasks) {
                int score = -1 * subtask.join();
                if (score > bestScore) {
                    bestScore = score;
                }
            }

            return bestScore;
        }
    }

}
