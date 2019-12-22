package ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.stoppablesearch;

import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal.TranspositionTableYBWCAlphaBetaSearchFunction;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultStoppableSearch implements StoppableSearch {
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private final ConcurrentMap<Board, TranspositionTableYBWCAlphaBetaSearchFunction.TranspositionTableEntry> transpositionTable = new ConcurrentHashMap<>();
    private final ConcurrentMap<Board, TranspositionTableYBWCAlphaBetaSearchFunction.BestMoveTableEntry> bestMovesTable = new ConcurrentHashMap<>();
    private final TranspositionTableYBWCAlphaBetaSearchFunction searchFunction = new TranspositionTableYBWCAlphaBetaSearchFunction(transpositionTable, bestMovesTable, forkJoinPool);

    @Override
    public Optional<Move> search(Board board, int depth, EvaluationFunction evaluationFunction, int timeout) {
        AtomicReference<Move> foundMove = new AtomicReference<>();

        Thread searchThread = new Thread(() -> {
            try {
                Move move = searchFunction.search(board, depth, evaluationFunction);
                foundMove.set(move);
            } catch (RuntimeException ignore) {
            }
        });
        searchThread.start();

        try {
            searchThread.join(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (RecursiveTask<?> task : searchFunction.getTopLevelTasks()) {
            task.cancel(true);
        }

        return Optional.ofNullable(foundMove.get());
    }
}
