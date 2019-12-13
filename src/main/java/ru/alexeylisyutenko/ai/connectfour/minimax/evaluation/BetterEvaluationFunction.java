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

// TODO: Refactor the code.
// TODO: Create a class to represent a chain?
// TODO: Take into consideration chains of length 1.

public class BetterEvaluationFunction implements EvaluationFunction {
    @Override
    public int evaluate(Board board) {
        int score;
        if (board.isGameOver()) {
            score = -10000 + board.getNumberOfTokensOnBoard();
        } else {
            score = 0;

            // This simple approach works poorly? We need to improve it.
            Set<List<Cell>> currentPlayerChains = board.getChainCells(board.getCurrentPlayerId()).stream()
                    .filter(cells -> cells.size() > 1).collect(Collectors.toSet());
            for (List<Cell> chain : currentPlayerChains) {
                score += getChainScore(chain, board);
            }
            Set<List<Cell>> otherPlayerChains = board.getChainCells(board.getOtherPlayerId()).stream()
                    .filter(cells -> cells.size() > 1).collect(Collectors.toSet());
            for (List<Cell> chain : otherPlayerChains) {
                score -= getChainScore(chain, board);
            }

            // Prefer having pieces in the center of the board.
            for (int row = 0; row < BOARD_HEIGHT; row++) {
                for (int column = 0; column < BOARD_WIDTH; column++) {
                    if (board.getCellPlayerId(row, column) == board.getCurrentPlayerId()) {
                        score -= Math.abs(3 - column);
                    } else if (board.getCellPlayerId(row, column) == board.getOtherPlayerId()) {
                        score += Math.abs(3 - column);
                    }
                }
            }
        }
        return score;
    }

    private int getChainScore(List<Cell> chain, Board board) {
        int chainScore = 0;

        // Get player's id for the chain we're evaluating.
        Cell cell = chain.get(0);
        int playerId = board.getCellPlayerId(cell.getRow(), cell.getColumn());
        int otherPlayerId = playerId == 1 ? 2 : 1;

        Cell firstCell = chain.get(0);
        Cell lastCell = chain.get(chain.size() - 1);

        // Evaluate left chain direction. Check if it is possible to build 4 in this direction.
        ChainDirection leftDirection = getLeftChainDirection(chain);
        int leftAvailableLength = getDirectionalAvailableLength(board,
                firstCell.getRow() + leftDirection.getRowDirection(),
                firstCell.getColumn() + leftDirection.getColumnDirection(),
                leftDirection,
                otherPlayerId);
        if (leftAvailableLength >= 4 - chain.size()) {
            chainScore += chain.size() * 10;
        }

        // Evaluate right chain direction. Check if it is possible to build 4 in this direction.
        ChainDirection rightDirection = leftDirection.reverse();
        int rightAvailableLength = getDirectionalAvailableLength(board,
                lastCell.getRow() + rightDirection.getRowDirection(),
                lastCell.getColumn() + rightDirection.getColumnDirection(),
                rightDirection,
                otherPlayerId);
        if (rightAvailableLength >= 4 - chain.size()) {
            chainScore += chain.size() * 10;
        }

        return chainScore;
    }

    private ChainDirection getLeftChainDirection(List<Cell> chain) {
        if (chain.size() < 2) {
            throw new IllegalArgumentException("Incorrect chain size: " + chain.size());
        }

        Cell firstCell = chain.get(0);
        Cell secondCell = chain.get(1);
        int columnDirection = firstCell.getColumn() - secondCell.getColumn();
        int rowDirection = firstCell.getRow() - secondCell.getRow();
        return new ChainDirection(rowDirection, columnDirection);
    }

    private int getDirectionalAvailableLength(Board board, int row, int column,
                                              ChainDirection direction, int otherPlayerId) {
        int availableLength = 0;
        while (isValidCell(row, column) &&
                board.getCellPlayerId(row, column) != otherPlayerId) {

            availableLength++;
            row += direction.getRowDirection();
            column += direction.getColumnDirection();
        }
        return availableLength;
    }

    private boolean isValidCell(int row, int column) {
        if (row < 0 || row >= BOARD_HEIGHT) {
            return false;
        }
        if (column < 0 || column >= BOARD_WIDTH) {
            return false;
        }
        return true;
    }

    private ChainDirection getChainDirection(List<Cell> chain) {
        if (chain.size() < 2) {
            throw new IllegalArgumentException("There is direction for a chain whose size is less than 2");
        }
        Cell firstCell = chain.get(0);
        Cell secondCell = chain.get(1);

        return new ChainDirection(secondCell.getColumn() - firstCell.getColumn(), secondCell.getRow() - firstCell.getRow());
    }

    @Value
    private static final class ChainDirection {
        private final int rowDirection;
        private final int columnDirection;

        public ChainDirection reverse() {
            return new ChainDirection(-rowDirection, -columnDirection);
        }
    }
}
