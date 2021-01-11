package ru.alexeylisyutenko.ai.connectfour.player.factory.impl;

import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.player.NaiveDeepLearningPlayer;
import ru.alexeylisyutenko.ai.connectfour.player.factory.AbstractPlayerFactory;

import java.util.Map;

public class NaiveDeepLearningPlayerFactory extends AbstractPlayerFactory {
    @Override
    public String getImplementationName() {
        return "Naive deep learning player";
    }

    @Override
    public Player create(Integer depth, Integer timeout, Map<String, ?> additionalArguments) {
        return new NaiveDeepLearningPlayer();
    }
}
