package ru.alexeylisyutenko.ai.connectfour.demo.deeplearing;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDataset;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDatasets;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class Nd4jDemo {
    @Test
    @Disabled
    void demo() {
        // Construct INDArrays for Connect Four training data set.
        ConnectFourDataset connectFourDataset = ConnectFourDatasets.connectFourDataset();

        Pair<INDArray, INDArray> indArrays = constructINDArraysFor(connectFourDataset.getValidationSet());
        INDArray features = indArrays.getLeft();
        INDArray outputs = indArrays.getRight();

        System.out.println(Arrays.toString(features.shape()));
        System.out.println(features);
        System.out.println();

        System.out.println(Arrays.toString(outputs.shape()));
        System.out.println(outputs);
        System.out.println();

        INDArray reshaped = features.reshape(-1, BOARD_HEIGHT * BOARD_WIDTH);
        System.out.println(Arrays.toString(reshaped.shape()));
        System.out.println(reshaped);
        System.out.println();
    }

    private Pair<INDArray, INDArray> constructINDArraysFor(Set<BoardWithMove> dataset) {
        Objects.requireNonNull(dataset, "dataset cannot be null");

        INDArray features = Nd4j.create(dataset.size(), BOARD_HEIGHT, BOARD_WIDTH);
        INDArray outputs = Nd4j.create(dataset.size());
        int sampleIndex = 0;
        for (BoardWithMove boardWithMove : dataset) {
            features.slice(sampleIndex).assign(boardToINDArray(boardWithMove.getBoard()));
            outputs.putScalar(sampleIndex, boardWithMove.getMove());
            sampleIndex++;
        }
        return Pair.of(features, outputs);
    }

    private INDArray boardToINDArray(Board board) {
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
}
