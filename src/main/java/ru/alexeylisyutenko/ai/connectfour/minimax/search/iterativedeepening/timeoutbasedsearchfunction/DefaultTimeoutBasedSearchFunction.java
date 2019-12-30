package ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.timeoutbasedsearchfunction;

import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.StoppableSearchFunction;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultTimeoutBasedSearchFunction implements TimeoutBasedSearchFunction {
    private final StoppableSearchFunction stoppableSearchFunction;

    public DefaultTimeoutBasedSearchFunction(StoppableSearchFunction stoppableSearchFunction) {
        this.stoppableSearchFunction = stoppableSearchFunction;
    }

    @Override
    public Optional<Move> search(Board board, int depth, EvaluationFunction evaluationFunction, int timeout) {
        AtomicReference<Move> foundMove = new AtomicReference<>();

        // TODO: Use some executor service here.
        Thread searchThread = launchSearchThread(board, depth, evaluationFunction, foundMove);
        joinThread(timeout, searchThread);
        stoppableSearchFunction.stop();
        joinThread(searchThread);

        return Optional.ofNullable(foundMove.get());
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

    private Thread launchSearchThread(Board board, int depth, EvaluationFunction evaluationFunction, AtomicReference<Move> foundMove) {
        Thread searchThread = new Thread(() -> {
            try {
                Move move = stoppableSearchFunction.search(board, depth, evaluationFunction);
                foundMove.set(move);
            } catch (RuntimeException ignore) {
            }
        });
        searchThread.start();
        return searchThread;
    }
}
