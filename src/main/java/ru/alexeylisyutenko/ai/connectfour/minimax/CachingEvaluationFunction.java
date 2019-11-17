package ru.alexeylisyutenko.ai.connectfour.minimax;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Board evaluation function which caches results in a hash map.
 */
public class CachingEvaluationFunction implements EvaluationFunction {
    private final EvaluationFunction evaluationFunction;
    private final ConcurrentMap<Board, Integer> cache;

    public CachingEvaluationFunction(EvaluationFunction evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public int evaluate(Board board) {
        return cache.computeIfAbsent(board, dummy -> evaluationFunction.evaluate(board));
    }
}
