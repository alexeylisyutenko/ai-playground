package ru.alexeylisyutenko.ai.connectfour.demo;

import ru.alexeylisyutenko.ai.connectfour.player.ConsolePlayer;
import ru.alexeylisyutenko.ai.connectfour.runner.DefaultGameRunner;

public class GameDemo {
    public static void main(String[] args) {
        ConsolePlayer player1 = new ConsolePlayer();
        ConsolePlayer player2 = new ConsolePlayer();

        DefaultGameRunner gameRunner = new DefaultGameRunner(player1, player2);
        gameRunner.startGame();
    }
}
