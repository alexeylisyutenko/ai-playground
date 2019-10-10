package ru.alexeylisyutenko.ai.connectfour.game;

/**
 * Connect four main player.
 */
public interface Player {
    /**
     * Called by {@link GameRunner} when a main starts.
     *
     * @param playerId this player id
     */
    void gameStarted(int playerId);

    /**
     * This method is called by {@link GameRunner} when it's this player's turn to make a move.
     * In response this player must call {@link GameContext#makeMove(int)} method to make an appropriate move.
     * A move must be made within some time limit, otherwise this player loses the main because of timeout.
     *
     * @param gameContext object that allows this player to make moves
     */
    void requestMove(GameContext gameContext);

    /**
     * Called by {@link GameRunner} when main is finished.
     *
     * @param gameResult result of the main
     */
    void gameFinished(GameResult gameResult);
}
