package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.Board;
import ru.alexeylisyutenko.ai.connectfour.runner.GameRunner;
import ru.alexeylisyutenko.ai.connectfour.runner.MoveMaker;

/**
 * Connect four game player.
 */
public interface Player {
    /**
     * Called by {@link GameRunner} to setup current player id.
     *
     * @param playerId this player id
     */
    void setId(int playerId);

    /**
     * This method is called by {@link GameRunner} when it's this player's turn to make a move.
     * In response this player must call {@link GameRunner#makeMove(int)} method to make an appropriate move.
     * The method must be called within some time limit, otherwise this player loses the game because of timeout.
     *
     * @param gameRunner game runner which controls current game
     * @param board      current board
     */
    void requestMove(MoveMaker moveMaker, int timeout, Board board);

    /**
     * Called by {@link GameRunner} when game is finished.
     *
     * @param gameResult result of the game from the perspective of this player.
     */
    void gameFinished(GameResult gameResult);
}
