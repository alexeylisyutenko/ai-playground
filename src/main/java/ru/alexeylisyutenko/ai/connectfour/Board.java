package ru.alexeylisyutenko.ai.connectfour;

import java.util.List;
import java.util.Set;

/**
 * Connect-Four game board.
 * <p>
 * Implementations of this interface must be immutable.
 * </p>
 */
public interface Board {
    /**
     * Returns the id of the player who should be moving now.
     *
     * @return the id of the player who should be moving now
     */
    int getCurrentPlayerId();

    /**
     * Returns the id of the opponent of the player who should be moving now.
     *
     * @return the id of the opponent of the player who should be moving now
     */
    int getOtherPlayerId();

    /**
     * Get the id# of the player owning the token in the specified cell.
     * Return 0 if it is unclaimed.
     *
     * @param row    row of a cell
     * @param column column of a cell
     * @return the id# of the player owning the token in the specified cell
     */
    int getCellPlayerId(int row, int column);

    /**
     * Returns the player ID of the player whose token is the topmost token in the specified column.
     * Returns 0 if the column is empty.
     *
     * @param column column number
     * @return the player ID of the player whose token is the topmost token in the specified column
     */
    int getTopEltInColumn(int column);

    /**
     * Returns the row number for the highest-numbered unoccupied row in the specified column.
     * Returns -1 if the column is full, returns 6 if the column is empty.
     * <p>
     * NOTE: this is the row index number not the actual "height" of the column, and that row
     * indices count from 0 at the top-most row down to 5 at the bottom-most row.
     *
     * @param column column number
     * @return the row number for the highest-numbered unoccupied row in the specified column
     */
    int getHeightOfColumn(int column);

    /**
     * Returns a new board with the current player's token added to column.
     * The new board will indicate that it's now the other player's turn.
     *
     * @param column column used to make a move
     * @return new board with current player's token added to column
     */
    Board makeMove(int column);

    /**
     * Returns the length of the longest contiguous chain of tokens held by the player with the specified player ID.
     * A 'chain' is as defined by the Connect Four rules, meaning that the first player to build a chain of length 4 wins the game.
     *
     * @param playerId player ID
     * @return the length of the longest contiguous chain of tokens held by the player with the specified player ID
     */
    int getLongestChain(int playerId);

    /**
     * Returns a set containing cells for each distinct chain (of length 1 or greater) of tokens controlled
     * by the current player on the board.
     *
     * @param playerId player ID
     * @return a set containing cells for each distinct chain (of length 1 or greater) of tokens controlled
     * by the current player on the board
     */
    Set<List<Cell>> getChainCells(int playerId);

    /**
     * Returns the total number of tokens on the board (for either player).
     * This can be used as a game progress meter of sorts, since the number increases by exactly one each turn.
     *
     * @return the total number of tokens on the board.
     */
    int getNumberOfTokensOnBoard();

    /**
     * Returns the player ID number of the player who has won, or 0.
     *
     * @return player ID who won the game
     */
    int getWinnerId();

    /**
     * Returns true if the game has come to a conclusion. Use getWinnerId() to determine the winner.
     *
     * @return true if the game has come to a conclusion, otherwise false
     */
    boolean isGameOver();
}
