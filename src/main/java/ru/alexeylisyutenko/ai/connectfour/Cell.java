package ru.alexeylisyutenko.ai.connectfour;

import lombok.Value;

/**
 * Class which contains cell coordinates on a Connect-Four board.
 */
@Value
public final class Cell {
    private final int row;
    private final int column;

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}