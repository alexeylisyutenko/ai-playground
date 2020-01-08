package ru.alexeylisyutenko.ai.connectfour.game;

import lombok.EqualsAndHashCode;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;

import java.util.ArrayList;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.util.BitBoardHelper.bitmapToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class BitBoard extends AbstractBoard {
    /**
     * 4-token slots masks used int the internal evaluate function.
     */
    private static final long[] SLOT_MASKS = calculateSlotMasks();

    /**
     * A bottom mask which contains ones in all bottom positions.
     */
    private static final long ALL_COLUMNS_BOTTOM_MASK = calculateBottomMask();

    /**
     * Bitmap of the current player's stones
     */
    @EqualsAndHashCode.Include
    private final long position;
    /**
     * Bitmap of all the already played cells.
     */
    @EqualsAndHashCode.Include
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

    private static long calculateBottomMask() {
        long bottom = 0;
        for (int column = 0; column < BOARD_WIDTH; column++) {
            bottom |= bottomMask(column);
        }
        return bottom;
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

    private static long[] calculateSlotMasks() {
        ArrayList<Long> slotMasks = new ArrayList<>();

        // Horizontal chains.
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH - 3; column++) {
                long m = cellMask(row, column);
                m |= cellMask(row, column + 1);
                m |= cellMask(row, column + 2);
                m |= cellMask(row, column + 3);
                slotMasks.add(m);
            }
        }

        // Vertical chains.
        for (int column = 0; column < BOARD_WIDTH; column++) {
            for (int row = 0; row < BOARD_HEIGHT - 3; row++) {
                long m = cellMask(row, column);
                m |= cellMask(row + 1, column);
                m |= cellMask(row + 2, column);
                m |= cellMask(row + 3, column);
                slotMasks.add(m);
            }
        }

        // Diagonals from top-left corner to bottom-right corner.
        for (int row = 0; row < BOARD_HEIGHT - 3; row++) {
            for (int column = 0; column < BOARD_WIDTH - 3; column++) {
                long m = cellMask(row, column);
                m |= cellMask(row + 1, column + 1);
                m |= cellMask(row + 2, column + 2);
                m |= cellMask(row + 3, column + 3);
                slotMasks.add(m);
            }
        }

        // Diagonals from bottom-left corner to top-right corner.
        for (int row = BOARD_HEIGHT - 3; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH - 3; column++) {
                long m = cellMask(row, column);
                m |= cellMask(row - 1, column + 1);
                m |= cellMask(row - 2, column + 2);
                m |= cellMask(row - 3, column + 3);
                slotMasks.add(m);
            }
        }

        return slotMasks.stream().mapToLong(value -> value).toArray();
    }

    private static long bottomMask(int column) {
        return 1L << column * (BOARD_HEIGHT + 1);
    }

    private static long topMask(int column) {
        return 1L << (BOARD_HEIGHT - 1) << column * (BOARD_HEIGHT + 1);
    }

    private static long cellMask(int row, int column) {
        return 1L << (BOARD_HEIGHT - 1 - row) << column * (BOARD_HEIGHT + 1);
    }

    private static long columnMask(int column) {
        return ((1L << BOARD_HEIGHT) - 1) << column * (BOARD_HEIGHT + 1);
    }

    private static long msb64(long n) {
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        n |= n >> 32;
        n = n + 1;
        return (n >> 1);
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
        if ((position & msb64(m)) != 0) {
            return getCurrentPlayerId();
        } else {
            return getOtherPlayerId();
        }
    }

    @Override
    public int getHeightOfColumn(int column) {
        validateColumnNumber(column);

        long m = (mask & columnMask(column)) >> column * (BOARD_HEIGHT + 1);
        long msb = (m + 1) >> 1;
        switch ((int) msb) {
            case 0:
                return 6;
            case 1:
                return 4;
            case 2:
                return 3;
            case 4:
                return 2;
            case 8:
                return 1;
            case 16:
                return 0;
            case 32:
                return -1;
            default:
                throw new IllegalStateException();
        }
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

    @Override
    public boolean isInternalEvaluationSupported() {
        return true;
    }

    @Override
    public int internalEvaluate() {
        int score;
        if (winFoundInPosition(position ^ mask)) {
            score = -1000000 + getNumberOfTokensOnBoard();
        } else if (isTie()) {
            score = 0;
        } else {
            score = 0;
            for (long slotMask : SLOT_MASKS) {
                int currentPlayerTokens = Long.bitCount(position & slotMask);
                int otherPlayerTokens = Long.bitCount((position ^ mask) & slotMask);
                if (currentPlayerTokens != 0 && otherPlayerTokens == 0) {
                    score += currentPlayerTokens * currentPlayerTokens;
                } else if (currentPlayerTokens == 0 && otherPlayerTokens != 0) {
                    score -= otherPlayerTokens * otherPlayerTokens;
                }
            }
        }
        return score;
    }

    @Override
    public long getId() {
        return position + mask + ALL_COLUMNS_BOTTOM_MASK;
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

    public void printInternals() {
        System.out.println("Position: ");
        System.out.println(bitmapToString(position));
        System.out.println();

        System.out.println("Mask");
        System.out.println(bitmapToString(mask));
        System.out.println();
    }
}
