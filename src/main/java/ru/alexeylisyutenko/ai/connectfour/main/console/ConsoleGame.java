package ru.alexeylisyutenko.ai.connectfour.main.console;

import ru.alexeylisyutenko.ai.connectfour.main.console.gamelistener.ConsoleGameEventListener;
import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.main.console.player.ConsolePlayer;
import ru.alexeylisyutenko.ai.connectfour.player.RandomPlayer;
import ru.alexeylisyutenko.ai.connectfour.game.DefaultGameRunner;

public class ConsoleGame {
    public static void main(String[] args) {
        Player player1 = new ConsolePlayer();
        Player player2 = new RandomPlayer();

        DefaultGameRunner gameRunner = new DefaultGameRunner(player1, player2, new ConsoleGameEventListener());
        gameRunner.startGame();
    }
}
