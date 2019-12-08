package ru.alexeylisyutenko.ai.connectfour.minimax;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.util.concurrent.atomic.AtomicLong;

public class CountingEvaluationFunction implements EvaluationFunction {
    private final EvaluationFunction evaluationFunction;
    private final AtomicLong counter;

    public CountingEvaluationFunction(EvaluationFunction evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
        this.counter = new AtomicLong();
    }

    @Override
    public int evaluate(Board board) {
        counter.incrementAndGet();
        return evaluationFunction.evaluate(board);
    }

    public long getEvaluationsCounter() {
        return counter.get();
    }
}
