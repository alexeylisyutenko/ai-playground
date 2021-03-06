package ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CountingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.FocusedEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MinimaxSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.util.BoardGenerators;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TranspositionTableAlphaBetaSearchFunctionTest {

    private SearchFunction minimaxSearchFunction;
    private SearchFunction searchFunctionUnderTest;
    private CountingEvaluationFunction minimaxEvaluationFunction;
    private CountingEvaluationFunction alphaBetaEvaluationFunction;
    private int depth;

    @BeforeEach
    void setup() {
        minimaxSearchFunction = new MinimaxSearchFunction();
        searchFunctionUnderTest = new TranspositionTableAlphaBetaSearchFunction();

        minimaxEvaluationFunction = new CountingEvaluationFunction(new FocusedEvaluationFunction());
        alphaBetaEvaluationFunction = new CountingEvaluationFunction(new FocusedEvaluationFunction());

        depth = RandomUtils.nextInt(4, 6);
    }

    @Disabled
    @RepeatedTest(1000)
    void transpositionTableYbwcAlphaBetaSearchFunctionMustProduceSameMovesAsMinimaxSearchFunction() {
        Board board = BoardGenerators.constructRandomNonFinishedBoard();
        ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();
        consoleBoardVisualizer.visualize(board);

        System.out.println("Depth: " + depth);

        Move minimaxMove = minimaxSearchFunction.search(board, depth, minimaxEvaluationFunction);
        System.out.println("Minimax move: " + minimaxMove);
        System.out.println("Minimax evaluations: " + minimaxEvaluationFunction.getEvaluationsCounter());

        Move alphaBetaMove = searchFunctionUnderTest.search(board, depth, alphaBetaEvaluationFunction);
        System.out.println("Transposition table AlphaBeta move: " + alphaBetaMove);
        System.out.println("Transposition table AlphaBeta evaluations: " + alphaBetaEvaluationFunction.getEvaluationsCounter());

        assertEquals(minimaxMove, alphaBetaMove);
    }

    @Disabled
    @RepeatedTest(10)
    void genuine() {
        List<Board> boards = BoardGenerators.generateGenuineGameBoardSequence();
        for (Board board : boards) {
            Move minimaxMove = minimaxSearchFunction.search(board, depth, minimaxEvaluationFunction);
            System.out.println("Minimax move: " + minimaxMove);

            Move alphaBetaMove = searchFunctionUnderTest.search(board, depth, alphaBetaEvaluationFunction);
            System.out.println("Transposition table AlphaBeta move: " + alphaBetaMove);

//            assertEquals(minimaxMove, alphaBetaMove);
            assertEquals(minimaxMove.getColumn(), alphaBetaMove.getColumn());
        }
    }

}