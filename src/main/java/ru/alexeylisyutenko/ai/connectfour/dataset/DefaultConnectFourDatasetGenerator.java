package ru.alexeylisyutenko.ai.connectfour.dataset;

import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.InternalEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.IterativeDeepeningSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.util.BoardGenerators;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class DefaultConnectFourDatasetGenerator implements ConnectFourDatasetGenerator {
    private final AlphaBetaSearchFunction searchFunction = new AlphaBetaSearchFunction();
    private final InternalEvaluationFunction evaluationFunction = new InternalEvaluationFunction();

    @Override
    public void generate(int samplesInEachClassCount, int testSamplesCount, String trainingDataFileName, String testDataFileName) {
        validateArguments(samplesInEachClassCount, testSamplesCount, trainingDataFileName, testDataFileName);

        // Generate boards.
        System.out.println("Generating boards...");
        Pair<Set<Board>, Set<Board>> boards = generateBoards(samplesInEachClassCount, testSamplesCount);
        Set<Board> trainingSetBoards = boards.getLeft();
        Set<Board> testSetBoards = boards.getRight();

        //!!!
        System.out.println();
        printBoardsInfo(trainingSetBoards);
        System.out.println();
        printBoardsInfo(testSetBoards);
        System.out.println();
        //!!!

        // Evaluate boards.
        List<BoardWithMove > evaluatedTrainingSetBoards = evaluateBoards(trainingSetBoards, "Training set");
        List<BoardWithMove> evaluatedTestSetBoards = evaluateBoards(testSetBoards, "Test set");

        //!!!
        System.out.println();
        printMovesDistribution(evaluatedTrainingSetBoards);
        printMovesDistribution(evaluatedTestSetBoards);
        //!!!

        // Save boards.

    }

    private void printMovesDistribution(List<BoardWithMove> evaluatedBoards) {
        Map<Integer, Long> columnStats = evaluatedBoards.stream().map(BoardWithMove::getMove).map(Move::getColumn)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println();
        System.out.println("Column histogram:");
        System.out.println(columnStats);
    }

    private List<BoardWithMove> evaluateBoards(Set<Board> boards, String setName) {
        EvaluationFunction evaluationFunction = new InternalEvaluationFunction();
        AtomicInteger counter = new AtomicInteger();
        return boards.stream().parallel().map(board -> {
            ThreadLocal<SearchFunction> threadLocal =
                    ThreadLocal.withInitial(() -> new IterativeDeepeningSearchFunction(5, false));
            Move move = threadLocal.get().search(board, 1, evaluationFunction);
            if ((counter.get() % 100) == 0) {
                System.out.println(String.format("%s evaluation:\t%d/%d", setName, counter.get(), boards.size()));
            }
            counter.incrementAndGet();
            return new BoardWithMove(board, move);
        }).collect(Collectors.toList());
    }

    private void validateArguments(int samplesInEachClassCount, int testSamplesCount, String trainingDataFileName, String testDataFileName) {
        if (samplesInEachClassCount <= 0) {
            throw new IllegalArgumentException("samplesInEachClassCount must be greater or equal to zero");
        }
        if (testSamplesCount <= 0) {
            throw new IllegalArgumentException("testSamplesCount must be greater or equal to zero");
        }
        if (trainingDataFileName.isBlank()) {
            throw new IllegalArgumentException("trainingDataFileName cannot be blank");
        }
        if (testDataFileName.isBlank()) {
            throw new IllegalArgumentException("testDataFileName cannot be blank");
        }
    }

    private boolean canBeFinishedInOneMove(Board board) {
        List<Pair<Integer, Board>> allNextMoves = MinimaxHelper.getAllNextMoves(board);
        for (Pair<Integer, Board> nextMove : allNextMoves) {
            Board nextMoveBoard = nextMove.getRight();
            if (nextMoveBoard.isGameOver()) {
                return true;
            }
        }
        return false;
    }

    private void printBoardsInfo(Set<Board> boards) {
        System.out.println("Boards statistics:");
        IntSummaryStatistics statistics = boards.stream().mapToInt(Board::getNumberOfTokensOnBoard).summaryStatistics();
        System.out.println(statistics);

        Map<Integer, Long> tokensStatistics = boards.stream()
                .collect(Collectors.groupingBy(
                        board -> board.getNumberOfTokensOnBoard() / 5,
                        Collectors.counting()));
        System.out.println("Number of tokens distribution = " + tokensStatistics);

        Map<Boolean, Long> canBeFinishedInOneStep = boards.stream()
                .collect(Collectors.groupingBy(this::canBeFinishedInOneMove, Collectors.counting()));
        System.out.println("Can be finished in one step distribution = " + canBeFinishedInOneStep);
    }

    private Pair<Set<Board>, Set<Board>> generateBoards(int trainingSetSizeForEachClass, int testSetSize) {
        Set<Board> trainingSet = new HashSet<>();

        // Generate training set.
        int[] sizes = new int[BOARD_WIDTH];
        Iterator<Board> boardIterator = BoardGenerators.distinctBoards().iterator();
        while (trainingSet.size() < trainingSetSizeForEachClass * BOARD_WIDTH) {
            Board board = boardIterator.next();
            Move move = searchFunction.search(board, 6, evaluationFunction);

            if (sizes[move.getColumn()] < trainingSetSizeForEachClass) {
                sizes[move.getColumn()]++;
                trainingSet.add(board);
            }
        }

        // Generate test set.
        Set<Board> testSet = new HashSet<>();
        while (testSet.size() < testSetSize) {
            Board board = boardIterator.next();
            if (trainingSet.contains(board)) {
                throw new IllegalStateException("board is in the training set");
            }
            testSet.add(board);
        }

        return Pair.of(trainingSet, testSet);
    }

    private static class Evaluator {
        private final AtomicInteger progressCounter;
        private final Set<Board> boards;
        private final String boardSetName;

        public Evaluator(Set<Board> boards, String boardSetName) {
            this.progressCounter = new AtomicInteger();
            this.boards = boards;
            this.boardSetName = boardSetName;
        }

        // Print the results and estimate time.
        public static List<BoardWithMove> evaluateBoards(Set<Board> boards, String boardSetName) {
            return new Evaluator(boards, boardSetName).doEvaluate();
        }

        private List<BoardWithMove> doEvaluate() {
            return List.of();
        }

//        private List<BoardWithMove> doEvaluateBoards(Set<Board> boards, String setName) {
//            EvaluationFunction evaluationFunction = new InternalEvaluationFunction();
//            return boards.stream().parallel().map(board -> {
//                ThreadLocal<SearchFunction> threadLocal = ThreadLocal.withInitial(() -> new IterativeDeepeningSearchFunction(5, false));
//                Move move = threadLocal.get().search(board, 1, evaluationFunction);
//                return new BoardWithMove(board, move);
//            }).collect(Collectors.toList());
//        }

    }

    @Value
    private static class BoardWithMove {
        Board board;
        Move move;
    }
}
