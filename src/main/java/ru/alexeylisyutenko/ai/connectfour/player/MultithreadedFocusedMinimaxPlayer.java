package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.FocusedEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MultithreadedMinimaxSearchFunction;

public class MultithreadedFocusedMinimaxPlayer extends MinimaxBasedPlayer {
    public MultithreadedFocusedMinimaxPlayer(int depth) {
        super(new MultithreadedMinimaxSearchFunction(), new CachingEvaluationFunction(new FocusedEvaluationFunction()), depth);
    }

    public MultithreadedFocusedMinimaxPlayer() {
        this(4);
    }

    @Override
    public void gameStarted(int playerId) {
        super.gameStarted(playerId);
        ((CachingEvaluationFunction) evaluationFunction).clearCache();
    }
}
