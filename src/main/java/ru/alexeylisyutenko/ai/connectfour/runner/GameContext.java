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
     *
     * @return
     */
    Board getBoard();

    /**
     *
     * @param column
     */
    void makeMove(int column);
}
