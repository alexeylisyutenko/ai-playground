package ru.alexeylisyutenko.ai.connectfour.demo;

import ru.alexeylisyutenko.ai.connectfour.console.gamelistener.ConsoleGameEventListener;
import ru.alexeylisyutenko.ai.connectfour.console.player.ConsolePlayer;
import ru.alexeylisyutenko.ai.connectfour.player.Player;
import ru.alexeylisyutenko.ai.connectfour.player.RandomPlayer;
import ru.alexeylisyutenko.ai.connectfour.runner.DefaultGameRunner;

public class GameDemo {
    public static void main(String[] args) {
        Player player1 = new ConsolePlayer();
        Player player2 = new RandomPlayer();

        DefaultGameRunner gameRunner = new DefaultGameRunner(player1, player2, new ConsoleGameEventListener());
        gameRunner.startGame();
    }
}
