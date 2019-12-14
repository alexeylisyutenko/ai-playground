package ru.alexeylisyutenko.ai.connectfour.minimax.search.plain;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class MultithreadedMinimaxSearchFunction implements SearchFunction {
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    private static List<BoardValueSearchRecursiveTask> createRecursiveTasks(List<Pair<Integer, Board>> nextMoves, int depth, EvaluationFunction evaluationFunction) {
        return nextMoves.stream()
                .map(Pair::getRight)
                .map(currentBoard -> new BoardValueSearchRecursiveTask(currentBoard, depth - 1, evaluationFunction))
                .collect(Collectors.toList());
    }

    @Override
    public Move search(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            throw new IllegalStateException("Search function was called on a terminal node");
        }

        List<Pair<Integer, Board>> nextMoves = MinimaxHelper.getAllNextMoves(board);
        List<BoardValueSearchRecursiveTask> recursiveTasks = createRecursiveTasks(nextMoves, depth, evaluationFunction);

        recursiveTasks.forEach(forkJoinPool::submit);

        List<Integer> scores = recursiveTasks.stream()
                .map(recursiveTask -> -recursiveTask.join())
                .collect(Collectors.toList());

        List<Move> moves = constructMoves(nextMoves, scores);
        moves.sort(Comparator.comparing(Move::getScore).reversed());

        // Find equal score boundary.
        int equalScoreHigh = 1;
        while (equalScoreHigh < moves.size() &&
                moves.get(equalScoreHigh-1).getScore() == moves.get(equalScoreHigh).getScore()) {
            equalScoreHigh++;
        }

        return moves.get(RandomUtils.nextInt(0, equalScoreHigh));
    }

    private List<Move> constructMoves(List<Pair<Integer, Board>> nextMoves, List<Integer> scores) {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < nextMoves.size(); i++) {
            Integer column = nextMoves.get(i).getLeft();
            Integer score = scores.get(i);
            moves.add(new Move(column, score));
        }
        return moves;
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
