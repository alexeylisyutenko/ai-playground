package ru.alexeylisyutenko.ai.connectfour.exception;

import lombok.Getter;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

@Getter
public class InvalidMoveException extends RuntimeException {
    private final int column;
    private final Board board;

    public InvalidMoveException(int column, Board board) {
        super(String.format("It is impossible to make a move in column '%d' because it's full", column));
        this.column = column;
        this.board = board;
    }
}
