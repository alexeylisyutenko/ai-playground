package ru.alexeylisyutenko.ai.connectfour.runner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameResultTest {

    @Test
    void normalVictoryFactoryMethodMustCreateCorrectGameResultInstance() {
        GameResult gameResult = GameResult.normalVictory(1, 2);
        assertEquals(GameResult.Type.NORMAL_VICTORY, gameResult.getType());
        assertEquals(1, gameResult.getWinnerId());
        assertEquals(2, gameResult.getLoserId());
    }

    @Test
    void timeoutVictoryFactoryMethodMustCreateCorrectGameResultInstance() {
        GameResult gameResult = GameResult.timeoutVictory(1, 2);
        assertEquals(GameResult.Type.TIMEOUT_VICTORY, gameResult.getType());
        assertEquals(1, gameResult.getWinnerId());
        assertEquals(2, gameResult.getLoserId());
    }

    @Test
    void tieFactoryMethodMustCreateCorrectGameResultInstance() {
        GameResult gameResult = GameResult.tie();
        assertEquals(GameResult.Type.TIE, gameResult.getType());
        assertEquals(0, gameResult.getWinnerId());
        assertEquals(0, gameResult.getLoserId());
    }

    @Test
    void stoppedGameFactoryMethodMustCreateCorrectGameResultInstance() {
        GameResult gameResult = GameResult.stoppedGame();
        assertEquals(GameResult.Type.STOPPED_GAME, gameResult.getType());
        assertEquals(0, gameResult.getWinnerId());
        assertEquals(0, gameResult.getLoserId());
    }

}