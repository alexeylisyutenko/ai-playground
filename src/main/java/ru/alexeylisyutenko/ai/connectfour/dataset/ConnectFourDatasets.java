package ru.alexeylisyutenko.ai.connectfour.dataset;

import ru.alexeylisyutenko.ai.connectfour.dataset.defaultdataset.ConnectFourDatasetLoader;
import ru.alexeylisyutenko.ai.connectfour.dataset.defaultdataset.DefaultConnectFourDataset;
import ru.alexeylisyutenko.ai.connectfour.dataset.model.BoardWithMove;

import java.util.Set;

public class ConnectFourDatasets {
    private ConnectFourDatasets() {
    }

    public static ConnectFourDataset connectFourDataset() {
        ConnectFourDatasetLoader loader = new ConnectFourDatasetLoader();
        Set<BoardWithMove> trainingDataset = loader.load("training_set.bin");
        Set<BoardWithMove> testDataset = loader.load("test_set.bin");
        return new DefaultConnectFourDataset(trainingDataset, testDataset);
    }
}
