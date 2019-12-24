package ru.alexeylisyutenko.ai.connectfour.minimax;

/**
 * Best move search function that can be stopped in the middle of the searching process.
 */
public interface StoppableSearchFunction extends SearchFunction {
    /**
     * Stop all currently started searches.
     */
    void stop();
}
