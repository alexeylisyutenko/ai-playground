package ru.alexeylisyutenko.ai.connectfour;

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
    int getCell(int row, int column);

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
     *
     * @return
     */
    int getLongestChain();

    /**
     * Visualizes the board.
     */
    void visualize();
}
