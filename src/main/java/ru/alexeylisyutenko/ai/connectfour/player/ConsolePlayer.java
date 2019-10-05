package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.Board;
import ru.alexeylisyutenko.ai.connectfour.runner.MoveMaker;

import java.util.Scanner;

public class ConsolePlayer implements Player {
    private final Scanner scanner = new Scanner(System.in);

    private int playerId;

    @Override
    public void setId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void requestMove(MoveMaker moveMaker, int timeout, Board board) {
        System.out.print(String.format("Player %d: Pick a column #: --> ", playerId));
        int column = scanner.nextInt();
        moveMaker.makeMove(column);
    }

    @Override
    public void gameFinished(GameResult gameResult) {
        System.out.println(String.format("Player %d: Game finished: %s", playerId, gameResult));
    }
}
