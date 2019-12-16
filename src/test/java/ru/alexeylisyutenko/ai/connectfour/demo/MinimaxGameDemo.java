package ru.alexeylisyutenko.ai.connectfour.demo;

import lombok.Value;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.DefaultGameRunner;
import ru.alexeylisyutenko.ai.connectfour.game.GameResult;
import ru.alexeylisyutenko.ai.connectfour.game.GameRunner;
import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.main.console.gamelistener.ConsoleGameEventListener;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.*;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MultithreadedMinimaxSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.player.MinimaxBasedPlayer;

import java.util.concurrent.atomic.AtomicInteger;

public class MinimaxGameDemo {

    @Test
    @Disabled
    void competitionDemo() throws InterruptedException {
        int games = 200;
        int depth = 6;

        Player player1 = new MinimaxBasedPlayer(new MultithreadedMinimaxSearchFunction(), new CachingEvaluationFunction(new EvenBetterEvaluationFunction()), depth);
        Player player2 = new MinimaxBasedPlayer(new MultithreadedMinimaxSearchFunction(), new CachingEvaluationFunction(new EvenBetterEvaluationFunction()), depth);

        CompetitionResult competitionResult = runCompetition(player1, player2, games);
        System.out.println(competitionResult);
    }

    private CompetitionResult runCompetition(Player player1, Player player2, int games) throws InterruptedException {
        CompetitionResult firstResults = compete(player1, player2, games);
        CompetitionResult secondResults = compete(player2, player1, games);
        return new CompetitionResult(
                firstResults.getPlayer1Wins() + secondResults.getPlayer2Wins(),
                firstResults.getPlayer2Wins() + secondResults.getPlayer1Wins(),
                firstResults.getTies() + secondResults.getTies(),
                firstResults.getGames() + secondResults.getGames()
        );
    }

    private CompetitionResult compete(Player player1, Player player2, int games) throws InterruptedException {
        CompetitionGameEventListener competitionGameEventListener = new CompetitionGameEventListener();
        GameRunner gameRunner = new DefaultGameRunner(player1, player2, competitionGameEventListener);
        for (int i = 0; i < games; i++) {
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
