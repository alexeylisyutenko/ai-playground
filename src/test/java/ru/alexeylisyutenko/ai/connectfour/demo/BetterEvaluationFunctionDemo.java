package ru.alexeylisyutenko.ai.connectfour.demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.DefaultBoard;
import ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BetterEvaluationFunction;

import java.util.List;

import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.constructBoardArray;

public class BetterEvaluationFunctionDemo {

    @Disabled
    @Test
    void evaluateSomeBoards() {
        int boards = 5;
        for (int i = 0; i < boards; i++) {
            Board board = BoardHelpers.constructRandomNonFinishedBoard(2, 20);

            ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();
            consoleBoardVisualizer.visualize(board);
            System.out.println();

            String currentPlayer = board.getCurrentPlayerId() == 1 ? "☻" : "☺";
            System.out.println("Current player = " + currentPlayer);
            BetterEvaluationFunction betterEvaluationFunction = new BetterEvaluationFunction();
            System.out.println("Score = " + betterEvaluationFunction.evaluate(board));
            System.out.println();
        }
    }

    @Disabled
    @Test
    void evaluateSomeOtherBoards() {
        List<Board> boards = List.of(
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 2, 0, 0, 0, 1, 2},
                        {0, 2, 1, 1, 0, 1, 2}
                }), 1),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 2, 1},
                        {0, 0, 0, 0, 0, 1, 2},
                        {0, 0, 0, 0, 0, 1, 2}
                }), 1),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 1, 0, 0, 0}
                }), 1),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 1, 0, 0, 0, 0}
                }), 1)
        );

        for (Board board : boards) {
            ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();
            consoleBoardVisualizer.visualize(board);
            System.out.println();

            String currentPlayer = board.getCurrentPlayerId() == 1 ? "☻" : "☺";
            System.out.println("Current player = " + currentPlayer);
            BetterEvaluationFunction betterEvaluationFunction = new BetterEvaluationFunction();
            System.out.println("Score = " + betterEvaluationFunction.evaluate(board));
            System.out.println();
        }
    }

}
