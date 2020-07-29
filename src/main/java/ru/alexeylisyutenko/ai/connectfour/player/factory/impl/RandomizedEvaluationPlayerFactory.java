package ru.alexeylisyutenko.ai.connectfour.player.factory.impl;

import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.RandomizedEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.player.MinimaxBasedPlayer;
import ru.alexeylisyutenko.ai.connectfour.player.factory.AbstractPlayerFactory;
import ru.alexeylisyutenko.ai.connectfour.player.factory.PlayerFactory;

import java.util.Map;
import java.util.Objects;

public class RandomizedEvaluationPlayerFactory extends AbstractPlayerFactory {
    @Override
    public String getImplementationName() {
        return "Randomized evaluation player";
    }

    @Override
    public boolean isDepthArgumentRequired() {
        return true;
    }

    @Override
    public boolean isTimeoutArgumentRequited() {
        return false;
    }

    @Override
    public Player create(Integer depth, Integer timeout, Map<String, ?> additionalArguments) {
        Objects.requireNonNull(depth, "depth cannot be null");
        return new MinimaxBasedPlayer(new AlphaBetaSearchFunction(), new RandomizedEvaluationFunction(), depth);
    }
}
