package ru.alexeylisyutenko.ai.connectfour.minimax;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BestEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.EvenBetterEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.YBWCAlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal.ExperimentalYBWCAlphaBetaSearchFunction;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.alexeylisyutenko.ai.connectfour.minimax.helper.Boards.getGenuineBoards;

@State(Scope.Thread)
public class SearchFunctionsWithGenuineBoardsBenchmark {
    @Param({"6"})
    int depth;

    List<Board> boards;
    EvaluationFunction evaluationFunction;
    SearchFunction ybwcAlphaBetaSearchFunction;
    SearchFunction experimentalYbwcAlphaBetaSearchFunction;


    @Setup
    public void setup() {
        boards = getGenuineBoards();
        evaluationFunction = new CachingEvaluationFunction(new BestEvaluationFunction());
        ybwcAlphaBetaSearchFunction = new YBWCAlphaBetaSearchFunction();
        experimentalYbwcAlphaBetaSearchFunction = new ExperimentalYBWCAlphaBetaSearchFunction();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 5)
    @Measurement(iterations = 3, time = 5)
    public void ybwcAlphaBetaSearchFunctionGame(Blackhole bh) {
        for (Board board : boards) {
            bh.consume(ybwcAlphaBetaSearchFunction.search(board, depth, evaluationFunction));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 5)
    @Measurement(iterations = 3, time = 5)
    public void experimentalYbwcAlphaBetaSearchFunctionGame(Blackhole bh) {
        for (Board board : boards) {
            bh.consume(experimentalYbwcAlphaBetaSearchFunction.search(board, depth, evaluationFunction));
        }
    }

}
