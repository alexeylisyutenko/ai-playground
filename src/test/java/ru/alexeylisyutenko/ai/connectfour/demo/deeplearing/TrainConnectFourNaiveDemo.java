package ru.alexeylisyutenko.ai.connectfour.demo.deeplearing;

import org.deeplearning4j.core.storage.StatsStorage;
import org.deeplearning4j.datasets.iterator.INDArrayDataSetIterator;
import org.deeplearning4j.earlystopping.EarlyStoppingConfiguration;
import org.deeplearning4j.earlystopping.EarlyStoppingResult;
import org.deeplearning4j.earlystopping.saver.LocalFileModelSaver;
import org.deeplearning4j.earlystopping.scorecalc.DataSetLossCalculator;
import org.deeplearning4j.earlystopping.termination.MaxEpochsTerminationCondition;
import org.deeplearning4j.earlystopping.termination.MaxTimeIterationTerminationCondition;
import org.deeplearning4j.earlystopping.trainer.EarlyStoppingTrainer;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.model.stats.StatsListener;
import org.deeplearning4j.ui.model.storage.InMemoryStatsStorage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nd4j.common.primitives.Pair;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDataset;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDatasets;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static ru.alexeylisyutenko.ai.connectfour.demo.deeplearing.ConnectFourDatasetHelpers.constructINDArraysFor;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class TrainConnectFourNaiveDemo {
    @Test
    @Disabled
    void demo() throws IOException {
        ConnectFourDataset connectFourDataset = ConnectFourDatasets.connectFourDataset();


        INDArray flattenedMeanBoard = ConnectFourDatasetHelpers.constructMeanBoardArrayFlattened(connectFourDataset.getTrainingSet());
        INDArrayDataSetIterator trainInterator =
                ConnectFourDatasetHelpers.createINDArrayDataSetIterator(connectFourDataset.getTrainingSet(), flattenedMeanBoard, 200);
        INDArrayDataSetIterator validateIterator =
                ConnectFourDatasetHelpers.createINDArrayDataSetIterator(connectFourDataset.getValidationSet(), flattenedMeanBoard, 200);
        INDArrayDataSetIterator testIterator =
                ConnectFourDatasetHelpers.createINDArrayDataSetIterator(connectFourDataset.getTestSet(), flattenedMeanBoard, 200);

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(new Adam())
                .l2(1e-4)
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(BOARD_HEIGHT * BOARD_WIDTH) // Number of input datapoints.
                        .nOut(3000) // Number of output datapoints.
                        .activation(Activation.RELU) // Activation function.
                        .weightInit(WeightInit.XAVIER) // Weight initialization.
                        .build())
                .layer(new DenseLayer.Builder()
                        .nIn(3000)
                        .nOut(2000)
                        .activation(Activation.RELU)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .layer(new DenseLayer.Builder()
                        .nIn(2000)
                        .nOut(1000)
                        .activation(Activation.RELU)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .layer(new DenseLayer.Builder()
                        .nIn(1000)
                        .nOut(300)
                        .activation(Activation.RELU)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(300)
                        .nOut(BOARD_WIDTH)
                        .activation(Activation.SOFTMAX)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .build();

        //Initialize the user interface backend
        UIServer uiServer = UIServer.getInstance();

        //Configure where the network information (gradients, score vs. time etc) is to be stored. Here: store in memory.
        StatsStorage statsStorage = new InMemoryStatsStorage();         //Alternative: new FileStatsStorage(File), for saving and loading later

        //Attach the StatsStorage instance to the UI: this allows the contents of the StatsStorage to be visualized
        uiServer.attach(statsStorage);

//        EarlyStoppingConfiguration<MultiLayerNetwork> esConf = new EarlyStoppingConfiguration.Builder<MultiLayerNetwork>()
//                .epochTerminationConditions(new MaxEpochsTerminationCondition(2))
//                .iterationTerminationConditions(new MaxTimeIterationTerminationCondition(20, TimeUnit.MINUTES))
//                .scoreCalculator(new DataSetLossCalculator(validateIterator, true))
//                .evaluateEveryNEpochs(1)
//                .modelSaver(new LocalFileModelSaver("deeplearing-models"))
//                .build();
//
//        EarlyStoppingTrainer trainer = new EarlyStoppingTrainer(esConf, conf, trainInterator);
//
//        //Conduct early stopping training:
//        EarlyStoppingResult<MultiLayerNetwork> result = trainer.fit();
//
//        //Print out the results:
//        System.out.println("Termination reason: " + result.getTerminationReason());
//        System.out.println("Termination details: " + result.getTerminationDetails());
//        System.out.println("Total epochs: " + result.getTotalEpochs());
//        System.out.println("Best epoch number: " + result.getBestModelEpoch());
//        System.out.println("Score at best epoch: " + result.getBestModelScore());
//
//        //Get the best model:
//        MultiLayerNetwork network = result.getBestModel();

        // create the MLN
        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        network.init();

        // pass a training listener that reports score every 10 iterations
        int eachIterations = 10;
        network.addListeners(new ScoreIterationListener(eachIterations));
        network.addListeners(new StatsListener(statsStorage));

        // train
        int numEpochs = 200;
        for (int i = 0; i < numEpochs; i++) {
            System.out.println("Epoch " + i + " / " + numEpochs);
            network.fit(trainInterator);

            network.save(new File(String.format("deeplearing-models/naive-model-%d", i)), false);
        }

        // TODO: evaluate
        Evaluation eval = network.evaluate(testIterator);
        System.out.println(eval);
    }
}
