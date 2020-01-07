package ru.alexeylisyutenko.ai.connectfour.minimax.helper;

import org.apache.commons.lang3.RandomUtils;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;

public class RandomizedEvaluationFunction implements EvaluationFunction {
    @Override
    public int evaluate(Board board) {
        if (board.isGameOver()) {
            return -1000 + board.getNumberOfTokensOnBoard();
        } else {
            return RandomUtils.nextInt(0, 10) - 5;
        }
    }
}
