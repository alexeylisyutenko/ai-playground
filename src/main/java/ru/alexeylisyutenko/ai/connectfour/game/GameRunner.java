package ru.alexeylisyutenko.ai.connectfour.game;

import java.util.List;

/**
 * Runs a game of Connect Four.
 */
public interface GameRunner {
    /**
     * Returns the first player of the game.
     *
     * @return the first player of the game
     */
    Player getPlayer1();

    /**
     * Returns the second player of the game.
     *
     * @return the second player of the game
     */

    Player getPlayer2();

    /**
     * Returns current timeout in seconds within which a player must make a move.
     *
     * @return timeout in seconds within which a player must make a move
     */
    int getTimeLimit();

    /**
     * Returns the current game state.
     *
     * @return current game state
     */
    GameState getState();

    /**
     * Returns board history of the current game, or of the last game if the state is STOPPED.
     *
     * @return board history
     */
    List<Board> getBoardHistory();

    /**
     * Start a game.
     */
    void startGame();

    /**
     * Stop a game.
     */
    void stopGame();
}
