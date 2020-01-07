package ru.alexeylisyutenko.ai.connectfour.minimax.search;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.StoppableSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.InternalEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.YBWCAlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal.*;

import java.util.concurrent.TimeUnit;

import static ru.alexeylisyutenko.ai.connectfour.minimax.helper.Game.runGame;

@State(Scope.Thread)
public class SearchFunctionsGameRunnerBasedBenchmark {
    @Param({"7"})
    int depth;

    @Param({"100"})
    int games;

    @State(Scope.Benchmark)
    public static class TranspositionTableAlphaBetaState {
        final EvaluationFunction evaluationFunction = new CachingEvaluationFunction(new InternalEvaluationFunction());
        final SearchFunction searchFunction = new TranspositionTableAlphaBetaSearchFunction();
    }

    @State(Scope.Benchmark)
    public static class TranspositionTableYBWCAlphaBetaState {
        final EvaluationFunction evaluationFunction = new CachingEvaluationFunction(new InternalEvaluationFunction());
        final StoppableSearchFunction searchFunction = new TranspositionTableYBWCAlphaBetaSearchFunction();
        @TearDown
        public void tearDown() {
            searchFunction.stop();
        }
    }

    @State(Scope.Benchmark)
    public static class OnlyTranspositionTableYBWCAlphaBetaState {
        final EvaluationFunction evaluationFunction = new CachingEvaluationFunction(new InternalEvaluationFunction());
        final StoppableSearchFunction searchFunction = new OnlyTranspositionTableYBWCAlphaBetaSearchFunction();
        @TearDown
        public void tearDown() {
            searchFunction.stop();
        }
    }

    @State(Scope.Benchmark)
    public static class OnlyEvalFunctionReorderingYBWCAlphaBetaState {
        final EvaluationFunction evaluationFunction = new CachingEvaluationFunction(new InternalEvaluationFunction());
        final SearchFunction searchFunction = new OnlyEvalFunctionReorderingYBWCAlphaBetaSearchFunction();
    }

    @State(Scope.Benchmark)
    public static class NoEvalFunctionReorderingTranspositionTableYBWCAlphaBetaState {
        final EvaluationFunction evaluationFunction = new CachingEvaluationFunction(new InternalEvaluationFunction());
        final StoppableSearchFunction searchFunction = new NoEvalFunctionReorderingTranspositionTableYBWCAlphaBetaSearchFunction();
        @TearDown
        public void tearDown() {
            searchFunction.stop();
        }
    }

    @State(Scope.Benchmark)
    public static class NoBestMoveTableTranspositionTableYBWCAlphaBetaState {
        final EvaluationFunction evaluationFunction = new CachingEvaluationFunction(new InternalEvaluationFunction());
        final StoppableSearchFunction searchFunction = new NoBestMoveTableTranspositionTableYBWCAlphaBetaSearchFunction();
        @TearDown
        public void tearDown() {
            searchFunction.stop();
        }
    }

    @State(Scope.Benchmark)
    public static class PlainYBWCAlphaBetaState {
        final EvaluationFunction evaluationFunction = new CachingEvaluationFunction(new InternalEvaluationFunction());
        final StoppableSearchFunction searchFunction = new PlainYBWCAlphaBetaSearchFunction();
        @TearDown
        public void tearDown() {
            searchFunction.stop();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void transpositionTableAlphaBetaSearchFunction(TranspositionTableAlphaBetaState state) throws InterruptedException {
        for (int i = 0; i < games; i++) {
            runGame(state.searchFunction, state.evaluationFunction, depth);;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void transpositionTableYBWCAlphaBetaSearchFunction(TranspositionTableYBWCAlphaBetaState state) throws InterruptedException {
        for (int i = 0; i < games; i++) {
            runGame(state.searchFunction, state.evaluationFunction, depth);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void onlyTranspositionTableYBWCAlphaBetaSearchFunction(OnlyTranspositionTableYBWCAlphaBetaState state) throws InterruptedException {
        for (int i = 0; i < games; i++) {
            runGame(state.searchFunction, state.evaluationFunction, depth);;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void onlyEvalFunctionReorderingYBWCAlphaBetaSearchFunction(OnlyEvalFunctionReorderingYBWCAlphaBetaState state) throws InterruptedException {
        for (int i = 0; i < games; i++) {
            runGame(state.searchFunction, state.evaluationFunction, depth);;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void noEvalFunctionReorderingTranspositionTableYBWCAlphaBetaSearchFunction(NoEvalFunctionReorderingTranspositionTableYBWCAlphaBetaState state) throws InterruptedException {
        for (int i = 0; i < games; i++) {
            runGame(state.searchFunction, state.evaluationFunction, depth);;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void noBestMoveTableTranspositionTableYBWCAlphaBetaSearchFunction(NoBestMoveTableTranspositionTableYBWCAlphaBetaState state) throws InterruptedException {
        for (int i = 0; i < games; i++) {
            runGame(state.searchFunction, state.evaluationFunction, depth);;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void plainYBWCAlphaBetaSearchFunction(PlainYBWCAlphaBetaState state) throws InterruptedException {
        for (int i = 0; i < games; i++) {
            runGame(state.searchFunction, state.evaluationFunction, depth);
        }
    }

}
