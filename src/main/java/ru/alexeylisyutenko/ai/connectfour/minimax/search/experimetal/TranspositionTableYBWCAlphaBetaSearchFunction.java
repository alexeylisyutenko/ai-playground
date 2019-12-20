package ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.util.Constants.NEGATIVE_INFINITY;
import static ru.alexeylisyutenko.ai.connectfour.util.Constants.POSITIVE_INFINITY;

// TODO: Refactor the code.

public class TranspositionTableYBWCAlphaBetaSearchFunction implements SearchFunction {

    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    private final ConcurrentMap<Board, TranspositionTableEntry> transpositionTable = new ConcurrentHashMap<>();

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
                youngBrotherMove.getRight(), depth -1, -POSITIVE_INFINITY, -NEGATIVE_INFINITY, evaluationFunction, transpositionTable);

        // Receive a score for the youngest brother and since it's the first score we get save it as the best score.
        int bestScore = -1 * forkJoinPool.invoke(youngBrotherRecursiveTask);
        int bestColumn = youngBrotherMove.getLeft();

        // Fire all the other tasks concurrently for older brothers.
        ArrayList<BoardValueSearchRecursiveTask> recursiveTasks = new ArrayList<>(BOARD_WIDTH);
        for (int i = 1; i < nextMoves.size(); i++) {
            Pair<Integer, Board> nextMove = nextMoves.get(i);
            BoardValueSearchRecursiveTask recursiveTask = new BoardValueSearchRecursiveTask(
                    nextMove.getRight(), depth -1, -POSITIVE_INFINITY, -bestScore, evaluationFunction, transpositionTable);
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
        private final ConcurrentMap<Board, TranspositionTableEntry> transpositionTable;

        @Override
        protected Integer compute() {
            if (MinimaxHelper.isTerminal(depth, board)) {
                return evaluationFunction.evaluate(board);
            }

            int currentAlpha = alpha;
            int currentBeta = beta;

            TranspositionTableEntry transpositionTableEntry = transpositionTable.get(board);
            if (transpositionTableEntry != null && transpositionTableEntry.getDepth() >= depth) {
                switch (transpositionTableEntry.getType()) {
                    case EXACT_VALUE:
                        return transpositionTableEntry.getValue();
                    case UPPER_BOUND:
                        currentBeta = Math.min(currentBeta, transpositionTableEntry.getValue());
                        break;
                    case LOWER_BOUND:
                        currentAlpha = Math.max(currentAlpha, transpositionTableEntry.getValue());
                        break;
                }

                if (currentAlpha >= currentBeta) {
                    return transpositionTableEntry.getValue();
                }
            }

            // Create an iterator for generating all possible next moves.
            Iterator<Pair<Integer, Board>> nextMovesIterator = MinimaxHelper.getAllNextMovesIterator(board);

            // Get a score for a younger brother synchronously.
            int value = NEGATIVE_INFINITY;
            if (!nextMovesIterator.hasNext()) {
                throw new IllegalStateException("There are no moves for this board");
            }
            Pair<Integer, Board> youngBrotherMove = nextMovesIterator.next();
            BoardValueSearchRecursiveTask youngBrotherRecursiveTask = new BoardValueSearchRecursiveTask(
                    youngBrotherMove.getRight(), depth -1, -currentBeta, -currentAlpha, evaluationFunction, transpositionTable);
            int score = -1 * youngBrotherRecursiveTask.compute();
            if (score > value) {
                value = score;
            }
            currentAlpha = Math.max(currentAlpha, value);
            if (currentAlpha >= currentBeta) {
                saveTranspositionTableEntry(board, depth, value, alpha, currentBeta);
                return value;
            }

            // We can't prune, so we need to create recursive tasks for older brothers.
            ArrayList<BoardValueSearchRecursiveTask> recursiveTasks = new ArrayList<>(BOARD_WIDTH);
            while (nextMovesIterator.hasNext()) {
                Pair<Integer, Board> nextMove = nextMovesIterator.next();
                BoardValueSearchRecursiveTask recursiveTask = new BoardValueSearchRecursiveTask(
                        nextMove.getRight(), depth -1, -currentBeta, -currentAlpha, evaluationFunction, transpositionTable);
                recursiveTask.fork();
                recursiveTasks.add(recursiveTask);
            }

            // Process the results of the older brothers and try to cancel forked tasks if we're in pruning situation.
            boolean pruning = false;
            for (BoardValueSearchRecursiveTask task : recursiveTasks) {
                if (!pruning) {

                    // TODO: if here is an exception, all recursiveTasks must be canceled.
                    score = -1 * task.join();

                    if (score > value) {
                        value = score;
                    }
                    currentAlpha = Math.max(currentAlpha, value);
                    if (currentAlpha >= currentBeta) {
                        pruning = true;
                    }
                } else {
                    task.cancel(true);
                }
            }

            saveTranspositionTableEntry(board, depth, value, alpha, currentBeta);
            return value;
        }

        private void saveTranspositionTableEntry(Board board, int depth, int value, int originalAlpha, int beta) {
            TranspositionTableEntry entry;
            if (value <= originalAlpha) {
                entry = new TranspositionTableEntry(depth, TranspositionTableEntryType.UPPER_BOUND, value);
            } else if (value >= beta) {
                entry = new TranspositionTableEntry(depth, TranspositionTableEntryType.LOWER_BOUND, value);
            } else {
                entry = new TranspositionTableEntry(depth, TranspositionTableEntryType.EXACT_VALUE, value);
            }
            transpositionTable.put(board, entry);
        }

    }

    private enum TranspositionTableEntryType {
        EXACT_VALUE, UPPER_BOUND, LOWER_BOUND;
    }

    @Value
    private static class TranspositionTableEntry {
        private final int depth;
        private final TranspositionTableEntryType type;
        private final int value;
    }

}
