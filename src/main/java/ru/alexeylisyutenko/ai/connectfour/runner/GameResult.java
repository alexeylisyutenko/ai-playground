package ru.alexeylisyutenko.ai.connectfour.runner;

import lombok.Getter;
import lombok.ToString;

/**
 * Result of a game.
 *
 * <p>There are several possible outcomes in a single game. Field {@link GameResult#type} represents particular type of
 * an outcome.</p>
 * <p> For example, if a player wins a game by building four consecutive tokens in a row then a {@link GameResult#type}
 * field has a value {@link Type#NORMAL_VICTORY} and fields {@link GameResult#winnerId} and {@link GameResult#loserId}
 * represent winner's id and loser's id correspondingly. Or if a game ended in a tie then {@link GameResult#type} is
 * equal to {@link Type#TIE}.</p>
 */
@Getter
@ToString
public final class GameResult {
    /**
     * Type of a game outcome.
     */
    private final Type type;

    /**
     * An id of a player who is a winner.
     *
     * <p>For types {@link Type#TIE} and {@link Type#STOPPED_GAME} this field is 0.</p>
     */
    private final int winnerId;

    /**
     * An id of a player who is a loser.
     *
     * <p>For types {@link Type#TIE} and {@link Type#STOPPED_GAME} this field is 0.</p>
     */
    private final int loserId;

    private GameResult(Type type, int winnerId, int loserId) {
        this.type = type;
        this.winnerId = winnerId;
        this.loserId = loserId;
    }

    public static GameResult normalVictory(int winnerId, int loserId) {
        return new GameResult(Type.NORMAL_VICTORY, winnerId, loserId);
    }

    public static GameResult timeoutVictory(int winnerId, int loserId) {
        return new GameResult(Type.TIMEOUT_VICTORY, winnerId, loserId);
    }

    public static GameResult tie() {
        return new GameResult(Type.TIE, 0, 0);
    }

    public static GameResult stoppedGame() {
        return new GameResult(Type.STOPPED_GAME, 0, 0);
    }

    /**
     * Type of a game outcome.
     */
    public enum Type {
        /**
         * Normal victory happens when one of the players builds four consecutive tokens in a row.
         */
        NORMAL_VICTORY,

        /**
         * Timeout victory happens when one of the players doesn't make his or her move in time.
         */
        TIMEOUT_VICTORY,

        /**
         * A game ends in a tie when the board is full but there is no winner.
         */
        TIE,

        /**
         * A game was stopped by calling {@link GameRunner#stopGame()} method.
         */
        STOPPED_GAME
    }
}
