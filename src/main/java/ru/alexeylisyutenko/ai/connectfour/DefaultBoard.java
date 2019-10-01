package ru.alexeylisyutenko.ai.connectfour;

import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;

import java.util.List;
import java.util.Set;

import static ru.alexeylisyutenko.ai.connectfour.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.Constants.BOARD_WIDTH;

public class DefaultBoard implements Board {
    private final static int EMPTY_CELL_PLAYER_ID = 0;

    private final int[] boardArray;
    private final int currentPlayerId;

    public DefaultBoard() {
        this.boardArray = new int[BOARD_HEIGHT * BOARD_WIDTH];
        this.currentPlayerId = 1;
    }

    public DefaultBoard(int[] boardArray, int currentPlayerId) {
        this.boardArray = boardArray;
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
    public int getCellPlayerId(int row, int column) {
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
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            int playerId = getCellPlayerId(i, column);
            if (playerId != EMPTY_CELL_PLAYER_ID) {
                return playerId;
            }
        }
        return 0;
    }

    @Override
    public int getHeightOfColumn(int column) {
        validateColumnNumber(column);
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            if (getCellPlayerId(i, column) != EMPTY_CELL_PLAYER_ID) {
                return i - 1;
            }
        }
        return BOARD_HEIGHT;
    }

    @Override
    public Board makeMove(int column) {
        validateColumnNumber(column);

        int columnHeight = getHeightOfColumn(column);
        if (columnHeight == -1) {
            throw new InvalidMoveException(column, this);
        }

        int[] newBoardArray = boardArray.clone();
        int row = Math.min(5, columnHeight);
        newBoardArray[BOARD_WIDTH * row + column] = currentPlayerId;
        return new DefaultBoard(newBoardArray, getOtherPlayerId());
    }

    @Override
    public int getLongestChain(int playerId) {
//           * Returns the length of the longest contiguous chain of tokens held by the player with the specified player ID.
//                * A 'chain' is as defined by the Connect Four rules, meaning that the first player to build a chain of length 4 wins the game.

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
}
