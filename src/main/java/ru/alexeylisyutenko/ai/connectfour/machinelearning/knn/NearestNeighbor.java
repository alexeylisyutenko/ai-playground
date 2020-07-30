package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

/**
 * K Nearest Neighbors model predicting Connect Four moves.
 */
public interface NearestNeighbor {
    /**
     * Predict a move.
     *
     * @param board current board
     * @param k number of nearest neighbors
     * @return predicted move
     */
    int predict(Board board, int k);
}
