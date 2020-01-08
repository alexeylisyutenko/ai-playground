package ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

public class CacheBasedBestMoveTable implements BestMoveTable {
    public static final int DEFAULT_BEST_MOVE_TABLE_CACHE_MAXIMUM_SIZE = 10_000_000;

    private final Cache<Long, BestMoveTableEntry> cache;

    public CacheBasedBestMoveTable() {
        this(DEFAULT_BEST_MOVE_TABLE_CACHE_MAXIMUM_SIZE);
    }

    public CacheBasedBestMoveTable(int cacheMaximumSize) {
        this.cache = CacheBuilder.newBuilder().maximumSize(cacheMaximumSize).build();
    }

    @Override
    public BestMoveTableEntry get(Board board) {
        return cache.getIfPresent(board.getId());
    }

    @Override
    public void save(Board board, BestMoveTableEntry entry) {
        cache.put(board.getId(), entry);
    }
}
