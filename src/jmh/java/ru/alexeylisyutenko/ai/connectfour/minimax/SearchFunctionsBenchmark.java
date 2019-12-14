package ru.alexeylisyutenko.ai.connectfour.minimax;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BetterEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.FocusedEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.YBWCAlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MinimaxSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MultithreadedMinimaxSearchFunction;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.alexeylisyutenko.ai.connectfour.minimax.helper.Boards.getBoardsForBenchmarking;

@State(Scope.Thread)
public class SearchFunctionsBenchmark {

    @Param({"50"})
    int boardCount;

    @Param({"6"})
    int depth;

    List<Board> boards;
    EvaluationFunction evaluationFunction;
    SearchFunction minimaxSearchFunction;
    SearchFunction alphaBetaSearchFunction;
    SearchFunction multithreadedMinimaxSearchFunction;
    SearchFunction ybwcAlphaBetaSearchFunction;

    @Setup
    public void setup() {
        boards = getBoardsForBenchmarking(boardCount);
        evaluationFunction = new BetterEvaluationFunction();
        minimaxSearchFunction = new MinimaxSearchFunction();
        alphaBetaSearchFunction = new AlphaBetaSearchFunction();
        multithreadedMinimaxSearchFunction = new MultithreadedMinimaxSearchFunction();
        ybwcAlphaBetaSearchFunction = new YBWCAlphaBetaSearchFunction();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void minimaxSearchFunction(Blackhole bh) {
        for (Board board : boards) {
            bh.consume(minimaxSearchFunction.search(board, depth, evaluationFunction));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void alphaBetaSearchFunction(Blackhole bh) {
        for (Board board : boards) {
            bh.consume(alphaBetaSearchFunction.search(board, depth, evaluationFunction));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void multithreadedMinimaxSearchFunction(Blackhole bh) {
        for (Board board : boards) {
            bh.consume(multithreadedMinimaxSearchFunction.search(board, depth, evaluationFunction));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void ybwcAlphaBetaSearchFunction(Blackhole bh) {
        for (Board board : boards) {
            bh.consume(ybwcAlphaBetaSearchFunction.search(board, depth, evaluationFunction));
        }
    }

}
