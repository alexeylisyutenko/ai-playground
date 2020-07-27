package ru.alexeylisyutenko.ai.connectfour.dataset.model;

import lombok.Value;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

/**
 * A Connect Four game board along with a optimal move.
 * <br>
 * Move is represented by a column number where a player should put a token in order to make an optimal move.
 */
@Value
public class BoardWithMove {
    Board board;
    int move;
}
