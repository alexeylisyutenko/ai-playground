package ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

/**
 * A transposition table that is a cache of previously seen positions, and associated evaluations, in a game tree.
 */
public interface TranspositionTable {
    /**
     * Get a transposition table entry for a board.
     *
     * @param board a board which we are looking an entry for
     * @return null - if there is no such entry for this board, otherwise - a valid entry for the board.
     */
    TranspositionTableEntry get(Board board);

    /**
     * Save a transposition table entry for a board.
     *
     * @param board a board which we are saving an entry for
     * @param entry an entry we are saving
     */
    void save(Board board, TranspositionTableEntry entry);
}
