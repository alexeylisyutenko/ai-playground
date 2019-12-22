package ru.alexeylisyutenko.ai.connectfour.minimax;

import org.apache.commons.lang3.RandomUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.DefaultBoard;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BestEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal.TranspositionTableAlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal.TranspositionTableYBWCAlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MinimaxSearchFunction;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class SearchFunctionsGameRunnerBasedBenchmark {
    @Param({"11"})
    int depth;

    @Param({"1"})
    int games;

    EvaluationFunction evaluationFunction;
    SearchFunction transpositionTableAlphaBetaSearchFunction;
    SearchFunction transpositionTableYBWCAlphaBetaSearchFunction;
    private EvaluationFunction randomizedEvaluationFunction;
    private MinimaxSearchFunction minimaxSearchFunction;

    @Setup
    public void setup() {
        evaluationFunction = new CachingEvaluationFunction(new BestEvaluationFunction());
        randomizedEvaluationFunction = board -> {
            int score;
            if (board.isGameOver()) {
                score = -1000 + board.getNumberOfTokensOnBoard();
            } else {
                score = RandomUtils.nextInt(0, 10) - 5;
            }
            return score;
        };
        minimaxSearchFunction = new MinimaxSearchFunction();

        transpositionTableAlphaBetaSearchFunction = new TranspositionTableAlphaBetaSearchFunction();
        transpositionTableYBWCAlphaBetaSearchFunction = new TranspositionTableYBWCAlphaBetaSearchFunction();
    }


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 5)
    @Measurement(iterations = 3, time = 5)
    public void transpositionTableYBWCAlphaBetaSearchFunctionGame() throws InterruptedException {
        for (int i = 0; i < games; i++) {
            runGame(transpositionTableYBWCAlphaBetaSearchFunction);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 5)
    @Measurement(iterations = 3, time = 5)
    public void transpositionTableAlphaBetaSearchFunctionGame(Blackhole bh) throws InterruptedException {
        for (int i = 0; i < games; i++) {
            runGame(transpositionTableAlphaBetaSearchFunction);
        }
    }

    private void runGame(SearchFunction searchFunction) {
        Board board = new DefaultBoard();
        while (!board.isGameOver()) {
            Move move;
            if (board.getCurrentPlayerId() == 1) {
                move = minimaxSearchFunction.search(board, 3, randomizedEvaluationFunction);
            } else {
                move = searchFunction.search(board, depth, evaluationFunction);
            }
            board = board.makeMove(move.getColumn());
        }
    }

}
