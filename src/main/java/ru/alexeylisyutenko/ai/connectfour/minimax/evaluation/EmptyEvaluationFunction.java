package ru.alexeylisyutenko.ai.connectfour.minimax.evaluation;

import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;

public class EmptyEvaluationFunction implements EvaluationFunction {
    @Override
    public int evaluate(Board board) {
        if (board.isGameOver()) {
            return  -2000 + board.getNumberOfTokensOnBoard();
        } else {
            return 0;
        }
    }
}
