package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.minimax.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.FocusedEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MultithreadedMinimaxSearchFunction;

public class MultithreadedFocusedMinimaxPlayer extends MinimaxBasedPlayer {
    public MultithreadedFocusedMinimaxPlayer(int depth) {
        super(new MultithreadedMinimaxSearchFunction(), new CachingEvaluationFunction(new FocusedEvaluationFunction()), depth);
    }

    public MultithreadedFocusedMinimaxPlayer() {
        this(9);
    }

    @Override
    public void gameStarted(int playerId) {
        super.gameStarted(playerId);
        ((CachingEvaluationFunction) evaluationFunction).clearCache();
    }
}
