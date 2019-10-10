package ru.alexeylisyutenko.ai.connectfour.game;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

@EqualsAndHashCode(exclude = "currentPlayerId")
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
        int longestChain = 0;
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                if (getCellPlayerId(row, column) == playerId) {
                    longestChain = Math.max(longestChain, calculateMaximumLengthFromCell(row, column));
                }
            }
        }
        return longestChain;
    }

    private int calculateMaximumLengthFromCell(int row, int column) {
        int length1 = calculateDirectionalVectorLength(row, column, Pair.of(0, 1)) + calculateDirectionalVectorLength(row, column, Pair.of(0, -1)) + 1;
        int length2 = calculateDirectionalVectorLength(row, column, Pair.of(1, 1)) + calculateDirectionalVectorLength(row, column, Pair.of(-1, -1)) + 1;
        int length3 = calculateDirectionalVectorLength(row, column, Pair.of(1, 0)) + calculateDirectionalVectorLength(row, column, Pair.of(-1, 0)) + 1;
        int length4 = calculateDirectionalVectorLength(row, column, Pair.of(1, -1)) + calculateDirectionalVectorLength(row, column, Pair.of(-1, 1)) + 1;

        return Math.max(Math.max(length1, length2), Math.max(length3, length4));
    }

    private int calculateDirectionalVectorLength(int row, int column, Pair<Integer, Integer> direction) {
        int count = 0;
        int playerId = getCellPlayerId(row, column);
        while (row >= 0 && row < BOARD_HEIGHT
                && column >= 0 && column < BOARD_WIDTH
                && getCellPlayerId(row, column) == playerId) {

            row += direction.getLeft();
            column += direction.getRight();
            count++;
        }
        return count - 1;
    }

    @Override
    public Set<List<Cell>> getChainCells(int playerId) {
        HashSet<List<Cell>> chains = new HashSet<>();
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                if (getCellPlayerId(row, column) == playerId) {
                    Set<List<Cell>> chainSetsFromCell = getChainSetsFromCell(row, column);
                    chains.addAll(chainSetsFromCell);
                }
            }
        }
        return chains;
    }

    private Set<List<Cell>> getChainSetsFromCell(int row, int column) {
        List<Cell> chain1 = Lists.newArrayList(Iterables.concat(
                Lists.reverse(getDirectionalVectorCells(row, column, Pair.of(0, 1))),
                List.of(new Cell(row, column)),
                getDirectionalVectorCells(row, column, Pair.of(0, -1))
        ));
        List<Cell> chain2 = Lists.newArrayList(Iterables.concat(
                Lists.reverse(getDirectionalVectorCells(row, column, Pair.of(1, 1))),
                List.of(new Cell(row, column)),
                getDirectionalVectorCells(row, column, Pair.of(-1, -1))
        ));
        List<Cell> chain3 = Lists.newArrayList(Iterables.concat(
                Lists.reverse(getDirectionalVectorCells(row, column, Pair.of(1, 0))),
                List.of(new Cell(row, column)),
                getDirectionalVectorCells(row, column, Pair.of(-1, 0))
        ));
        List<Cell> chain4 = Lists.newArrayList(Iterables.concat(
                Lists.reverse(getDirectionalVectorCells(row, column, Pair.of(1, -1))),
                List.of(new Cell(row, column)),
                getDirectionalVectorCells(row, column, Pair.of(-1, 1))
        ));

        HashSet<List<Cell>> chains = new HashSet<>();
        chains.add(chain1);
        chains.add(chain2);
        chains.add(chain3);
        chains.add(chain4);
        return chains;
    }

    private List<Cell> getDirectionalVectorCells(int row, int column, Pair<Integer, Integer> direction) {
        List<Cell> chain = new ArrayList<>();
        int playerId = getCellPlayerId(row, column);
        while (row >= 0 && row < BOARD_HEIGHT
                && column >= 0 && column < BOARD_WIDTH
                && getCellPlayerId(row, column) == playerId) {

            chain.add(new Cell(row, column));
            row += direction.getLeft();
            column += direction.getRight();
        }
        chain.remove(0);
        return chain;
    }

    @Override
    public int getNumberOfTokensOnBoard() {
        int tokens = 0;
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                if (getCellPlayerId(row, column) != EMPTY_CELL_PLAYER_ID) {
                    tokens++;
                }
            }
        }
        return tokens;
    }

    @Override
    public int getWinnerId() {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                int currentCellPlayerId = getCellPlayerId(row, column);
                if (currentCellPlayerId != EMPTY_CELL_PLAYER_ID) {
                    boolean winFound = calculateMaximumLengthFromCell(row, column) >= 4;
                    if (winFound) {
                        return currentCellPlayerId;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public boolean isGameOver() {
        return getWinnerId() != 0 || isTie();
    }

    @Override
    public boolean isTie() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            if (getCellPlayerId(0, i) == EMPTY_CELL_PLAYER_ID) {
                return false;
            }
        }
        return true;
    }
}
