package ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

public class CacheBasedTranspositionTable implements TranspositionTable {
    public static final int DEFAULT_TRANSPOSITION_TABLE_CACHE_MAXIMUM_SIZE = 10_000_000;

    private final Cache<Long, TranspositionTableEntry> cache;

    public CacheBasedTranspositionTable() {
        this(DEFAULT_TRANSPOSITION_TABLE_CACHE_MAXIMUM_SIZE);
    }

    public CacheBasedTranspositionTable(int cacheMaximumSize) {
        this.cache = CacheBuilder.newBuilder().maximumSize(cacheMaximumSize).build();
    }

    @Override
    public TranspositionTableEntry get(Board board) {
        return cache.getIfPresent(board.getId());
    }

    @Override
    public void save(Board board, TranspositionTableEntry entry) {
        cache.put(board.getId(), entry);
    }
}
