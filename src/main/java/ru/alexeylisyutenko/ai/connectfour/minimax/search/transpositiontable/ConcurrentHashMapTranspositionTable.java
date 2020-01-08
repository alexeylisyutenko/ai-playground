package ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentHashMapTranspositionTable implements TranspositionTable {
    private final ConcurrentMap<Long, TranspositionTableEntry> transpositionTable = new ConcurrentHashMap<>();

    @Override
    public TranspositionTableEntry get(Board board) {
        return transpositionTable.get(board.getId());
    }

    @Override
    public void save(Board board, TranspositionTableEntry entry) {
        transpositionTable.merge(board.getId(), entry, (entry1, entry2) -> entry1.getDepth() > entry2.getDepth() ? entry1 : entry2);
    }
}
