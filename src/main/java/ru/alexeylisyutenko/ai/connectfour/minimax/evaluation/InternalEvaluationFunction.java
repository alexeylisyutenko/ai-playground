package ru.alexeylisyutenko.ai.connectfour.minimax.evaluation;

import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;

public class InternalEvaluationFunction implements EvaluationFunction {
    @Override
    public int evaluate(Board board) {
        return board.internalEvaluate();
    }
}
