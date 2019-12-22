package ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.util.Constants.NEGATIVE_INFINITY;
import static ru.alexeylisyutenko.ai.connectfour.util.Constants.POSITIVE_INFINITY;

/**
 * Parallel version of the AlphaBeta Pruning algorithm which uses "Young Brothers Wait Concept" to parallelize the
 * algorithm.
 */
public class YBWCAlphaBetaSearchFunction implements SearchFunction {

    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    @Override
    public Move search(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            throw new IllegalStateException("Search function was called on a terminal node");
        }

        // Generate all possible next moves.
        List<Pair<Integer, Board>> nextMoves = MinimaxHelper.getAllNextMoves(board);

        // We need to invoke only the first move.
        Pair<Integer, Board> youngBrotherMove = nextMoves.get(0);
        BoardValueSearchRecursiveTask youngBrotherRecursiveTask = new BoardValueSearchRecursiveTask(
                youngBrotherMove.getRight(), depth -1, -POSITIVE_INFINITY, -NEGATIVE_INFINITY, evaluationFunction);

        // Receive a score for the youngest brother and since it's the first score we get save it as the best score.
        int bestScore = -1 * forkJoinPool.invoke(youngBrotherRecursiveTask);
        int bestColumn = youngBrotherMove.getLeft();

        // Fire all the other tasks concurrently for older brothers.
        ArrayList<BoardValueSearchRecursiveTask> recursiveTasks = new ArrayList<>(BOARD_WIDTH);
        for (int i = 1; i < nextMoves.size(); i++) {
            Pair<Integer, Board> nextMove = nextMoves.get(i);
            BoardValueSearchRecursiveTask recursiveTask = new BoardValueSearchRecursiveTask(
                    nextMove.getRight(), depth -1, -POSITIVE_INFINITY, -bestScore, evaluationFunction);
            forkJoinPool.submit(recursiveTask);
            recursiveTasks.add(recursiveTask);
        }

        // Prepare list of columns for each move apart from the first one.
        List<Integer> moveColumns = nextMoves.stream()
                .skip(1)
                .map(Pair::getLeft)
                .collect(Collectors.toList());

        // Receive scores for older brothers and find the best move.
        for (int i = 0; i < recursiveTasks.size(); i++) {
            int score = -1 * recursiveTasks.get(i).join();
            if (score > bestScore) {
                bestScore = score;
                bestColumn = moveColumns.get(i);
            }
        }

        return new Move(bestColumn, bestScore);
    }


    @AllArgsConstructor
    private static class BoardValueSearchRecursiveTask extends RecursiveTask<Integer> {
        private final Board board;
        private final int depth;
        private final int alpha;
        private final int beta;
        private final EvaluationFunction evaluationFunction;

        @Override
        protected Integer compute() {
            if (MinimaxHelper.isTerminal(depth, board)) {
                return evaluationFunction.evaluate(board);
            }

            int currentAlpha = alpha;
            int currentNodeScore;

            // Create an iterator for generating all possible next moves.
            Iterator<Pair<Integer, Board>> nextMovesIterator = MinimaxHelper.getAllNextMovesIterator(board);

            // Get a score for a younger brother synchronously.
            if (!nextMovesIterator.hasNext()) {
                throw new IllegalStateException("There are no moves for this board");
            }
            Pair<Integer, Board> youngBrotherMove = nextMovesIterator.next();
            BoardValueSearchRecursiveTask youngBrotherRecursiveTask = new BoardValueSearchRecursiveTask(
                    youngBrotherMove.getRight(), depth -1, -beta, -currentAlpha, evaluationFunction);
            currentNodeScore = -1 * youngBrotherRecursiveTask.compute();
            if (currentNodeScore >= beta) {
                return currentNodeScore;
            }
            if (currentNodeScore > currentAlpha) {
                currentAlpha = currentNodeScore;
            }

            // We can't prune, so we need to create recursive tasks for older brothers.
            ArrayList<BoardValueSearchRecursiveTask> recursiveTasks = new ArrayList<>(BOARD_WIDTH);
            while (nextMovesIterator.hasNext()) {
                Pair<Integer, Board> nextMove = nextMovesIterator.next();
                BoardValueSearchRecursiveTask recursiveTask = new BoardValueSearchRecursiveTask(
                        nextMove.getRight(), depth -1, -beta, -currentAlpha, evaluationFunction);
                recursiveTask.fork();
                recursiveTasks.add(recursiveTask);
            }

            // Process the results of the older brothers and try to cancel forked tasks if we're in pruning situation.
            boolean pruning = false;
            for (BoardValueSearchRecursiveTask task : recursiveTasks) {
                if (!pruning) {
                    int score = -1 * task.join();
                    if (score > currentNodeScore) {
                        currentNodeScore = score;
                    }
                    if (score >= beta) {
                        pruning = true;
                    }
                    if (score > currentAlpha) {
                        currentAlpha = score;
                    }
                } else {
                    task.cancel(true);
                }
            }

            return currentNodeScore;
        }
    }

}
