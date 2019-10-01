package ru.alexeylisyutenko.ai.connectfour;

import org.junit.jupiter.api.Test;

class DefaultBoardTest {

    BoardVisualizer visualizer = new ConsoleBoardVisualizer();

    @Test
    void someOperationsDemo() {
        Board board = new DefaultBoard();

        visualizer.visualize(board);

        System.out.println();
        System.out.println(board.getHeightOfColumn(0));

        Board board1 = board.makeMove(3).makeMove(3).makeMove(0);
        visualizer.visualize(board1);

        System.out.println();
        System.out.println(board1.getTopEltInColumn(0));
    }

}