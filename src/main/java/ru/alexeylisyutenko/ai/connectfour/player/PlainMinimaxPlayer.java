package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.minimax.BasicEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxSearchFunction;

public class PlainMinimaxPlayer extends MinimaxBasedPlayer {
    public PlainMinimaxPlayer(int depth) {
        super(new MinimaxSearchFunction(), new BasicEvaluationFunction(), depth);
    }

    public PlainMinimaxPlayer() {
        this(6);
    }
}
