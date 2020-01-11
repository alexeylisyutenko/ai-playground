package ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CountingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.FocusedEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MinimaxSearchFunction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.generateGenuineGameBoardSequence;

class TranspositionTableYBWCAlphaBetaSearchFunctionTest {

    private SearchFunction minimaxSearchFunction;
    private SearchFunction searchFunctionUnderTest;
    private CountingEvaluationFunction minimaxEvaluationFunction;
    private CountingEvaluationFunction alphaBetaEvaluationFunction;
    private int depth;

    @BeforeEach
    void setup() {
        minimaxSearchFunction = new MinimaxSearchFunction();
        searchFunctionUnderTest = new TranspositionTableYBWCAlphaBetaSearchFunction();

        minimaxEvaluationFunction = new CountingEvaluationFunction(new FocusedEvaluationFunction());
        alphaBetaEvaluationFunction = new CountingEvaluationFunction(new FocusedEvaluationFunction());

        depth = RandomUtils.nextInt(4, 6);
    }

    @Disabled
    @Test
    @RepeatedTest(1000)
    void transpositionTableYbwcAlphaBetaSearchFunctionMustProduceSameMovesAsMinimaxSearchFunction() {
        Board board = BoardHelpers.constructRandomNonFinishedBoard();
        ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();
        consoleBoardVisualizer.visualize(board);

        System.out.println("Depth: " + depth);

        Move minimaxMove = minimaxSearchFunction.search(board, depth, minimaxEvaluationFunction);
        System.out.println("Minimax move: " + minimaxMove);
        System.out.println("Minimax evaluations: " + minimaxEvaluationFunction.getEvaluationsCounter());

        Move alphaBetaMove = searchFunctionUnderTest.search(board, depth, alphaBetaEvaluationFunction);
        System.out.println("Transposition table YBWC AlphaBeta move: " + alphaBetaMove);
        System.out.println("Transposition table YBWC AlphaBeta evaluations: " + alphaBetaEvaluationFunction.getEvaluationsCounter());

        assertEquals(minimaxMove.getColumn(), alphaBetaMove.getColumn());
    }

    @Disabled
    @Test
    @RepeatedTest(50)
    void genuine() {
        List<Board> boards = generateGenuineGameBoardSequence();
        for (Board board : boards) {
            Move minimaxMove = minimaxSearchFunction.search(board, depth, minimaxEvaluationFunction);
            System.out.println("Minimax move: " + minimaxMove);

            Move alphaBetaMove = searchFunctionUnderTest.search(board, depth, alphaBetaEvaluationFunction);
            System.out.println("Transposition table AlphaBeta move: " + alphaBetaMove);

            assertEquals(minimaxMove.getColumn(), alphaBetaMove.getColumn());
        }
    }
}