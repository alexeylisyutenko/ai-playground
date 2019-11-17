package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.minimax.BasicEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MultithreadedMinimaxSearchFunction;

public class MultithreadedPlainMinimaxPlayer extends MinimaxBasedPlayer {
    public MultithreadedPlainMinimaxPlayer(int depth) {
        super(new MultithreadedMinimaxSearchFunction(), new CachingEvaluationFunction(new BasicEvaluationFunction()), depth);
    }

    public MultithreadedPlainMinimaxPlayer() {
        this(9);
    }

    @Override
    public void gameStarted(int playerId) {
        super.gameStarted(playerId);
        ((CachingEvaluationFunction) evaluationFunction).clearCache();
    }
}
