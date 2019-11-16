package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.minimax.BasicEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxSearchFunction;

public class MinimaxPlayer extends SearchFunctionBasedPlayer {
    public MinimaxPlayer() {
        super(new MinimaxSearchFunction(), new BasicEvaluationFunction(), 6);
    }
}
