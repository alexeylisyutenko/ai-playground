package ru.alexeylisyutenko.ai.connectfour.game;

import lombok.EqualsAndHashCode;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

@EqualsAndHashCode(callSuper = false)
public class DefaultBoard extends AbstractBoard {
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
}
