package ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable;

import lombok.Value;

@Value
public class TranspositionTableEntry {
    private final int depth;
    private final TranspositionTableEntryType type;
    private final int value;
}
