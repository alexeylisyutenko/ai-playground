package ru.alexeylisyutenko.ai.connectfour.player.factory.impl;

import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BetterEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.InternalEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal.TranspositionTableAlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.player.MinimaxBasedPlayer;
import ru.alexeylisyutenko.ai.connectfour.player.factory.AbstractPlayerFactory;

import java.util.Map;
import java.util.Objects;

public class InternalEvaluationPlayerFactory extends AbstractPlayerFactory {
    @Override
    public String getImplementationName() {
        return "Internal evaluation player";
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
        return new MinimaxBasedPlayer(new TranspositionTableAlphaBetaSearchFunction(), new CachingEvaluationFunction(new InternalEvaluationFunction()), depth);
    }
}
