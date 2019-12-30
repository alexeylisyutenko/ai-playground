package ru.alexeylisyutenko.ai.connectfour.game;

import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;

import java.util.List;
import java.util.Set;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class BitBoard implements Board {
    /**
     * Bitmap of the current player's stones
     */
    private final long position;

    /**
     * Bitmap of all the already played cells.
     */
    private final long mask;

    /**
     * Current player's id.
     */
    private final int currentPlayerId;

    /**
     * Number of moves played since the beginning of the game.
     */
    private final int moves;

    public BitBoard() {
        this.position = 0;
        this.mask = 0;
        this.currentPlayerId = 1;
        this.moves = 0;
    }

    public BitBoard(long position, long mask, int currentPlayerId, int moves) {
        this.position = position;
        this.mask = mask;
        this.currentPlayerId = currentPlayerId;
        this.moves = moves;
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

        long cellMask = cellMask(row, column);
        if ((mask & cellMask) == 0) {
            return 0;
        }
        if ((position & cellMask) == 0) {
            return getOtherPlayerId();
        } else {
            return getCurrentPlayerId();
        }
    }

    @Override
    public int getTopEltInColumn(int column) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int getHeightOfColumn(int column) {
        //
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Board makeMove(int column) {
        validateColumnNumber(column);

        boolean canPlay = (mask & topMask(column)) == 0;
        if (!canPlay) {
            throw new InvalidMoveException(column, this);
        }
        return new BitBoard(position ^ mask, mask | (mask + bottomMask(column)), getOtherPlayerId(), moves + 1);
    }

    @Override
    public int getLongestChain(int playerId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Set<List<Cell>> getChainCells(int playerId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int getNumberOfTokensOnBoard() {
        return moves;
    }

    @Override
    public int getWinnerId() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean isTie() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean isGameOver() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private long bottomMask(int column) {
        return 1L << column * (BOARD_HEIGHT + 1);
    }

    private long topMask(int column) {
        return 1L << (BOARD_HEIGHT - 1) << column * (BOARD_HEIGHT + 1);
    }

    private long cellMask(int row, int column) {
        return 1L << (BOARD_HEIGHT - 1 - row) << column * (BOARD_HEIGHT + 1);
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
}
