package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BasicEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MinimaxSearchFunction;

public class PlainMinimaxPlayer extends MinimaxBasedPlayer {
    public PlainMinimaxPlayer(int depth) {
        super(new MinimaxSearchFunction(), new BasicEvaluationFunction(), depth);
    }

    public PlainMinimaxPlayer() {
        this(6);
    }
}
