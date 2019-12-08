package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BasicEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MultithreadedMinimaxSearchFunction;

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
