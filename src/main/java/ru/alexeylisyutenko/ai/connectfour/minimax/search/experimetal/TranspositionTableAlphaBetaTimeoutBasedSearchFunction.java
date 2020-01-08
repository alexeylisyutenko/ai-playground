package ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.timeoutbasedsearchfunction.TimeoutBasedSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.util.Constants.NEGATIVE_INFINITY;
import static ru.alexeylisyutenko.ai.connectfour.util.Constants.POSITIVE_INFINITY;

// TODO: Benchmark and refactor. Modify according to TranspositionTableAlphaBetaSearchFunction implementation.

public class TranspositionTableAlphaBetaTimeoutBasedSearchFunction implements TimeoutBasedSearchFunction {
    private final TranspositionTable transpositionTable;
    private final BestMoveTable bestMovesTable;

    private volatile boolean stopped = true;

    public TranspositionTableAlphaBetaTimeoutBasedSearchFunction() {
        this(new ConcurrentHashMapTranspositionTable(), new ConcurrentHashMapBestMoveTable());
    }

    public TranspositionTableAlphaBetaTimeoutBasedSearchFunction(TranspositionTable transpositionTable, BestMoveTable bestMovesTable) {
        this.transpositionTable = transpositionTable;
        this.bestMovesTable = bestMovesTable;
    }

    @Override
    public Optional<Move> search(Board board, int depth, EvaluationFunction evaluationFunction, int timeout) {
        if (!stopped) {
            throw new IllegalStateException("Search is already started");
        }
        stopped = false;
        AtomicReference<Move> foundMove = new AtomicReference<>();
        Thread searchThread = launchSearchThread(board, depth, evaluationFunction, foundMove);
        joinThread(timeout, searchThread);
        stopped = true;
        joinThread(searchThread);
        return Optional.ofNullable(foundMove.get());
    }

    private Thread launchSearchThread(Board board, int depth, EvaluationFunction evaluationFunction, AtomicReference<Move> foundMove) {
        Thread searchThread = new Thread(() -> {
            try {
                Move move = doSearch(board, depth, evaluationFunction);
                foundMove.set(move);
            } catch (RuntimeException ignore) {
            }
        });
        searchThread.start();
        return searchThread;
    }

    private void joinThread(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void joinThread(int timeout, Thread thread) {
        try {
            thread.join(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Move doSearch(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            throw new IllegalStateException("Search function was called on a terminal node");
        }

        BestMoveTableEntry bestMoveTableEntry = bestMovesTable.get(board);
        if (bestMoveTableEntry != null && bestMoveTableEntry.getDepth() >= depth) {
            return new Move(bestMoveTableEntry.getColumn(), bestMoveTableEntry.getScore());
        }

        List<Pair<Integer, Board>> nextMoves = getNextMovesOrderedAsList(board, evaluationFunction);
        int bestColumn = NEGATIVE_INFINITY;
        int bestScore = NEGATIVE_INFINITY;

        for (Pair<Integer, Board> nextMove : nextMoves) {
            int score = -1 * findAlphaBetaBoardValue(nextMove.getRight(), depth - 1, -POSITIVE_INFINITY, -bestScore, evaluationFunction);
            if (score > bestScore) {
                bestScore = score;
                bestColumn = nextMove.getLeft();
            }
        }

        return new Move(bestColumn, bestScore);
    }

    private int findAlphaBetaBoardValue(Board board, int depth, int alpha, int beta, EvaluationFunction evaluationFunction) {
        if (stopped) {
            throw new RuntimeException("Stop requested");
        }
        if (MinimaxHelper.isTerminal(depth, board)) {
            return evaluationFunction.evaluate(board);
        }

        BestMoveTableEntry bestMoveTableEntry = bestMovesTable.get(board);
        if (bestMoveTableEntry != null && bestMoveTableEntry.getDepth() >= depth) {
            return bestMoveTableEntry.getColumn();
        }

        int originalAlpha = alpha;

        TranspositionTableEntry transpositionTableEntry = transpositionTable.get(board);
        if (transpositionTableEntry != null && transpositionTableEntry.getDepth() >= depth) {
            switch (transpositionTableEntry.getType()) {
                case EXACT_VALUE:
                    return transpositionTableEntry.getValue();
                case UPPER_BOUND:
                    beta = Math.min(beta, transpositionTableEntry.getValue());
                    break;
                case LOWER_BOUND:
                    alpha = Math.max(alpha, transpositionTableEntry.getValue());
                    break;
            }

            if (alpha >= beta) {
                return transpositionTableEntry.getValue();
            }
        }

        Iterator<Pair<Integer, Board>> nextMovesIterator = getNextMovesOrdered(board, evaluationFunction);

        int value = NEGATIVE_INFINITY;
        int column = NEGATIVE_INFINITY;
        while (nextMovesIterator.hasNext()) {
            Pair<Integer, Board> nextMove = nextMovesIterator.next();

            int score = -1 * findAlphaBetaBoardValue(nextMove.getRight(), depth - 1, -beta, -alpha, evaluationFunction);
            if (score > value) {
                value = score;
                column = nextMove.getLeft();
            }

            alpha = Math.max(alpha, value);
            if (alpha >= beta) {
                break;
            }
        }

        // If we get an exact value for a board we can save the results in the best moves table.
        boolean isBoardScoreExact = value > originalAlpha && value < beta;
        if (isBoardScoreExact) {
            saveBestMovesTableEntry(board, depth, column, value);
        }

        // Save entry in the transposition table.
        saveTranspositionTableEntry(board, depth, value, originalAlpha, beta);

        return value;
    }

    private void saveBestMovesTableEntry(Board board, int depth, int column, int score) {
        bestMovesTable.save(board, new BestMoveTableEntry(depth, column, score));
    }

    private Iterator<Pair<Integer, Board>> getNextMovesOrdered(Board board, EvaluationFunction evaluationFunction) {
        BestMoveTableEntry bestMoveTableEntry = bestMovesTable.get(board);
        if (bestMoveTableEntry != null) {
            return MinimaxHelper.getAllNextMovesIterator(board, bestMoveTableEntry.getColumn());
        } else {
            List<Pair<Integer, Board>> nextMoves = getNextMovesOrderedByEvaluationFunction(board, evaluationFunction);

            // Save a best move obtained by next moves evaluation.
            int column = nextMoves.get(0).getLeft();
            saveBestMovesTableEntry(board, 0, column, 0);

            return nextMoves.iterator();

        }
    }

    private List<Pair<Integer, Board>> getNextMovesOrderedAsList(Board board, EvaluationFunction evaluationFunction) {
        List<Pair<Integer, Board>> nextMoves = new ArrayList<>(BOARD_WIDTH);
        getNextMovesOrdered(board, evaluationFunction).forEachRemaining(nextMoves::add);
        return nextMoves;
    }

    private List<Pair<Integer, Board>> getNextMovesOrderedByEvaluationFunction(Board board, EvaluationFunction evaluationFunction) {
        return MinimaxHelper.getAllNextMoves(board).stream()
                .map(move -> new ImmutableTriple<>(move.getLeft(), move.getRight(), evaluationFunction.evaluate(move.getRight())))
                .sorted(Comparator.comparing(ImmutableTriple::getRight))
                .map(triple -> Pair.of(triple.getLeft(), triple.getMiddle()))
                .collect(Collectors.toList());
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
        transpositionTable.save(board, entry);
    }
}
