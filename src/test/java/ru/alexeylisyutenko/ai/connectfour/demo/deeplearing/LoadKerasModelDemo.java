package ru.alexeylisyutenko.ai.connectfour.demo.deeplearing;

import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDatasets;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class LoadKerasModelDemo {

    public INDArray boardToINDArray(Board board) {
        Objects.requireNonNull(board, "board cannot be null");
        INDArray indArray = Nd4j.create(1, BOARD_HEIGHT, BOARD_WIDTH, 1);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                if (board.getCellPlayerId(row, column) == board.getCurrentPlayerId()) {
                    indArray.putScalar(new int[]{0, row, column, 0}, 1.0);
                } else if (board.getCellPlayerId(row, column) == board.getOtherPlayerId()) {
                    indArray.putScalar(new int[]{0, row, column, 0}, 0.5);
                } else {
                    indArray.putScalar(new int[]{0, row, column, 0}, 0.0);
                }
            }
        }
        return indArray;
    }

    @Disabled
    @Test
    void demo() throws IOException, UnsupportedKerasConfigurationException, InvalidKerasConfigurationException {
        // Load a model.
        MultiLayerNetwork network = KerasModelImport.importKerasSequentialModelAndWeights(
                "deeplearing-models/model_config.json", "deeplearing-models/model_weights.h5");

        // Load a board.
        ArrayList<BoardWithMove> boardWithMoves = new ArrayList<>(ConnectFourDatasets.connectFourDataset().getTestSet());
        Collections.shuffle(boardWithMoves);
        BoardWithMove boardWithMove = boardWithMoves.get(0);
        Board board = boardWithMove.getBoard();

        // Prepare array.
        INDArray indArray = boardToINDArray(board);
        System.out.println(indArray);

        int move = network.predict(indArray)[0];
        System.out.println("Predicted: " + move + ", real: " + boardWithMove.getMove());
    }
}
