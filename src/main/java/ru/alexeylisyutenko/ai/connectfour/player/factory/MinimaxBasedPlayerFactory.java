package ru.alexeylisyutenko.ai.connectfour.player.factory;

import ru.alexeylisyutenko.ai.connectfour.game.Player;

public abstract class MinimaxBasedPlayerFactory implements PlayerFactory {
    @Override
    public String getImplementationName() {
        return null;
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
    public Player create(Integer depth, Integer timeout) {
        return null;
    }
}
