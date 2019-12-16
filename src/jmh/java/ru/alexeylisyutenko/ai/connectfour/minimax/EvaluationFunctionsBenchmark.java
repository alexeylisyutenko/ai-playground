package ru.alexeylisyutenko.ai.connectfour.minimax;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.alexeylisyutenko.ai.connectfour.minimax.helper.Boards.getBoardsForBenchmarking;

@State(Scope.Thread)
public class EvaluationFunctionsBenchmark {
    @Param({"100"})
    int boardCount;

    List<Board> boards;
    EvaluationFunction basicEvaluationFunction;
    EvaluationFunction focusedEvaluationFunction;
    EvaluationFunction betterEvaluationFunction;
    EvaluationFunction evenBetterEvaluationFunction;
    EvaluationFunction bestEvaluationFunction;

    @Setup
    public void setup() {
        boards = getBoardsForBenchmarking(boardCount);
        basicEvaluationFunction = new BasicEvaluationFunction();
        focusedEvaluationFunction = new FocusedEvaluationFunction();
        betterEvaluationFunction = new BetterEvaluationFunction();
        evenBetterEvaluationFunction = new EvenBetterEvaluationFunction();
        bestEvaluationFunction = new BestEvaluationFunction();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void basic(Blackhole bh) {
        for (Board board : boards) {
            bh.consume(basicEvaluationFunction.evaluate(board));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void focused(Blackhole bh) {
        for (Board board : boards) {
            bh.consume(focusedEvaluationFunction.evaluate(board));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void better(Blackhole bh) {
        for (Board board : boards) {
            bh.consume(betterEvaluationFunction.evaluate(board));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void evenBetter(Blackhole bh) {
        for (Board board : boards) {
            bh.consume(evenBetterEvaluationFunction.evaluate(board));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void best(Blackhole bh) {
        for (Board board : boards) {
            bh.consume(bestEvaluationFunction.evaluate(board));
        }
    }

}
