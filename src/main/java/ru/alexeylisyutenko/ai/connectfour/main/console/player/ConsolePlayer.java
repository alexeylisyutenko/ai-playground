package ru.alexeylisyutenko.ai.connectfour.main.console.player;

import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.game.GameContext;
import ru.alexeylisyutenko.ai.connectfour.game.GameResult;

import java.util.Scanner;

public class ConsolePlayer implements Player {
    private final Scanner scanner = new Scanner(System.in);

    private int playerId;

    @Override
    public void gameStarted(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void requestMove(GameContext gameContext) {
        System.out.print(String.format("Player %d: Pick a column #: --> ", playerId));
        int column = scanner.nextInt();
        gameContext.makeMove(column);
    }

    @Override
    public void gameFinished(GameResult gameResult) {
//        System.out.println(String.format("Player %d: Game finished: %s", playerId, gameResult));
    }
}
