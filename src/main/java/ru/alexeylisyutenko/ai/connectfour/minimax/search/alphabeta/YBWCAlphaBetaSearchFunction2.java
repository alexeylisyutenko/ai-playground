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

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.util.Constants.NEGATIVE_INFINITY;
import static ru.alexeylisyutenko.ai.connectfour.util.Constants.POSITIVE_INFINITY;

// TODO: Test and Refactor!

/**
 * Parallel version of the AlphaBeta Pruning algorithm which uses "Young Brothers Wait Concept" to parallelize the
 * algorithm.
 */
public class YBWCAlphaBetaSearchFunction2 implements SearchFunction {

    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    @Override
    public Move search(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            throw new IllegalStateException("Search function was called on a terminal node");
        }

        List<Pair<Integer, Board>> nextMoves = MinimaxHelper.getAllNextMoves(board);
        int bestColumn = NEGATIVE_INFINITY;
        int bestScore = NEGATIVE_INFINITY;
        for (Pair<Integer, Board> nextMove : nextMoves) {
            BoardValueSearchRecursiveTask recursiveTask = new BoardValueSearchRecursiveTask(
                    nextMove.getRight(), depth -1, -POSITIVE_INFINITY, -bestScore, evaluationFunction);
            int score = -1 * forkJoinPool.invoke(recursiveTask);
            if (score > bestScore) {
                bestScore = score;
                bestColumn = nextMove.getLeft();
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

            Iterator<Pair<Integer, Board>> nextMovesIterator = MinimaxHelper.getAllNextMovesIterator(board);

            // Get a score for a younger brother.
            if (!nextMovesIterator.hasNext()) {
                throw new IllegalStateException("There are no moves for this board");
            }
            Pair<Integer, Board> youngBrotherMove = nextMovesIterator.next();
            BoardValueSearchRecursiveTask recursiveTask =
                    new BoardValueSearchRecursiveTask(youngBrotherMove.getRight(), depth -1, -beta, -currentAlpha, evaluationFunction);
            currentNodeScore = -1 * recursiveTask.compute();
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
                recursiveTask = new BoardValueSearchRecursiveTask(nextMove.getRight(), depth -1, -beta, -currentAlpha, evaluationFunction);
                recursiveTask.fork();
                recursiveTasks.add(recursiveTask);
            }

            for (BoardValueSearchRecursiveTask task : recursiveTasks) {
                int score = -1 * task.join();
                if (score > currentNodeScore) {
                    currentNodeScore = score;
                }
                if (score >= beta) {
                    return currentNodeScore;
                }
                if (score > currentAlpha) {
                    currentAlpha = score;
                }
            }

            return currentNodeScore;
        }
    }

    /*
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
     */
}
