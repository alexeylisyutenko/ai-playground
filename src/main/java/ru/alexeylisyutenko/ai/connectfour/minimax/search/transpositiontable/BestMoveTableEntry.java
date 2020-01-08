package ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable;

import lombok.Value;

@Value
public class BestMoveTableEntry {
    private final int depth;
    private final int column;
    private final int score;
}
