package ru.alexeylisyutenko.ai.connectfour.game;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;

import java.sql.SQLOutput;

import static org.junit.jupiter.api.Assertions.*;

class BitBoardTest {

    private static ConsoleBoardVisualizer visualizer  = new ConsoleBoardVisualizer();

    @Test
    void demo() {
        Board board = new BitBoard();

        visualizer.visualize(board);
        System.out.println("Current player: " + board.getCurrentPlayerId());
        System.out.println();

        board = board.makeMove(3);
        visualizer.visualize(board);
        System.out.println("Current player: " + board.getCurrentPlayerId());
        System.out.println();

        board = board.makeMove(3);
        visualizer.visualize(board);
        System.out.println("Current player: " + board.getCurrentPlayerId());
        System.out.println();

        board = board.makeMove(3);
        visualizer.visualize(board);
        System.out.println("Current player: " + board.getCurrentPlayerId());
        System.out.println();

        board = board.makeMove(0);
        visualizer.visualize(board);
        System.out.println("Current player: " + board.getCurrentPlayerId());
        System.out.println();

        board = board.makeMove(6);
        visualizer.visualize(board);
        System.out.println("Current player: " + board.getCurrentPlayerId());
        System.out.println();

        board = board.makeMove(3);
        visualizer.visualize(board);
        System.out.println("Current player: " + board.getCurrentPlayerId());
        System.out.println();

        board = board.makeMove(3);
        visualizer.visualize(board);
        System.out.println("Current player: " + board.getCurrentPlayerId());
        System.out.println();

        board = board.makeMove(3);
        visualizer.visualize(board);
        System.out.println("Current player: " + board.getCurrentPlayerId());
        System.out.println();
    }

}