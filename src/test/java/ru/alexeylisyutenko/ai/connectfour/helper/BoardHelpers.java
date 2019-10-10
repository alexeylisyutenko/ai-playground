package ru.alexeylisyutenko.ai.connectfour.helper;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public final class BoardHelpers {
    private BoardHelpers() {
    }

    public static int[] constructBoardArray(int[][] boardDoubleArray) {
        validateArraySize(boardDoubleArray);
        int[] boardArray = new int[BOARD_HEIGHT * BOARD_WIDTH];
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                boardArray[BOARD_WIDTH * i + j] = boardDoubleArray[i][j];
            }
        }
        return boardArray;
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

    public static void assertBoardArrayEquals(Board board, int[][] boardDoubleArray) {
        validateArraySize(boardDoubleArray);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                String message = String.format("Incorrect cell value with row '%d' and column '%d'", row, column);
                assertEquals(boardDoubleArray[row][column], board.getCellPlayerId(row, column), message);
            }
        }
    }

}
