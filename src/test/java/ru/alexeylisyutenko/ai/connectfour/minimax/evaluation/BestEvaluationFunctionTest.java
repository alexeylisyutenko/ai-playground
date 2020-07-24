package ru.alexeylisyutenko.ai.connectfour.minimax.evaluation;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers;
import ru.alexeylisyutenko.ai.connectfour.util.BoardGenerators;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BestEvaluationFunctionTest {
    @RepeatedTest(10000)
    void bestEvaluationFunctionMustProduceSameResultsAsEventBetter() {
        BestEvaluationFunction bestEvaluationFunction = new BestEvaluationFunction();
        EvenBetterEvaluationFunction evenBetterEvaluationFunction = new EvenBetterEvaluationFunction();
        Board board = BoardGenerators.constructRandomNonFinishedBoard();
        int bestScore = bestEvaluationFunction.evaluate(board);
        int evenBetterScore = evenBetterEvaluationFunction.evaluate(board);
        assertEquals(evenBetterScore, bestScore);
    }
}