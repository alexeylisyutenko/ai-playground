package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.game.GameResult;
import ru.alexeylisyutenko.ai.connectfour.game.Player;

/**
 * Abstract player class which implement some basic player functionality like saving playerId when
 * {@link Player#gameStarted(int)} is called.
 */
public abstract class AbstractPlayer implements Player {
    protected int playerId;

    @Override
    public void gameStarted(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void gameFinished(GameResult gameResult) {
    }
}
