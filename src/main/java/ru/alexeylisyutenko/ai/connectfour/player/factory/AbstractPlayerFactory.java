package ru.alexeylisyutenko.ai.connectfour.player.factory;

public abstract class AbstractPlayerFactory implements PlayerFactory {
    @Override
    public boolean isDepthArgumentRequired() {
        return false;
    }

    @Override
    public boolean isTimeoutArgumentRequited() {
        return false;
    }

    @Override
    public String toString() {
        return getImplementationName();
    }
}
