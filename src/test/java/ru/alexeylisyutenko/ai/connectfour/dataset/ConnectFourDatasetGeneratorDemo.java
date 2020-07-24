package ru.alexeylisyutenko.ai.connectfour.dataset;

import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.InternalEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.IterativeDeepeningSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.util.BoardGenerators;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class ConnectFourDatasetGeneratorDemo {

    private static final ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();

    @Test
    void demo() {
        ConnectFourDatasetGenerator generator = new DefaultConnectFourDatasetGenerator();
        generator.generate(10000, 20000, "training_set.bin", "test_set.bin");
    }

    @Test
    @Disabled
    public void generateDataset() {
        // Generate random boards.
        System.out.println("Generating boards...");
        HashSet<Board> boards = new HashSet<>();

        Iterator<Board> boardIterator = BoardGenerators.distinctBoards().iterator();

        AlphaBetaSearchFunction searchFunction = new AlphaBetaSearchFunction();
        InternalEvaluationFunction evaluationFunction = new InternalEvaluationFunction();

        int samplesInEachClass = 10000;
        HashMap<Integer, List<Board>> samplesByMoveColumns = new HashMap<>();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            samplesByMoveColumns.put(i, new ArrayList<>());
        }

        while (boards.size() < samplesInEachClass * 7) {
            // Generate a board.
            Board board = boardIterator.next();
            Move move = searchFunction.search(board, 6, evaluationFunction);

            // Check a bucket.
            if (samplesByMoveColumns.get(move.getColumn()).size() < samplesInEachClass) {
                samplesByMoveColumns.get(move.getColumn()).add(board);
                boards.add(board);
            }
        }

        // Evaluate each board.
        System.out.println("Evaluating boards...");
//        List<BoardWithMove> evaluatedBoards = evaluateBoards(boards);
        List<BoardWithMove> evaluatedBoards = List.of();

        Map<Integer, Long> columnStats = evaluatedBoards.stream().map(BoardWithMove::getMove).map(Move::getColumn)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println();
        System.out.println("Column histogram:");
        System.out.println(columnStats);
    }

    private List<BoardWithMove> evaluateBoards(Set<Board> boards) {
        CachingEvaluationFunction evaluationFunction = new CachingEvaluationFunction(new InternalEvaluationFunction());
        AtomicInteger counter = new AtomicInteger();
        return boards.stream().parallel().map(board -> {
            ThreadLocal<SearchFunction> threadLocal =
                    ThreadLocal.withInitial(() -> new IterativeDeepeningSearchFunction(100, false));
            Move move = threadLocal.get().search(board, 1, evaluationFunction);

            String message = String.format("%s : move = %s, tokens = %d, counter = %d",
                    Thread.currentThread().getName(), move.toString(), board.getNumberOfTokensOnBoard(), counter.incrementAndGet());
            System.out.println(message);

            return new BoardWithMove(board, move);
        }).collect(Collectors.toList());
    }

    @Value
    private static class BoardWithMove {
        Board board;
        Move move;
    }

}
