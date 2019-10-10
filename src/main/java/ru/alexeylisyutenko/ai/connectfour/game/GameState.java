package ru.alexeylisyutenko.ai.connectfour.game;

/**
 * Class that represents a current state of a game.
 */
public enum GameState {
    /**
     * Game is stopped and a client can start a game by calling the {@link GameRunner#startGame()} method.
     */
    STOPPED,

    /**
     * It is the first player's turn to make a move.
     */
    WAITING_FOR_PLAYER1_MOVE,

    /**
     * It is the second player's turn to make a move.
     */
    WAITING_FOR_PLAYER2_MOVE
}
