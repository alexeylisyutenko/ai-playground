package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset;

import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.defaultdataset.ConnectFourDatasetLoader;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.defaultdataset.DefaultConnectFourDataset;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;

import java.util.Set;

public class ConnectFourDatasets {
    public static final String DATASETS_TRAINING_SET_FILE = "datasets/training_set.bin";
    public static final String DATASETS_TEST_SET_FILE = "datasets/test_set.bin";

    private ConnectFourDatasets() {
    }

    public static ConnectFourDataset connectFourDataset() {
        ConnectFourDatasetLoader loader = new ConnectFourDatasetLoader();
        Set<BoardWithMove> trainingDataset = loader.load(DATASETS_TRAINING_SET_FILE);
        Set<BoardWithMove> testDataset = loader.load(DATASETS_TEST_SET_FILE);
        return new DefaultConnectFourDataset(trainingDataset, testDataset);
    }
}
