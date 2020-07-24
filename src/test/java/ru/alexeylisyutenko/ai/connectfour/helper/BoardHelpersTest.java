package ru.alexeylisyutenko.ai.connectfour.helper;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.util.BoardGenerators;

import static org.junit.jupiter.api.Assertions.*;

class BoardHelpersTest {
    @Test
    void constructRandomNonFinishedBoard() {
        for (int i = 0; i < 100000; i++) {
            BoardGenerators.constructRandomNonFinishedBoard();
        }
    }
}