package ru.alexeylisyutenko.ai.connectfour.machinelearning.deeplearning;

import org.deeplearning4j.datasets.iterator.INDArrayDataSetIterator;
import org.nd4j.common.primitives.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDataset;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDatasets;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public final class ConnectFourDatasetForDeepLearningHelpers {
    private ConnectFourDatasetForDeepLearningHelpers() {
    }

    public static INDArray constructMeanBoardArrayFlattened(Set<BoardWithMove> dataset) {
        Pair<INDArray, INDArray> indArrayINDArrays = constructINDArraysFor(dataset);
        INDArray features = indArrayINDArrays.getLeft();
        INDArray meanBoard = features.mean(0);
        return meanBoard.reshape(-1);
    }

    public static INDArrayDataSetIterator createINDArrayDataSetIterator(Set<BoardWithMove> dataset, INDArray flattenedMeanBoard, int batchSize) {
        Objects.requireNonNull(dataset, "dataset cannot be null");

        ArrayList<Pair<INDArray, INDArray>> pairs = new ArrayList<>(dataset.size());
        for (BoardWithMove boardWithMove : dataset) {
            INDArray boardArray = boardToINDArray(boardWithMove.getBoard()).reshape(-1).subi(flattenedMeanBoard);
            INDArray outputsArray = Nd4j.zeros(BOARD_WIDTH);
            outputsArray.putScalar(boardWithMove.getMove(), 1.0);

            pairs.add(Pair.of(boardArray, outputsArray));
        }
        return new INDArrayDataSetIterator(pairs, batchSize);
    }

    public static Pair<INDArray, INDArray> constructINDArraysFor(Set<BoardWithMove> dataset) {
        Objects.requireNonNull(dataset, "dataset cannot be null");

        INDArray features = Nd4j.create(dataset.size(), BOARD_HEIGHT, BOARD_WIDTH);
        INDArray outputs = Nd4j.zeros(dataset.size(), 7);
        int sampleIndex = 0;
        for (BoardWithMove boardWithMove : dataset) {
            features.slice(sampleIndex).assign(boardToINDArray(boardWithMove.getBoard()));
            outputs.slice(sampleIndex).putScalar(boardWithMove.getMove(), 1.0);
            sampleIndex++;
        }
        return Pair.of(features, outputs);
    }

    public static INDArray boardToINDArray(Board board) {
        Objects.requireNonNull(board, "board cannot be null");

        INDArray indArray = Nd4j.create(BOARD_HEIGHT, BOARD_WIDTH);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                if (board.getCellPlayerId(row, column) == board.getCurrentPlayerId()) {
                    indArray.put(row, column, 1.0);
                } else if (board.getCellPlayerId(row, column) == board.getOtherPlayerId()) {
                    indArray.put(row, column, -1.0);
                } else {
                    indArray.put(row, column, 0.0);
                }
            }
        }
        return indArray;
    }

    public static INDArray boardToINDArrayFlattened(Board board) {
        return boardToINDArray(board).reshape(-1);
    }
}
