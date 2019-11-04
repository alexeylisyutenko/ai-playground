package ru.alexeylisyutenko.ai.connectfour.demo;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.DefaultGameRunner;
import ru.alexeylisyutenko.ai.connectfour.player.RandomPlayer;

public class DefaultGameRunnerDemo {

    public static void main(String[] args) {
        RandomPlayer player1 = new RandomPlayer();
        RandomPlayer player2 = new RandomPlayer();

        DefaultGameRunner defaultGameRunner = new DefaultGameRunner(player1, player2, null);

        defaultGameRunner.startGame();
//        defaultGameRunner.stopGame();
    }
}
