package ru.alexeylisyutenko.ai.connectfour;

import java.util.List;
import java.util.Set;

import static ru.alexeylisyutenko.ai.connectfour.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.Constants.BOARD_WIDTH;

public class DefaultBoard implements Board {
    private final static int EMPTY_CELL_PLAYER_ID = 0;

    private final int[] boardArray;
    private final BoardVisualizer visualizer;
    private final int currentPlayerId;

    public DefaultBoard() {
        this.boardArray = new int[BOARD_HEIGHT * BOARD_WIDTH];
        this.visualizer = new ConsoleBoardVisualizer();
        this.currentPlayerId = 1;
    }

    public DefaultBoard(int[] boardArray, BoardVisualizer visualizer, int currentPlayerId) {
        this.boardArray = boardArray;
        this.visualizer = visualizer;
        this.currentPlayerId = currentPlayerId;
    }

    @Override
    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    @Override
    public int getOtherPlayerId() {
        return currentPlayerId == 1 ? 2 : 1;
    }

    @Override
    public int getCell(int row, int column) {
        validateRowNumber(row);
        validateColumnNumber(column);

        return boardArray[BOARD_WIDTH * row + column];
    }

    private void validateColumnNumber(int column) {
        if (column < 0 || column >= BOARD_WIDTH) {
            throw new IllegalArgumentException(String.format("Incorrect column number '%d', it must be between '%d' and '%d'", column, 0, BOARD_WIDTH - 1));
        }
    }

    private void validateRowNumber(int row) {
        if (row < 0 || row >= BOARD_HEIGHT) {
            throw new IllegalArgumentException(String.format("Incorrect row number '%d', it must be between '%d' and '%d'", row, 0, BOARD_HEIGHT - 1));
        }
    }

    @Override
    public int getTopEltInColumn(int column) {
        validateColumnNumber(column);


        return 0;
    }

    @Override
    public int getHeightOfColumn(int column) {
        validateColumnNumber(column);

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            if (getCell(i, column) != EMPTY_CELL_PLAYER_ID) {
                return i-1;
            }
        }
        return BOARD_HEIGHT;

        /**
         *      * Returns the row number for the highest-numbered unoccupied row in the specified column.
         *      * Returns -1 if the column is full, returns 6 if the column is empty.
         *      * <p>
         *      * NOTE: this is the row index number not the actual "height" of the column, and that row
         *      * indices count from 0 at the top-most row down to 5 at the bottom-most row.
         */
    }

    @Override
    public Board makeMove(int column) {
        return null;
    }

    @Override
    public int getLongestChain(int playerId) {
        return 0;
    }

    @Override
    public Set<List<Cell>> getChainCells(int playerId) {
        return null;
    }

    @Override
    public int getNumberOfTokensOnBoard() {
        return 0;
    }

    @Override
    public int getWinnerId() {
        return 0;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public void visualize() {
        visualizer.visualize(this);
    }
}
