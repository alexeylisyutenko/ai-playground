package ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BestEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CountingEvaluationFunction;

import java.time.Duration;
import java.time.LocalDateTime;

import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.constructRandomNonFinishedBoard;

class IterativeDeepeningSearchFunctionTest {

    private final ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();

    @Test
    void searchDemo() {
        Board board = constructRandomNonFinishedBoard(0, 5);
        consoleBoardVisualizer.visualize(board);
        System.out.println("Current player: " + board.getCurrentPlayerId());
        System.out.println();

        IterativeDeepeningSearchFunction iterativeDeepeningSearchFunction = new IterativeDeepeningSearchFunction(5000);

        LocalDateTime searchStarted = LocalDateTime.now();
        System.out.println("Search search started: " + searchStarted);

        Move move = iterativeDeepeningSearchFunction.search(board, 100, new CountingEvaluationFunction(new BestEvaluationFunction()));

        LocalDateTime searchStopped = LocalDateTime.now();
        System.out.println("Search search stopped: " + searchStopped);
        long millisBetween = Duration.between(searchStarted, searchStopped).toMillis();
        System.out.println(String.format("It took: %d ms", millisBetween));
        System.out.println("Move: " + move);
    }

}