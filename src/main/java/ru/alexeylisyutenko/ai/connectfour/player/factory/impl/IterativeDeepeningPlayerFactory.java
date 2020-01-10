package ru.alexeylisyutenko.ai.connectfour.player.factory.impl;

import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.InternalEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.IterativeDeepeningSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.player.MinimaxBasedPlayer;
import ru.alexeylisyutenko.ai.connectfour.player.factory.AbstractPlayerFactory;
import ru.alexeylisyutenko.ai.connectfour.player.factory.PlayerFactory;

import java.util.Map;
import java.util.Objects;

public class IterativeDeepeningPlayerFactory extends AbstractPlayerFactory {
    @Override
    public String getImplementationName() {
        return "Iterative deepening";
    }

    @Override
    public boolean isDepthArgumentRequired() {
        return false;
    }

    @Override
    public boolean isTimeoutArgumentRequited() {
        return true;
    }

    @Override
    public Player create(Integer depth, Integer timeout, Map<String, ?> additionalArguments) {
        Objects.requireNonNull(timeout, "timeout cannot be null");
        if (timeout < 0) {
            throw new IllegalArgumentException("Illegal timeout: " + timeout);
        }
        return new MinimaxBasedPlayer(new IterativeDeepeningSearchFunction(timeout), new CachingEvaluationFunction(new InternalEvaluationFunction()), 1);
    }
}
