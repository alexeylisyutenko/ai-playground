package ru.alexeylisyutenko.ai.connectfour.runner;

import ru.alexeylisyutenko.ai.connectfour.player.Player;

public interface GameRunner {
    Player getPlayer1();

    Player getPlayer2();

    int getTimeLimit();

    void startGame();

    void stopGame();
}
