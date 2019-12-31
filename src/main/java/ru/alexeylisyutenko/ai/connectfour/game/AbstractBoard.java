package ru.alexeylisyutenko.ai.connectfour.game;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public abstract class AbstractBoard implements Board {
    protected final static int EMPTY_CELL_PLAYER_ID = 0;

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

    protected void validateColumnNumber(int column) {
        if (column < 0 || column >= BOARD_WIDTH) {
            throw new IllegalArgumentException(String.format("Incorrect column number '%d', it must be between '%d' and '%d'", column, 0, BOARD_WIDTH - 1));
        }
    }

    protected void validateRowNumber(int row) {
        if (row < 0 || row >= BOARD_HEIGHT) {
            throw new IllegalArgumentException(String.format("Incorrect row number '%d', it must be between '%d' and '%d'", row, 0, BOARD_HEIGHT - 1));
        }
    }
}
