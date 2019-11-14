package ru.alexeylisyutenko.ai.connectfour.minimax;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

/**
 *
 */
public interface SearchFunction {
    /**
     *
     * @param board
     * @param depth
     * @param evaluationFunction
     * @return
     */
    int search(Board board, int depth, EvaluationFunction evaluationFunction);
}
