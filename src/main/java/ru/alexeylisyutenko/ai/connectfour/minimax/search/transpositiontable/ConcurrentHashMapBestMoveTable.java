package ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentHashMapBestMoveTable implements BestMoveTable {
    private final ConcurrentMap<Long, BestMoveTableEntry> bestMovesTable = new ConcurrentHashMap<>();

    @Override
    public BestMoveTableEntry get(Board board) {
        return bestMovesTable.get(board.getId());
    }

    @Override
    public void save(Board board, BestMoveTableEntry entry) {
        bestMovesTable.merge(board.getId(), entry, (entry1, entry2) -> entry1.getDepth() > entry2.getDepth() ? entry1 : entry2);
    }
}
