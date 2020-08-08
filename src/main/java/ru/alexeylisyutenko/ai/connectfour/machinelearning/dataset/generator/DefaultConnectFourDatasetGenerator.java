package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.generator;

import lombok.Builder;
import lombok.Value;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.evaluator.BoardSetEvaluator;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.helper.BoardSetHelpers;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.serializer.BoardWithMoveSerializer;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.InternalEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.util.BoardGenerators;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class DefaultConnectFourDatasetGenerator implements ConnectFourDatasetGenerator {
    private final BoardSetEvaluator boardSetEvaluator;
    private final BoardWithMoveSerializer boardWithMoveSerializer;

    public DefaultConnectFourDatasetGenerator(BoardSetEvaluator boardSetEvaluator, BoardWithMoveSerializer boardWithMoveSerializer) {
        this.boardSetEvaluator = boardSetEvaluator;
        this.boardWithMoveSerializer = boardWithMoveSerializer;
    }

    @Override
    public void generate(ConnectFourDatasetGeneratorConfig config) {
        validateConfig(config);

        // Generate boards.
        System.out.println("Generating boards...");
        GeneratedBoards boards = generateBoards(config);
        printBoardSetsInformation(boards);

        // Evaluate boards.
        System.out.println("Evaluating boards...");
        EvaluatedBoards evaluatedBoards = evaluateBoards(boards);
        printEvaluatedBoardSetsInformation(evaluatedBoards);

        // Save boards.
        saveBoardWithMoves(evaluatedBoards.getEvaluatedTrainingSet(), config.getTrainingDataFileName());
        saveBoardWithMoves(evaluatedBoards.getEvaluatedValidationSet(), config.getValidationDataFileName());
        saveBoardWithMoves(evaluatedBoards.getEvaluatedTestSet(), config.getTestDataFileName());
    }

    private void saveBoardWithMoves(List<BoardWithMove> boardWithMoves, String fileName) {
        try (OutputStream fileOutputStream = new FileOutputStream(fileName)) {
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

    private void printEvaluatedBoardSetsInformation(EvaluatedBoards evaluatedBoards) {
        System.out.println();
        System.out.println();
        System.out.println("Training set boards moves distribution:");
        BoardSetHelpers.printMovesDistribution(evaluatedBoards.getEvaluatedTrainingSet());
        System.out.println();
        System.out.println("Validation set boards moves distribution:");
        BoardSetHelpers.printMovesDistribution(evaluatedBoards.getEvaluatedValidationSet());
        System.out.println();
        System.out.println("Test set boards moves distribution:");
        BoardSetHelpers.printMovesDistribution(evaluatedBoards.getEvaluatedTestSet());
        System.out.println();
    }

    private void printBoardSetsInformation(GeneratedBoards boards) {
        System.out.println("Training set boards:");
        BoardSetHelpers.printSingleBoardSetInformation(boards.getTrainingSet());
        System.out.println();
        System.out.println("Validation set boards:");
        BoardSetHelpers.printSingleBoardSetInformation(boards.getValidationSet());
        System.out.println();
        System.out.println("Test set boards:");
        BoardSetHelpers.printSingleBoardSetInformation(boards.getTestSet());
        System.out.println();
    }

    private void validateConfig(ConnectFourDatasetGeneratorConfig config) {
        if (config.getTrainingSamplesInEachClassCount() <= 0) {
            throw new IllegalArgumentException("samplesInEachClassCount must be greater or equal to zero");
        }
        if (config.getValidationSamplesCount() <= 0) {
            throw new IllegalArgumentException("validationSamplesCount must be greater or equal to zero");
        }
        if (config.getTestSamplesCount() <= 0) {
            throw new IllegalArgumentException("testSamplesCount must be greater or equal to zero");
        }
        if (config.getTrainingDataFileName().isBlank()) {
            throw new IllegalArgumentException("trainingDataFileName cannot be blank");
        }
        if (config.getValidationDataFileName().isBlank()) {
            throw new IllegalArgumentException("validationDataFileName cannot be blank");
        }
        if (config.getTestDataFileName().isBlank()) {
            throw new IllegalArgumentException("testDataFileName cannot be blank");
        }
    }

    private GeneratedBoards generateBoards(ConnectFourDatasetGeneratorConfig config) {
        AlphaBetaSearchFunction searchFunction = new AlphaBetaSearchFunction();
        InternalEvaluationFunction evaluationFunction = new InternalEvaluationFunction();

        // Generate training set.
        Set<Board> trainingSet = new HashSet<>();
        int[] sizes = new int[BOARD_WIDTH];
        Iterator<Board> boardIterator = BoardGenerators.distinctBoards().iterator();
        while (trainingSet.size() < config.getTrainingSamplesInEachClassCount() * BOARD_WIDTH) {
            Board board = boardIterator.next();
            Move move = searchFunction.search(board, 6, evaluationFunction);

            if (sizes[move.getColumn()] < config.getTrainingSamplesInEachClassCount()) {
                sizes[move.getColumn()]++;
                trainingSet.add(board);
            }
        }

        // Generate validation set.
        Set<Board> validationSet = new HashSet<>();
        while (validationSet.size() < config.getValidationSamplesCount()) {
            validationSet.add(boardIterator.next());
        }

        // Generate test set.
        Set<Board> testSet = new HashSet<>();
        while (testSet.size() < config.getTestSamplesCount()) {
            testSet.add(boardIterator.next());
        }

        return GeneratedBoards.builder()
                .trainingSet(trainingSet)
                .validationSet(validationSet)
                .testSet(testSet)
                .build();
    }

    private EvaluatedBoards evaluateBoards(GeneratedBoards boards) {
        List<BoardWithMove> evaluatedTrainingSetBoards = boardSetEvaluator.evaluate(boards.getTrainingSet(), "Training set");
        List<BoardWithMove> evaluatedValidationSetBoards = boardSetEvaluator.evaluate(boards.getValidationSet(), "Validation set");
        List<BoardWithMove> evaluatedTestSetBoards = boardSetEvaluator.evaluate(boards.getTestSet(), "Test set");

        return EvaluatedBoards.builder()
                .evaluatedTrainingSet(evaluatedTrainingSetBoards)
                .evaluatedValidationSet(evaluatedValidationSetBoards)
                .evaluatedTestSet(evaluatedTestSetBoards)
                .build();
    }

    @Builder
    @Value
    private static class GeneratedBoards {
        Set<Board> trainingSet;
        Set<Board> validationSet;
        Set<Board> testSet;
    }

    @Builder
    @Value
    private static class EvaluatedBoards {
        List<BoardWithMove> evaluatedTrainingSet;
        List<BoardWithMove> evaluatedValidationSet;
        List<BoardWithMove> evaluatedTestSet;
    }
}
