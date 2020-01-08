package ru.alexeylisyutenko.ai.connectfour.minimax.evaluation;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;

import java.util.concurrent.ExecutionException;

/**
 * Board evaluation function which caches results.
 */
public class CachingEvaluationFunction implements EvaluationFunction {
    public final static int DEFAULT_CACHE_SIZE = 10_000_000;

    private final EvaluationFunction evaluationFunction;
    private final Cache<Long, Integer> cache;

    public CachingEvaluationFunction(EvaluationFunction evaluationFunction) {
        this(evaluationFunction, DEFAULT_CACHE_SIZE, false);
    }

    public CachingEvaluationFunction(EvaluationFunction evaluationFunction, int cacheMaximumSize, boolean recordStats) {
        this.evaluationFunction = evaluationFunction;
        this.cache = buildCache(cacheMaximumSize, recordStats);
    }

    private Cache<Long, Integer> buildCache(int cacheMaximumSize, boolean recordStats) {
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
        cacheBuilder.maximumSize(cacheMaximumSize);
        if (recordStats) {
            cacheBuilder.recordStats();
        }
        return cacheBuilder.build();
    }

    @Override
    public int evaluate(Board board) {
        try {
            return cache.get(board.getId(), () -> evaluationFunction.evaluate(board));
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public CacheStats getCacheStats() {
        return cache.stats();
    }

    public void clearCache() {
        cache.invalidateAll();
    }
}
