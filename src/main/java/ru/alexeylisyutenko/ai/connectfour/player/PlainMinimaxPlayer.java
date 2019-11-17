package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.minimax.BasicEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxSearchFunction;

public class PlainMinimaxPlayer extends MinimaxBasedPlayer {
    public PlainMinimaxPlayer() {
        super(new MinimaxSearchFunction(), new CachingEvaluationFunction(new BasicEvaluationFunction()), 7);
    }
}
