package ru.alexeylisyutenko.ai.connectfour;

import org.junit.jupiter.api.Test;

class DefaultBoardTest {

    BoardVisualizer visualizer = new ConsoleBoardVisualizer();

    @Test
    void someOperationsDemo() {
        Board board = new DefaultBoard();

        Board board1 = board.makeMove(3).makeMove(4).makeMove(3).makeMove(0).makeMove(3).makeMove(1).makeMove(3);
        visualizer.visualize(board1);

        System.out.println();
        System.out.println("Longest chain: " + board1.getLongestChain(1));
        System.out.println("Is game over: " + board1.isGameOver());
        System.out.println("Winner ID: " + board1.getWinnerId());
    }

}