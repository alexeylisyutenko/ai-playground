package ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.StoppableSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BestEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal.TranspositionTableYBWCAlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.timeoutbasedsearchfunction.DefaultTimeoutBasedSearchFunction;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ForkJoinPool;

import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.constructRandomNonFinishedBoard;

class DefaultTimeoutBasedSearchFunctionTest {
    private final ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();

    @Test
    void stoppableSearchDemo() throws InterruptedException {
        Board board = constructRandomNonFinishedBoard(0, 5);
        consoleBoardVisualizer.visualize(board);
        System.out.println("Current player: " +board.getCurrentPlayerId());
        System.out.println();

        ConcurrentMap<Board, TranspositionTableYBWCAlphaBetaSearchFunction.TranspositionTableEntry> transpositionTable = new ConcurrentHashMap<>();
        ConcurrentMap<Board, TranspositionTableYBWCAlphaBetaSearchFunction.BestMoveTableEntry> bestMovesTable = new ConcurrentHashMap<>();
        StoppableSearchFunction stoppableSearchFunction = new TranspositionTableYBWCAlphaBetaSearchFunction(transpositionTable, bestMovesTable, ForkJoinPool.commonPool());
        DefaultTimeoutBasedSearchFunction defaultStoppableSearch = new DefaultTimeoutBasedSearchFunction(stoppableSearchFunction);

        LocalDateTime searchStarted = LocalDateTime.now();
        System.out.println("Search search started: " + searchStarted);

        Optional<Move> moveOptional = defaultStoppableSearch.search(board, 15, new BestEvaluationFunction(), 3000);

        LocalDateTime searchStopped = LocalDateTime.now();
        System.out.println("Search search stopped: " + searchStopped);
        long millisBetween = Duration.between(searchStarted, searchStopped).toMillis();
        System.out.println(String.format("It took: %d ms", millisBetween));
        System.out.println("Move: " + moveOptional);
    }

}