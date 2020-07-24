package ru.alexeylisyutenko.ai.connectfour.dataset.generator;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.dataset.evaluator.DefaultBoardSetEvaluator;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.InternalEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.util.BoardGenerators;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

// TODO: Refactor the code.
public class DefaultConnectFourDatasetGenerator implements ConnectFourDatasetGenerator {
    public static final int ITERATIVE_DEEPENING_TIMEOUT = 10;

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

        printBoardSetsInformation(trainingSetBoards, testSetBoards);

        // Evaluate boards.
        System.out.println("Evaluating boards...");
        List<BoardWithMove> evaluatedTrainingSetBoards = evaluateBoards(trainingSetBoards, "Training set");
        List<BoardWithMove> evaluatedTestSetBoards = evaluateBoards(testSetBoards, "Test set");

        printEvaluatedBoardSetsInformation(evaluatedTrainingSetBoards, evaluatedTestSetBoards);

        // Save boards.
        saveDataset(evaluatedTrainingSetBoards, evaluatedTestSetBoards, trainingDataFileName, testDataFileName);
    }

    private void saveDataset(List<BoardWithMove> evaluatedTrainingSetBoards, List<BoardWithMove> evaluatedTestSetBoards,
                             String trainingDataFileName, String testDataFileName) {
        try(OutputStream fileOutputStream = new FileOutputStream(trainingDataFileName)) {
            evaluatedTestSetBoards.forEach(boardWithMove -> {
                try {
                    fileOutputStream.write(serializeBoardWithMove(boardWithMove));
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] serializeBoardWithMove(BoardWithMove boardWithMove) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        // Serialize a move as a first byte in a sample.
        os.write(boardWithMove.getMove());

        // Serialize a board.
        Board board = boardWithMove.getBoard();
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                int cellByte;
                int cellPlayerId = board.getCellPlayerId(row, column);
                if (cellPlayerId == board.getCurrentPlayerId()) {
                    cellByte = 1;
                } else if (cellPlayerId == board.getOtherPlayerId()) {
                    cellByte = 2;
                } else {
                    cellByte = 0;
                }
                os.write(cellByte);
            }
        }
        return os.toByteArray();
    }

    private void printEvaluatedBoardSetsInformation(List<BoardWithMove> evaluatedTrainingSetBoards, List<BoardWithMove> evaluatedTestSetBoards) {
        System.out.println();
        printMovesDistribution(evaluatedTrainingSetBoards);
        printMovesDistribution(evaluatedTestSetBoards);
    }

    private void printBoardSetsInformation(Set<Board> trainingSetBoards, Set<Board> testSetBoards) {
        System.out.println("Training set boards:");
        printSingleBoardSetInformation(trainingSetBoards);
        System.out.println();
        System.out.println("Test set boards:");
        printSingleBoardSetInformation(testSetBoards);
        System.out.println();
    }

    private void printMovesDistribution(List<BoardWithMove> evaluatedBoards) {
        Map<Integer, Long> columnStats = evaluatedBoards.stream().map(BoardWithMove::getMove)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println("Move distribution = " + columnStats);
    }

    private List<BoardWithMove> evaluateBoards(Set<Board> boards, String boardSetName) {
        DefaultBoardSetEvaluator boardSetEvaluator = new DefaultBoardSetEvaluator(ITERATIVE_DEEPENING_TIMEOUT);
        return boardSetEvaluator.evaluate(boards, boardSetName);
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

    private void printSingleBoardSetInformation(Set<Board> boards) {
        IntSummaryStatistics statistics = boards.stream().mapToInt(Board::getNumberOfTokensOnBoard).summaryStatistics();
        System.out.println("Boards statistics = " + statistics);

        Map<Integer, Long> tokensStatistics = boards.stream()
                .collect(Collectors.groupingBy(
                        board -> board.getNumberOfTokensOnBoard() / 5,
                        Collectors.counting()));
        System.out.println("Number of tokens distribution = " + tokensStatistics);

        Map<Boolean, Long> canBeFinishedInOneStep = boards.stream().collect(Collectors.groupingBy(this::canBeFinishedInOneMove, Collectors.counting()));
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

}
