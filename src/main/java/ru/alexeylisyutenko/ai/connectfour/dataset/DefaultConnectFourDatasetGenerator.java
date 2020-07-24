package ru.alexeylisyutenko.ai.connectfour.dataset;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.InternalEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.util.BoardGenerators;

import java.util.*;
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

        System.out.println();
        printBoardsInfo(boards.getLeft());
        System.out.println();
        printBoardsInfo(boards.getRight());
        System.out.println();

        // Evaluate boards.

        // Save boards.

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
        // TODO: Test boards must be evenly distributed by sizes and classes.
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
}
