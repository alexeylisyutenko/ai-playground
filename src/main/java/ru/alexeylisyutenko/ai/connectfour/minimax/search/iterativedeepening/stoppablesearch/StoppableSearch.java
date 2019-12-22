package ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.stoppablesearch;

import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

public interface StoppableSearch {
    Optional<Move> search(Board board, int depth, EvaluationFunction evaluationFunction, int timeout);
}
