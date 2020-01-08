package ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

/**
 * A best move table which contains previously estimated best moves for boards.
 */
public interface BestMoveTable {

    /**
     * Get a best move table entry for a board.
     *
     * @param board a board which we are looking an entry for
     * @return null - if there is no such entry for this board, otherwise - a valid entry for the board.
     */
    BestMoveTableEntry get(Board board);

    /**
     * Save a best move table entry for a board.
     *
     * @param board a board which we are saving an entry for
     * @param entry an entry we are saving
     */
    void save(Board board, BestMoveTableEntry entry);
}