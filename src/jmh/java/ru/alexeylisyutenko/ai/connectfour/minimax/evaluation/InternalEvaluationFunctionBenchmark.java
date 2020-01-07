package ru.alexeylisyutenko.ai.connectfour.minimax.evaluation;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import ru.alexeylisyutenko.ai.connectfour.game.BitBoard;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BestEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.InternalEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.helper.BoardArrays;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@State(Scope.Thread)
public class InternalEvaluationFunctionBenchmark {

    List<BitBoard> boards;
    EvaluationFunction bestEvaluationFunction;
    EvaluationFunction internalEvaluationFunction;

    @Setup
    public void setup() {
        boards = BoardArrays.getBoardArrays().stream()
                .map(boardArrayMovePair -> new BitBoard(boardArrayMovePair.getLeft(), boardArrayMovePair.getRight()))
                .collect(Collectors.toList());
        bestEvaluationFunction = new BestEvaluationFunction();
        internalEvaluationFunction = new InternalEvaluationFunction();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void baseline(Blackhole bh) {
        for (BitBoard board : boards) {
            bh.consume(bestEvaluationFunction.evaluate(board));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void internalEvaluationFunction(Blackhole bh) {
        for (BitBoard board : boards) {
            bh.consume(internalEvaluationFunction.evaluate(board));
        }
    }
}
