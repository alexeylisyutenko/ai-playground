package ru.alexeylisyutenko.ai.connectfour.dataset.model;

import lombok.Value;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

@Value
public class BoardWithMove {
    Board board;
    int move;
}
