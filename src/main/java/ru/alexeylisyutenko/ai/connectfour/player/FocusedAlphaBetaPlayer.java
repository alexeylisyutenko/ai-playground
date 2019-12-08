package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.minimax.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.FocusedEvaluationFunction;

public class FocusedAlphaBetaPlayer extends MinimaxBasedPlayer {
    public FocusedAlphaBetaPlayer(int depth) {
        super(new AlphaBetaSearchFunction(), new FocusedEvaluationFunction(), depth);
    }
}
