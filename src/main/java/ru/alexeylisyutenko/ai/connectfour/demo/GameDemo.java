package ru.alexeylisyutenko.ai.connectfour.demo;

import ru.alexeylisyutenko.ai.connectfour.player.impl.ConsolePlayer;
import ru.alexeylisyutenko.ai.connectfour.player.Player;
import ru.alexeylisyutenko.ai.connectfour.player.impl.RandomPlayer;
import ru.alexeylisyutenko.ai.connectfour.runner.DefaultGameRunner;

public class GameDemo {
    public static void main(String[] args) {
        Player player1 = new ConsolePlayer();
        Player player2 = new ConsolePlayer();

        DefaultGameRunner gameRunner = new DefaultGameRunner(player1, player2);
        gameRunner.startGame();
    }
}
