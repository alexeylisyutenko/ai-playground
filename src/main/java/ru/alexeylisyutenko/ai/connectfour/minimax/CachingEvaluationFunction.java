package ru.alexeylisyutenko.ai.connectfour.minimax;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.util.HashMap;
import java.util.Map;

/**
 * Board evaluation function which caches results in a hash map.
 */
public class CachingEvaluationFunction implements EvaluationFunction {
    private final EvaluationFunction evaluationFunction;
    private final Map<Board, Integer> cache;

    public CachingEvaluationFunction(EvaluationFunction evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
        this.cache = new HashMap<>();
    }

    @Override
    public int evaluate(Board board) {
        return cache.computeIfAbsent(board, dummy -> evaluationFunction.evaluate(board));
    }
}
