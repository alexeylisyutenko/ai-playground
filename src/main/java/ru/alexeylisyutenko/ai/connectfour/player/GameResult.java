package ru.alexeylisyutenko.ai.connectfour.player;

/**
 * Game result from the player's perspective.
 */
public enum GameResult {
    /**
     * Player has won the game.
     */
    PLAYER_WON,

    /**
     * Player has lost the game.
     */
    PLAYER_LOST,

    /**
     * Player has lost the game because of timeout.
     */
    PLAYER_LOST_TIMEOUT,

    /**
     * The game ended in a tie.
     */
    TIE,

    /**
     * The game was stopped.
     */
    STOPPED
}
