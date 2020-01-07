package ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.StoppableSearchFunction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.util.Constants.NEGATIVE_INFINITY;
import static ru.alexeylisyutenko.ai.connectfour.util.Constants.POSITIVE_INFINITY;

public class NoBestMoveTableTranspositionTableYBWCAlphaBetaSearchFunction implements StoppableSearchFunction {
    private final List<RecursiveTask<?>> topLevelTasks;
    private final ConcurrentMap<Board, TranspositionTableEntry> transpositionTable;
    private final ForkJoinPool forkJoinPool;

    private volatile boolean stopped;

    public NoBestMoveTableTranspositionTableYBWCAlphaBetaSearchFunction() {
        this(new ConcurrentHashMap<>(), ForkJoinPool.commonPool());
    }

    public NoBestMoveTableTranspositionTableYBWCAlphaBetaSearchFunction(ConcurrentMap<Board, TranspositionTableEntry> transpositionTable, ForkJoinPool forkJoinPool) {
        this.topLevelTasks = new CopyOnWriteArrayList<>();
        this.transpositionTable = transpositionTable;
        this.forkJoinPool = forkJoinPool;
        this.stopped = true;
    }

    @Override
    public Move search(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (!stopped) {
            throw new IllegalStateException("Search is already started");
        }
        stopped = false;
        try {
            if (MinimaxHelper.isTerminal(depth, board)) {
                throw new IllegalStateException("Search function was called on a terminal node");
            }

            // Generate all possible next moves.
            List<Pair<Integer, Board>> nextMoves = new ArrayList<>(BOARD_WIDTH);
            getNextMovesOrdered(board, evaluationFunction).forEachRemaining(nextMoves::add);

            // We need to invoke only the first move.
            Pair<Integer, Board> youngBrotherMove = nextMoves.get(0);
            BoardValueSearchRecursiveTask youngBrotherRecursiveTask =
                    createTopLevelTask(youngBrotherMove.getRight(), depth - 1, -POSITIVE_INFINITY, -NEGATIVE_INFINITY, evaluationFunction);

            // Receive a score for the youngest brother and since it's the first score we get save it as the best score.
            int bestScore = -1 * forkJoinPool.invoke(youngBrotherRecursiveTask);
            int bestColumn = youngBrotherMove.getLeft();

            // Fire all the other tasks concurrently for older brothers.
            ArrayList<BoardValueSearchRecursiveTask> recursiveTasks = new ArrayList<>(BOARD_WIDTH);
            for (int i = 1; i < nextMoves.size(); i++) {
                Pair<Integer, Board> nextMove = nextMoves.get(i);
                BoardValueSearchRecursiveTask task = createTopLevelTask(nextMove.getRight(), depth - 1, -POSITIVE_INFINITY, -bestScore, evaluationFunction);
                recursiveTasks.add(task);
                forkJoinPool.submit(task);
            }

            // Save created top level tasks in the list.
            topLevelTasks.addAll(recursiveTasks);

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
        } finally {
            stopped = true;
            topLevelTasks.removeAll(topLevelTasks);
        }
    }

    private BoardValueSearchRecursiveTask createTopLevelTask(Board board, int depth, int alpha, int beta, EvaluationFunction evaluationFunction) {
        if (stopped) {
            throw new IllegalStateException("Failed to create a top level task. Search stop requested.");
        }
        return new BoardValueSearchRecursiveTask(board, depth, alpha, beta, evaluationFunction);
    }

    private void saveTranspositionTableEntry(Board board, int depth, int value, int originalAlpha, int beta) {
        TranspositionTableEntryType type;
        if (value <= originalAlpha) {
            type = TranspositionTableEntryType.UPPER_BOUND;
        } else if (value >= beta) {
            type = TranspositionTableEntryType.LOWER_BOUND;
        } else {
            type = TranspositionTableEntryType.EXACT_VALUE;
        }
        transpositionTable.merge(board, new TranspositionTableEntry(depth, type, value), (entry1, entry2) -> entry1.getDepth() > entry2.getDepth() ? entry1 : entry2);
    }

    private Iterator<Pair<Integer, Board>> getNextMovesOrdered(Board board, EvaluationFunction evaluationFunction) {
        return getNextMovesOrderedByEvaluationFunction(board, evaluationFunction).iterator();
    }

    private List<Pair<Integer, Board>> getNextMovesOrderedByEvaluationFunction(Board board, EvaluationFunction evaluationFunction) {
        return MinimaxHelper.getAllNextMoves(board).stream()
                .map(move -> new ImmutableTriple<>(move.getLeft(), move.getRight(), evaluationFunction.evaluate(move.getRight())))
                .sorted(Comparator.comparing(ImmutableTriple::getRight))
                .map(triple -> Pair.of(triple.getLeft(), triple.getMiddle()))
                .collect(Collectors.toList());
    }

    @Override
    public void stop() {
        stopped = true;
        for (RecursiveTask<?> task : topLevelTasks) {
            task.cancel(true);
        }
    }

    private enum TranspositionTableEntryType {
        EXACT_VALUE, UPPER_BOUND, LOWER_BOUND;
    }

    @Value
    public static class TranspositionTableEntry {
        private final int depth;
        private final TranspositionTableEntryType type;
        private final int value;
    }

    @Value
    public static class BestMoveTableEntry {
        private final int depth;
        private final int column;
    }

    @Value
    private static class TaskWithMove {
        private final BoardValueSearchRecursiveTask task;
        private final int move;
    }

    @AllArgsConstructor
    private class BoardValueSearchRecursiveTask extends RecursiveTask<Integer> {
        private final List<BoardValueSearchRecursiveTask> subtasks = new CopyOnWriteArrayList<>();

        private final Board board;
        private final int depth;
        private final int originalAlpha;
        private final int originalBeta;
        private final EvaluationFunction evaluationFunction;

        private BoardValueSearchRecursiveTask createSubtask(Board board, int depth, int alpha, int beta, EvaluationFunction evaluationFunction) {
            if (isCancelled() || isDone()) {
                throw new IllegalStateException("Failed to create a subtask. Current task is cancelled or done.");
            }
            BoardValueSearchRecursiveTask subtask = new BoardValueSearchRecursiveTask(board, depth, alpha, beta, evaluationFunction);
            subtasks.add(subtask);
            return subtask;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            boolean cancel = super.cancel(mayInterruptIfRunning);
            for (BoardValueSearchRecursiveTask subtask : subtasks) {
                subtask.cancel(mayInterruptIfRunning);
            }
            return cancel;
        }

        @Override
        protected Integer compute() {
            if (stopped) {
                throw new CancellationException("Stop requested");
            }

            if (MinimaxHelper.isTerminal(depth, board)) {
                return evaluationFunction.evaluate(board);
            }

            int currentAlpha = originalAlpha;
            int currentBeta = originalBeta;

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
            Iterator<Pair<Integer, Board>> nextMovesIterator = getNextMovesOrdered(board, evaluationFunction);

            // Get a score for a younger brother synchronously.
            int value = NEGATIVE_INFINITY;
            int column = NEGATIVE_INFINITY;
            if (!nextMovesIterator.hasNext()) {
                throw new IllegalStateException("There are no moves for this board");
            }
            Pair<Integer, Board> youngBrotherMove = nextMovesIterator.next();
            BoardValueSearchRecursiveTask youngBrotherTask = createSubtask(youngBrotherMove.getRight(), depth - 1, -currentBeta, -currentAlpha, evaluationFunction);
            int score = -1 * youngBrotherTask.compute();
            if (score > value) {
                value = score;
                column = youngBrotherMove.getLeft();
            }
            currentAlpha = Math.max(currentAlpha, value);
            if (currentAlpha >= currentBeta) {
                saveTranspositionTableEntry(board, depth, value, originalAlpha, currentBeta);
                return value;
            }

            // We can't prune, so we need to create recursive tasks for older brothers.
            ArrayList<TaskWithMove> tasksWithMoves = new ArrayList<>(BOARD_WIDTH);
            while (nextMovesIterator.hasNext()) {
                Pair<Integer, Board> nextMove = nextMovesIterator.next();
                BoardValueSearchRecursiveTask task = createSubtask(nextMove.getRight(), depth - 1, -currentBeta, -currentAlpha, evaluationFunction);
                tasksWithMoves.add(new TaskWithMove(task, nextMove.getLeft()));
            }

            // Fork this new recursive tasks.
            tasksWithMoves.stream().map(TaskWithMove::getTask).forEach(BoardValueSearchRecursiveTask::fork);

            // Process the results of the older brothers and try to cancel forked tasks if we're in pruning situation.
            int index = 0;
            while (index < tasksWithMoves.size()) {
                TaskWithMove taskWithMove = tasksWithMoves.get(index);
                index++;

                score = -1 * taskWithMove.getTask().join();
                if (score > value) {
                    value = score;
                    column = taskWithMove.getMove();
                }
                currentAlpha = Math.max(currentAlpha, value);
                if (currentAlpha >= currentBeta) {
                    break;
                }
            }

            // Cancel tasks that are left after pruning or canceling.
            while (index < tasksWithMoves.size()) {
                TaskWithMove taskWithMove = tasksWithMoves.get(index);
                taskWithMove.getTask().cancel(true);
                index++;
            }

            saveTranspositionTableEntry(board, depth, value, originalAlpha, currentBeta);
            return value;
        }
    }

}
