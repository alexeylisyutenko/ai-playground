package ru.alexeylisyutenko.ai.connectfour.dataset.generator;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.dataset.evaluator.BoardSetEvaluator;
import ru.alexeylisyutenko.ai.connectfour.dataset.helper.BoardSetHelpers;
import ru.alexeylisyutenko.ai.connectfour.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.dataset.serializer.BoardWithMoveSerializer;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.InternalEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.util.BoardGenerators;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class DefaultConnectFourDatasetGenerator implements ConnectFourDatasetGenerator {
    private final BoardSetEvaluator boardSetEvaluator;
    private final BoardWithMoveSerializer boardWithMoveSerializer;

    public DefaultConnectFourDatasetGenerator(BoardSetEvaluator boardSetEvaluator, BoardWithMoveSerializer boardWithMoveSerializer) {
        this.boardSetEvaluator = boardSetEvaluator;
        this.boardWithMoveSerializer = boardWithMoveSerializer;
    }

    @Override
    public void generate(int samplesInEachClassCount, int testSamplesCount, String trainingDataFileName, String testDataFileName) {
        validateArguments(samplesInEachClassCount, testSamplesCount, trainingDataFileName, testDataFileName);

        // Generate boards.
        System.out.println("Generating boards...");
        Pair<Set<Board>, Set<Board>> boards = generateBoards(samplesInEachClassCount, testSamplesCount);
        Set<Board> trainingSetBoards = boards.getLeft();
        Set<Board> testSetBoards = boards.getRight();

        printBoardSetsInformation(trainingSetBoards, testSetBoards);

        // Evaluate boards.
        System.out.println("Evaluating boards...");
        List<BoardWithMove> evaluatedTrainingSetBoards = boardSetEvaluator.evaluate(trainingSetBoards, "Training set");
        List<BoardWithMove> evaluatedTestSetBoards = boardSetEvaluator.evaluate(testSetBoards, "Test set");
        printEvaluatedBoardSetsInformation(evaluatedTrainingSetBoards, evaluatedTestSetBoards);

        // Save boards.
        saveBoardWithMoves(evaluatedTrainingSetBoards, trainingDataFileName);
        saveBoardWithMoves(evaluatedTestSetBoards, testDataFileName);
    }

    private void saveBoardWithMoves(List<BoardWithMove> boardWithMoves, String fileName) {
        try(OutputStream fileOutputStream = new FileOutputStream(fileName)) {
            boardWithMoves.forEach(boardWithMove -> {
                try {
                    fileOutputStream.write(boardWithMoveSerializer.serialize(boardWithMove));
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void printEvaluatedBoardSetsInformation(List<BoardWithMove> evaluatedTrainingSetBoards, List<BoardWithMove> evaluatedTestSetBoards) {
        System.out.println();
        BoardSetHelpers.printMovesDistribution(evaluatedTrainingSetBoards);
        BoardSetHelpers.printMovesDistribution(evaluatedTestSetBoards);
    }

    private void printBoardSetsInformation(Set<Board> trainingSetBoards, Set<Board> testSetBoards) {
        System.out.println("Training set boards:");
        BoardSetHelpers.printSingleBoardSetInformation(trainingSetBoards);
        System.out.println();
        System.out.println("Test set boards:");
        BoardSetHelpers.printSingleBoardSetInformation(testSetBoards);
        System.out.println();
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

    private Pair<Set<Board>, Set<Board>> generateBoards(int trainingSetSizeForEachClass, int testSetSize) {
        AlphaBetaSearchFunction searchFunction = new AlphaBetaSearchFunction();
        InternalEvaluationFunction evaluationFunction = new InternalEvaluationFunction();

        // Generate training set.
        Set<Board> trainingSet = new HashSet<>();
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
            testSet.add(boardIterator.next());
        }

        return Pair.of(trainingSet, testSet);
    }

}
