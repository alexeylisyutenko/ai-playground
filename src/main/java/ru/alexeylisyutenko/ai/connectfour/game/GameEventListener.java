package ru.alexeylisyutenko.ai.connectfour.game;

/**
 * The listener interface for receiving main events.
 */
public interface GameEventListener {
    /**
     * Invoked when main starts.
     *
     * @param gameRunner a main runner which controls current main
     * @param board an initial board state
     */
    void gameStarted(GameRunner gameRunner, Board board);

    /**
     * Invoked when a player tries to make an illegal move.
     *
     * @param gameRunner a main runner which controls current main
     * @param playerId player's id who tried to make an illegal move
     * @param column a column which a player tried to put a token in
     * @param board a board right before and after illegal move attempt
     */
    void illegalMoveAttempted(GameRunner gameRunner, int playerId, int column, Board board);

    /**
     * Invoked when a main runner requests a player to make a move.
     *
     * @param gameRunner a main runner which controls current main
     * @param playerId player's id whose turn it is to make a move
     * @param board a board before the move
     */
    void moveRequested(GameRunner gameRunner, int playerId, Board board);

    /**
     * Invoked when a move is made.
     *
     * @param gameRunner a main runner which controls current main
     * @param playerId player's id who made a move
     * @param column a column where a player put a token
     * @param board a board right after the player's move
     */
    void moveMade(GameRunner gameRunner, int playerId, int column, Board board);

    /**
     * Invoked when a main is finished.
     *
     * @param gameRunner a main runner which controls current main
     * @param gameResult a main result
     * @param board a board at the end of a game
     */
    void gameFinished(GameRunner gameRunner, GameResult gameResult, Board board);
}
