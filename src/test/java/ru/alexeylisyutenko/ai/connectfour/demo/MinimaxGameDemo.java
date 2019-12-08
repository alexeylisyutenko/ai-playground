package ru.alexeylisyutenko.ai.connectfour.demo;

import lombok.Value;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.DefaultGameRunner;
import ru.alexeylisyutenko.ai.connectfour.game.GameResult;
import ru.alexeylisyutenko.ai.connectfour.game.GameRunner;
import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.main.console.gamelistener.ConsoleGameEventListener;
import ru.alexeylisyutenko.ai.connectfour.player.MultithreadedFocusedMinimaxPlayer;

import java.util.concurrent.atomic.AtomicInteger;

public class MinimaxGameDemo {

    @Test
    @Disabled
    void competitionDemo() throws InterruptedException {
        Player player1 = new MultithreadedFocusedMinimaxPlayer(4);
        Player player2 = new MultithreadedFocusedMinimaxPlayer(4);
        CompetitionResult competitionResult = runCompetition(player1, player2, 100);
        System.out.println(competitionResult);
    }

    private CompetitionResult runCompetition(Player player1, Player player2, int gamesNumber) throws InterruptedException {
        CompetitionGameEventListener competitionGameEventListener = new CompetitionGameEventListener();
        GameRunner gameRunner = new DefaultGameRunner(player1, player2, competitionGameEventListener);

        for (int i = 0; i < gamesNumber; i++) {
            gameRunner.startGame();
            gameRunner.awaitGameStart();
            gameRunner.awaitGameStop();
        }

        return new CompetitionResult(competitionGameEventListener.getPlayer1Wins(), competitionGameEventListener.getPlayer2Wins(),
                competitionGameEventListener.getTies(), competitionGameEventListener.getGames());
    }

    private static class CompetitionGameEventListener extends ConsoleGameEventListener {
        private AtomicInteger player1Wins = new AtomicInteger();
        private AtomicInteger player2Wins = new AtomicInteger();
        private AtomicInteger ties = new AtomicInteger();
        private AtomicInteger games = new AtomicInteger();

        @Override
        public void gameFinished(GameRunner gameRunner, GameResult gameResult) {
            super.gameFinished(gameRunner, gameResult);

            games.incrementAndGet();
            if (gameResult.getType() == GameResult.Type.NORMAL_VICTORY) {
                if (gameResult.getWinnerId() == 1) {
                    player1Wins.incrementAndGet();
                } else {
                    player2Wins.incrementAndGet();
                }
            } else if (gameResult.getType() == GameResult.Type.TIE) {
                ties.incrementAndGet();
            }
        }

        public int getPlayer1Wins() {
            return player1Wins.get();
        }

        public int getPlayer2Wins() {
            return player2Wins.get();
        }

        public int getTies() {
            return ties.get();
        }

        public int getGames() {
            return games.get();
        }
    }

    @Value
    private static final class CompetitionResult {
        private final int player1Wins;
        private final int player2Wins;
        private final int ties;
        private final int games;
    }

}
