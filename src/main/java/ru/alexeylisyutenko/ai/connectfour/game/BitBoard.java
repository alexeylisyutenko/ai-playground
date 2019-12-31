package ru.alexeylisyutenko.ai.connectfour.game;

import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;

import java.util.List;
import java.util.Set;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.util.BitBoardHelper.bitmapToString;

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

    public BitBoard(int[][] boardDoubleArray, int currentPlayerId) {
        validateArraySize(boardDoubleArray);

        long position = 0;
        long mask = 0;
        int moves = 0;
        int otherPlayerId = currentPlayerId == 1 ? 2 : 1;
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                long cellMask = cellMask(row, column);
                int cell = boardDoubleArray[row][column];
                if (cell == currentPlayerId) {
                    position |= cellMask;
                }
                if (cell == currentPlayerId || cell == otherPlayerId) {
                    mask |= cellMask;
                    moves++;
                }
            }
        }

        this.position = position;
        this.mask = mask;
        this.currentPlayerId = currentPlayerId;
        this.moves = moves;
    }

    private static void validateArraySize(int[][] boardDoubleArray) {
        if (boardDoubleArray.length != BOARD_HEIGHT) {
            throw new IllegalArgumentException("Incorrect outer array size");
        }
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            if (boardDoubleArray[i].length != BOARD_WIDTH) {
                throw new IllegalArgumentException("Incorrect inner array size");
            }
        }
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
        validateColumnNumber(column);

        long m = mask & columnMask(column);
        if (m == 0) {
            return 0;
        }
        if ((position & msb(m)) != 0) {
            return getCurrentPlayerId();
        } else {
            return getOtherPlayerId();
        }
    }

    @Override
    public int getHeightOfColumn(int column) {
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
        if (winFoundInPosition(position)) {
            return getCurrentPlayerId();
        }
        if (winFoundInPosition(position ^ mask)) {
            return getOtherPlayerId();
        }
        return 0;
    }

    @Override
    public boolean isTie() {
        return moves == BOARD_HEIGHT * BOARD_WIDTH;
    }

    @Override
    public boolean isGameOver() {
        return getWinnerId() != 0 || isTie();
    }

    private boolean winFoundInPosition(long position) {
        // horizontal
        long m = position & (position >> (BOARD_HEIGHT + 1));
        if ((m & (m >> (2 * (BOARD_HEIGHT + 1)))) != 0) {
            return true;
        }

        // diagonal 1
        m = position & (position >> BOARD_HEIGHT);
        if ((m & (m >> (2 * BOARD_HEIGHT))) != 0) {
            return true;
        }

        // diagonal 2
        m = position & (position >> (BOARD_HEIGHT + 2));
        if ((m & (m >> (2 * (BOARD_HEIGHT + 2)))) != 0) {
            return true;
        }

        // vertical
        m = position & (position >> 1);
        if ((m & (m >> 2)) != 0) {
            return true;
        }

        return false;
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

    private long columnMask(int column) {
        return ((1L << BOARD_HEIGHT) - 1) << column * (BOARD_HEIGHT + 1);
    }

    private long msb(long n) {
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        n |= n >> 32;
        n = n + 1;
        return (n >> 1);
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

    public void printInternals() {
        System.out.println("Position: ");
        System.out.println(bitmapToString(position));
        System.out.println();

        System.out.println("Mask");
        System.out.println(bitmapToString(mask));
        System.out.println();
    }
}
