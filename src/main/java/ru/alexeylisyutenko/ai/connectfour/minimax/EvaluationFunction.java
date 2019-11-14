package ru.alexeylisyutenko.ai.connectfour.minimax;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

/**
 * Interface that represents a board evaluation function.
 */
public interface EvaluationFunction {
    /**
     * Evaluate a board.
     *
     * @param board a board to evaluate
     * @return a score of board
     */
    int evaluate(Board board);
}
