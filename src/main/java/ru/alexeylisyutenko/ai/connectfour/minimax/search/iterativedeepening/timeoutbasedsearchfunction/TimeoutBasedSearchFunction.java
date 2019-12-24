package ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.timeoutbasedsearchfunction;

import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;

import java.util.Optional;

public interface TimeoutBasedSearchFunction {
    Optional<Move> search(Board board, int depth, EvaluationFunction evaluationFunction, int timeout);
}
