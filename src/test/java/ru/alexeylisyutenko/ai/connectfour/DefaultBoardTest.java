package ru.alexeylisyutenko.ai.connectfour;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultBoardTest {

    @Test
    void someOperationsDemo() {
        Board board = new DefaultBoard();
        board.visualize();

        System.out.println();
        System.out.println(board.getHeightOfColumn(0));
    }



}