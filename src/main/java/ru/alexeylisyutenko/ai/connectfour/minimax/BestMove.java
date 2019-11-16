package ru.alexeylisyutenko.ai.connectfour.minimax;

import lombok.Value;

/**
 * Represents the best move for the current board discovered by a search function {@link SearchFunction#search}.
 */
@Value
public final class BestMove {
    private final int column;
    private final int score;
}
