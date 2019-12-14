package ru.alexeylisyutenko.ai.connectfour.minimax.evaluation;

import lombok.Value;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.Cell;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class EvenBetterEvaluationFunction implements EvaluationFunction {
    @Override
    public int evaluate(Board board) {
        int score;
        if (board.isGameOver()) {
            score = -1000000 + board.getNumberOfTokensOnBoard();
        } else {
            score = 0;

            // Horizontal chains.
            ChainDirection direction = new ChainDirection(0, 1);
            for (int row = 0; row < BOARD_HEIGHT; row++) {
                for (int column = 0; column < BOARD_WIDTH - 3; column++) {
                    score += evaluateFourCellCombinationScore(new Cell(row, column), direction, board);
                }
            }

            // Vertical chains.
            direction = new ChainDirection(1, 0);
            for (int column = 0; column < BOARD_WIDTH; column++) {
                for (int row = 0; row < BOARD_HEIGHT - 3; row++) {
                    score += evaluateFourCellCombinationScore(new Cell(row, column), direction, board);
                }
            }

            // Diagonals from top-left corner to bottom-right corner.
            direction = new ChainDirection(1, 1);
            for (int row = 0; row < BOARD_HEIGHT - 3; row++) {
                for (int column = 0; column < BOARD_WIDTH - 3; column++) {
                    Cell cell = new Cell(row, column);
                    score += evaluateFourCellCombinationScore(new Cell(row, column), direction, board);
                }
            }

            // Diagonals from bottom-left corner to top-right corner.
            direction = new ChainDirection(-1, 1);
            for (int row = BOARD_HEIGHT - 3; row < BOARD_HEIGHT; row++) {
                for (int column = 0; column < BOARD_WIDTH - 3; column++) {
                    Cell cell = new Cell(row, column);
                    score += evaluateFourCellCombinationScore(new Cell(row, column), direction, board);
                }
            }
        }
        return score;
    }

    private int evaluateFourCellCombinationScore(Cell cell, ChainDirection direction, Board board) {
        int currentPlayerTokens = 0;
        int otherPlayerTokens = 0;

        for (int i = 0; i < 4; i++) {
            int cellPlayerId = board.getCellPlayerId(cell.getRow(), cell.getColumn());
            if (cellPlayerId == board.getCurrentPlayerId()) {
                currentPlayerTokens++;
            } else if (cellPlayerId == board.getOtherPlayerId()){
                otherPlayerTokens++;
            }
            cell = direction.nextCell(cell);
        }

        if (currentPlayerTokens != 0 && otherPlayerTokens == 0) {
            return currentPlayerTokens * currentPlayerTokens;
        } else if (currentPlayerTokens == 0 && otherPlayerTokens != 0) {
            return -(otherPlayerTokens * otherPlayerTokens);
        } else {
            return 0;
        }
    }

    @Value
    private static final class ChainDirection {
        private final int rowDirection;
        private final int columnDirection;

        public ChainDirection reverse() {
            return new ChainDirection(-rowDirection, -columnDirection);
        }

        public Cell nextCell(Cell cell) {
            return new Cell(cell.getRow() + rowDirection, cell.getColumn() + columnDirection);
        }
    }

}
