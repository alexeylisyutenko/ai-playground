package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.FocusedEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.YBWCAlphaBetaSearchFunction;

public class YBWCFocusedAlphaBetaPlayer extends MinimaxBasedPlayer {
    public YBWCFocusedAlphaBetaPlayer(int depth) {
        super(new YBWCAlphaBetaSearchFunction(), new CachingEvaluationFunction(new FocusedEvaluationFunction()), depth);
    }
}
