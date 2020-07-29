package ru.alexeylisyutenko.ai.connectfour.player.factory.impl;

import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.player.KNearestNeighborsPlayer;
import ru.alexeylisyutenko.ai.connectfour.player.factory.AbstractPlayerFactory;

import java.util.Map;

public class NearestNeighborsPlayerFactory extends AbstractPlayerFactory {
    @Override
    public String getImplementationName() {
        return "K nearest neighbors player";
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
        return new KNearestNeighborsPlayer();
    }
}
