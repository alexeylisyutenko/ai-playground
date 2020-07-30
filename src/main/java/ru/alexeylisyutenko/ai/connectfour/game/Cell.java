package ru.alexeylisyutenko.ai.connectfour.game;

import lombok.Value;

/**
 * Class which contains cell coordinates on a Connect-Four board.
 */
@Value
public class Cell {
    int row;
    int column;

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}