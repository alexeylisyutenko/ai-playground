package ru.alexeylisyutenko.ai.connectfour.runner;

import ru.alexeylisyutenko.ai.connectfour.player.Player;

// TODO: Add javadoc.

public interface GameRunner {
    Player getPlayer1();

    Player getPlayer2();

    int getTimeLimit();

    GameState getState();

    void startGame();

    void stopGame();
}
