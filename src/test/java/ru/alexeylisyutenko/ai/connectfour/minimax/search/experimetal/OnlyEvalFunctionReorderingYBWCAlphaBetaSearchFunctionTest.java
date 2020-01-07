package ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CountingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.FocusedEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MinimaxSearchFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OnlyEvalFunctionReorderingYBWCAlphaBetaSearchFunctionTest {
    @Test
    @RepeatedTest(1000)
    void experimentalYbwcAlphaBetaSearchFunctionMustProduceSameMovesAsMinimaxSearchFunction() {
        int depth = RandomUtils.nextInt(4, 6);

        MinimaxSearchFunction minimaxSearchFunction = new MinimaxSearchFunction();
        OnlyEvalFunctionReorderingYBWCAlphaBetaSearchFunction alphaBetaSearchFunction = new OnlyEvalFunctionReorderingYBWCAlphaBetaSearchFunction();

        CountingEvaluationFunction minimaxEvaluationFunction = new CountingEvaluationFunction(new FocusedEvaluationFunction());
        CountingEvaluationFunction alphaBetaEvaluationFunction = new CountingEvaluationFunction(new FocusedEvaluationFunction());

        Board board = BoardHelpers.constructRandomNonFinishedBoard();
        ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();
        consoleBoardVisualizer.visualize(board);

        System.out.println("Depth: " + depth);

        Move minimaxMove = minimaxSearchFunction.search(board, depth, minimaxEvaluationFunction);
        System.out.println("Minimax move: " + minimaxMove);
        System.out.println("Minimax evaluations: " + minimaxEvaluationFunction.getEvaluationsCounter());

        Move alphaBetaMove = alphaBetaSearchFunction.search(board, depth, alphaBetaEvaluationFunction);
        System.out.println("Experimental YBWC AlphaBeta move: " + alphaBetaMove);
        System.out.println("Experimental YBWC AlphaBeta evaluations: " + alphaBetaEvaluationFunction.getEvaluationsCounter());

        assertEquals(minimaxMove, alphaBetaMove);
    }
}