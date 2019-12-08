package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.FocusedEvaluationFunction;

public class FocusedAlphaBetaPlayer extends MinimaxBasedPlayer {
    public FocusedAlphaBetaPlayer(int depth) {
        super(new AlphaBetaSearchFunction(), new CachingEvaluationFunction(new FocusedEvaluationFunction()), depth);
    }
}
