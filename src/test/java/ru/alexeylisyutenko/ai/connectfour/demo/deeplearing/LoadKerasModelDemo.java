package ru.alexeylisyutenko.ai.connectfour.demo.deeplearing;

import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDatasets;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.deeplearning.ConnectFourDatasetForDeepLearningHelpers;

import java.io.IOException;
import java.util.Arrays;

public class LoadKerasModelDemo {
    @Test
    void demo() throws IOException, UnsupportedKerasConfigurationException, InvalidKerasConfigurationException {
        // Load a model.
//        MultiLayerNetwork network = KerasModelImport.importKerasSequentialModelAndWeights(
//                "deeplearing-models/model_config.json", "deeplearing-models/model_weights.h5");

        // Load a board.
        BoardWithMove boardWithMove = ConnectFourDatasets.connectFourDataset().getTestSet().iterator().next();
        Board board = boardWithMove.getBoard();

        // Prepare array.

        INDArray indArray = ConnectFourDatasetForDeepLearningHelpers.boardToINDArray(board);
        System.out.println(Arrays.toString(indArray.shape()));


    }
}
