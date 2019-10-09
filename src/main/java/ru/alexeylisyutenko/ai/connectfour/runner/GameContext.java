package ru.alexeylisyutenko.ai.connectfour.runner;

import ru.alexeylisyutenko.ai.connectfour.Board;
import ru.alexeylisyutenko.ai.connectfour.player.Player;

/**
 * An interface of an object passed to players {@link Player} when a move is requested.
 */
public interface GameContext {
    /**
     * Returns current timeout in seconds within which a player must make a move.
     *
     * @return timeout in seconds within which a player must make a move
     */
    int getTimeout();

    /**
     * Returns current board state.
     *
     * @return current board state
     */
    Board getBoard();

    /**
     * {@link Player} must call this method to make a move when it is player's turn to make a move.
     *
     * @param column a column where a player wants to place a token
     */
    void makeMove(int column);
}
