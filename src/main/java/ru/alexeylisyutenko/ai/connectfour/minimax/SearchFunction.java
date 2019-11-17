package ru.alexeylisyutenko.ai.connectfour.minimax;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

/**
 * Best move search function whose implementations represent variations of the minimax algorithm.
 */
public interface SearchFunction {
    /**
     * Search for the best move for the current Connect Four Board.
     *
     * @param board the Connect Four Board instance to evaluate
     * @param depth the depth of the search tree (measured in maximum distance from a leaf to the root)
     * @param evaluationFunction the evaluation function to use to give a value to a leaf of the tree
     * @return the column number of the column that the search determines you should add a token to, and the score for that move
     */
    BestMove search(Board board, int depth, EvaluationFunction evaluationFunction);
}
