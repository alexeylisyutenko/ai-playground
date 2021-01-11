package ru.alexeylisyutenko.ai.connectfour.demo.deeplearing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nd4j.common.primitives.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDataset;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDatasets;

import java.util.Arrays;

import static ru.alexeylisyutenko.ai.connectfour.machinelearning.deeplearning.ConnectFourDatasetForDeepLearningHelpers.constructINDArraysFor;

public class Nd4jDemo {
    @Test
    @Disabled
    void demo() {
        // Construct INDArrays for Connect Four training data set.
        ConnectFourDataset connectFourDataset = ConnectFourDatasets.connectFourDataset();

        Pair<INDArray, INDArray> indArrays = constructINDArraysFor(connectFourDataset.getTrainingSet());
        INDArray features = indArrays.getLeft();
        INDArray outputs = indArrays.getRight();
        long numTraining = features.shape()[0];

        System.out.println("Outputs:");
        System.out.println(Arrays.toString(outputs.shape()));
        System.out.println(outputs);
        System.out.println();

        INDArray meanBoard = features.mean(0);
        System.out.println("Mean board:");
        System.out.println(Arrays.toString(meanBoard.shape()));
        System.out.println(meanBoard);
        System.out.println();

        System.out.println("Zero centered features: ");
        features.subi(meanBoard);
        System.out.println(Arrays.toString(features.shape()));
        System.out.println(features);
        System.out.println();

        System.out.println("Reshaped: ");
        INDArray reshaped = features.reshape(numTraining, -1);
        System.out.println(Arrays.toString(reshaped.shape()));
        System.out.println(reshaped);
        System.out.println();
    }
}
