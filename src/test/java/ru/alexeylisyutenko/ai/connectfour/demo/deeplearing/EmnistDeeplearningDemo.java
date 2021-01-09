package ru.alexeylisyutenko.ai.connectfour.demo.deeplearing;

import org.deeplearning4j.datasets.iterator.impl.EmnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.evaluation.classification.ROCMultiClass;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.IOException;

public class EmnistDeeplearningDemo {
    @Test
    @Disabled
    void demo() throws IOException {
        int batchSize = 128; // how many examples to simultaneously train in the network
        EmnistDataSetIterator.Set emnistSet = EmnistDataSetIterator.Set.BALANCED;
        EmnistDataSetIterator emnistTrain = new EmnistDataSetIterator(emnistSet, batchSize, true);
        EmnistDataSetIterator emnistTest = new EmnistDataSetIterator(emnistSet, batchSize, false);

//        System.out.println("emnistTrain.getLabels() = " + emnistTrain.getLabels());
//        System.out.println("emnistTrain.inputColumns() = " + emnistTrain.inputColumns());
//        System.out.println("emnistTrain.totalOutcomes() = " + emnistTrain.totalOutcomes());
//
//        DataSet dataSet = emnistTrain.next(5);
//        System.out.println(dataSet);
//        emnistTrain.reset();

        int outputNum = EmnistDataSetIterator.numLabels(emnistSet); // total output classes
        int rngSeed = 123; // integer for reproducability of a random number generator
        int numRows = 28; // number of "pixel rows" in an mnist digit
        int numColumns = 28;

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
//                .seed(rngSeed)
                .updater(new Adam())
                .l2(1e-4)
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(numRows * numColumns) // Number of input datapoints.
                        .nOut(1000) // Number of output datapoints.
                        .activation(Activation.RELU) // Activation function.
                        .weightInit(WeightInit.XAVIER) // Weight initialization.
                        .build())
                .layer(new DenseLayer.Builder()
                        .nIn(1000)
                        .nOut(600)
                        .activation(Activation.RELU)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .layer(new DenseLayer.Builder()
                        .nIn(600)
                        .nOut(300)
                        .activation(Activation.RELU)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(300)
                        .nOut(outputNum)
                        .activation(Activation.SOFTMAX)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .build();

        // create the MLN
        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        network.init();

        // pass a training listener that reports score every 10 iterations
        int eachIterations = 10;
        network.addListeners(new ScoreIterationListener(eachIterations));

        // fit a dataset for a single epoch
//        network.fit(emnistTrain);

        // fit for multiple epochs
        int numEpochs = 20;
        network.fit(emnistTrain, numEpochs);

        // or simply use for loop
//        for (int i = 0; i < numEpochs; i++) {
//            System.out.println("Epoch " + i + " / " + numEpochs);
//            network.fit(emnistTrain);
//        }

        // evaluate basic performance
        Evaluation eval = network.evaluate(emnistTest);
//        System.out.println(eval.accuracy());
//        System.out.println(eval.precision());
//        System.out.println(eval.recall());

        // evaluate ROC and calculate the Area Under Curve
//        ROCMultiClass roc = network.evaluateROCMultiClass(emnistTest, 0);
//        roc.calculateAUC(0);

        // optionally, you can print all stats from the evaluations
        System.out.print(eval.stats());
//        System.out.println(roc.stats());
    }
}
