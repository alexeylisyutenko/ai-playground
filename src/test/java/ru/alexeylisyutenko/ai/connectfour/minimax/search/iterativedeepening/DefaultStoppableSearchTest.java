package ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BestEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.stoppablesearch.DefaultStoppableSearch;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.constructRandomNonFinishedBoard;

class DefaultStoppableSearchTest {
    private final ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();

    @Test
    void stoppableSearchDemo() throws InterruptedException {
        Board board = constructRandomNonFinishedBoard(0, 5);
        consoleBoardVisualizer.visualize(board);
        System.out.println("Current player: " +board.getCurrentPlayerId());
        System.out.println();

        DefaultStoppableSearch defaultStoppableSearch = new DefaultStoppableSearch();

        LocalDateTime searchStarted = LocalDateTime.now();
        System.out.println("Search search started: " + searchStarted);

        Optional<Move> moveOptional = defaultStoppableSearch.search(board, 20, new BestEvaluationFunction(), 1000);

        LocalDateTime searchStopped = LocalDateTime.now();
        System.out.println("Search search stopped: " + searchStopped);
        long millisBetween = Duration.between(searchStarted, searchStopped).toMillis();
        System.out.println(String.format("It took: %d ms", millisBetween));
        System.out.println("Move: " + moveOptional);
    }

}