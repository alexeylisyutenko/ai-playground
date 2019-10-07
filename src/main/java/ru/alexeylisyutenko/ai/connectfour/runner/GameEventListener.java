package ru.alexeylisyutenko.ai.connectfour.runner;

import ru.alexeylisyutenko.ai.connectfour.Board;

/**
 * The listener interface for receiving game events.
 */
public interface GameEventListener {
    /**
     * Invoked when game starts.
     *
     * @param gameRunner a game runner which controls current game
     * @param board an initial board state
     */
    void gameStarted(GameRunner gameRunner, Board board);

    /**
     * Invoked when a player tries to make an illegal move.
     *
     * @param gameRunner a game runner which controls current game
     * @param playerId player's id who tried to make an illegal move
     * @param column a column which a player tried to put a token in
     * @param board a board right before and after illegal move attempt
     */
    void illegalMoveAttempted(GameRunner gameRunner, int playerId, int column, Board board);

    /**
     * Invoked when a game runner requests a player to make a move.
     *
     * @param gameRunner a game runner which controls current game
     * @param playerId player's id whose turn it is to make a move
     * @param board a board before the move
     */
    void moveRequested(GameRunner gameRunner, int playerId, Board board);

    /**
     * Invoked when a move is made.
     *
     * @param gameRunner a game runner which controls current game
     * @param playerId player's id who made a move
     * @param column a column where a player put a token
     * @param board a board right after the player's move
     */
    void moveMade(GameRunner gameRunner, int playerId, int column, Board board);

    /**
     * Invoked when a game is finished.
     *
     * @param gameRunner a game runner which controls current game
     * @param gameResult a game result
     */
    void gameFinished(GameRunner gameRunner, GameResult gameResult);
}
