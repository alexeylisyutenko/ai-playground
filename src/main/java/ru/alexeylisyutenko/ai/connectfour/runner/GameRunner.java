package ru.alexeylisyutenko.ai.connectfour.runner;

public interface GameRunner {
    int getTimeLimit();

    void runGame();

    void stopGame();
}
