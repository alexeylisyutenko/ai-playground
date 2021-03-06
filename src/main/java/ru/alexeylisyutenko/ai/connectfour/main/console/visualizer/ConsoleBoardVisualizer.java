package ru.alexeylisyutenko.ai.connectfour.main.console.visualizer;

import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.Constants;

public class ConsoleBoardVisualizer implements BoardVisualizer {
    public static final char[] BOARD_SYMBOL_MAPPING = {' ', '\u263B', '\u263A'};

    @Override
    public void visualize(Board board) {
        printRowWithColumnNumbers();
        print(board);
    }

    private void print(Board board) {
        for (int row = 0; row < Constants.BOARD_HEIGHT; row++) {
            printRow(board, row);
        }
    }

    private void printRow(Board board, int row) {
        System.out.print(row + " ");
        for (int column = 0; column < Constants.BOARD_WIDTH; column++) {
            int cell = board.getCellPlayerId(row, column);
            if (cell < 0 || cell >= BOARD_SYMBOL_MAPPING.length) {
                throw new IllegalStateException("Incorrect cell value: " + cell);
            }
            System.out.print(BOARD_SYMBOL_MAPPING[cell] + " ");
        }
        System.out.println();
    }

    private void printRowWithColumnNumbers() {
        System.out.print("  ");
        for (int column = 0; column < Constants.BOARD_WIDTH; column++) {
            System.out.print(column + " ");
        }
        System.out.println();
    }
}
