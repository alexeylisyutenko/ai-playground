package ru.alexeylisyutenko.ai.connectfour.minimax;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class BasicEvaluationFunction implements EvaluationFunction {
    @Override
    public int evaluate(Board board) {
        int score;
        if (board.isGameOver()) {
            // If game is over then the winning move was made by our opponent, so we can't win and return
            // the lowest possible score for this board.
            score = -1000;
        } else {
            score = board.getLongestChain(board.getCurrentPlayerId()) * 10;

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

            score = 0;
        }
        return score;
    }
}
