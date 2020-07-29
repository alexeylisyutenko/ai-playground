package ru.alexeylisyutenko.ai.connectfour.player.factory.impl;

import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.player.RandomPlayer;
import ru.alexeylisyutenko.ai.connectfour.player.factory.AbstractPlayerFactory;
import ru.alexeylisyutenko.ai.connectfour.player.factory.PlayerFactory;

import java.util.Map;

public class RandomPlayerFactoryFactory extends AbstractPlayerFactory {
    @Override
    public String getImplementationName() {
        return "Random player";
    }

    @Override
    public boolean isDepthArgumentRequired() {
        return false;
    }

    @Override
    public boolean isTimeoutArgumentRequited() {
        return false;
    }

    @Override
    public Player create(Integer depth, Integer timeout, Map<String, ?> additionalArguments) {
        return new RandomPlayer();
    }
}
