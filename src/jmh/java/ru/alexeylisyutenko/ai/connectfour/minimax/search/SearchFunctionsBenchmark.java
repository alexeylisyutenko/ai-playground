package ru.alexeylisyutenko.ai.connectfour.minimax.search;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BestEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BetterEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.FocusedEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.YBWCAlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal.TranspositionTableAlphaBetaSearchFunction;
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
    SearchFunction transpositionTableAlphaBetaSearchFunction;

    @Setup
    public void setup() {
        boards = getBoardsForBenchmarking(boardCount);
        evaluationFunction = new CachingEvaluationFunction(new BestEvaluationFunction());
        minimaxSearchFunction = new MinimaxSearchFunction();
        alphaBetaSearchFunction = new AlphaBetaSearchFunction();
        multithreadedMinimaxSearchFunction = new MultithreadedMinimaxSearchFunction();
        ybwcAlphaBetaSearchFunction = new YBWCAlphaBetaSearchFunction();
        transpositionTableAlphaBetaSearchFunction = new TranspositionTableAlphaBetaSearchFunction();
    }

//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
//    @OutputTimeUnit(TimeUnit.MILLISECONDS)
//    public void minimaxSearchFunction(Blackhole bh) {
//        for (Board board : boards) {
//            bh.consume(minimaxSearchFunction.search(board, depth, evaluationFunction));
//        }
//    }

//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
//    @OutputTimeUnit(TimeUnit.MILLISECONDS)
//    @Warmup(iterations = 3, time = 5)
//    @Measurement(iterations = 3, time = 5)
//    public void alphaBetaSearchFunction(Blackhole bh) {
//        for (Board board : boards) {
//            bh.consume(alphaBetaSearchFunction.search(board, depth, evaluationFunction));
//        }
//    }

//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
//    @OutputTimeUnit(TimeUnit.MILLISECONDS)
//    public void multithreadedMinimaxSearchFunction(Blackhole bh) {
//        for (Board board : boards) {
//            bh.consume(multithreadedMinimaxSearchFunction.search(board, depth, evaluationFunction));
//        }
//    }
//
//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
//    @OutputTimeUnit(TimeUnit.MILLISECONDS)
//    public void ybwcAlphaBetaSearchFunction(Blackhole bh) {
//        for (Board board : boards) {
//            bh.consume(ybwcAlphaBetaSearchFunction.search(board, depth, evaluationFunction));
//        }
//    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations = 3, time = 5)
    @Measurement(iterations = 3, time = 5)
    public void transpositionTableAlphaBetaSearchFunction(Blackhole bh) {
        for (Board board : boards) {
            bh.consume(transpositionTableAlphaBetaSearchFunction.search(board, depth, evaluationFunction));
        }
    }


}
