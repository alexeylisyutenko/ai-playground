package ru.alexeylisyutenko.ai.connectfour.minimax;

import lombok.Value;

/**
 * Represents a move used in search functions {@link SearchFunction}.
 */
@Value
public final class Move {
    /**
     * Connect Four Game column number.
     */
    private final int column;

    /**
     * Score for this move.
     */
    private final int score;
}
