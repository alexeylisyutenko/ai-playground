package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.defaultdataset;

import lombok.AllArgsConstructor;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDataset;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;

import java.util.Set;

@AllArgsConstructor
public class DefaultConnectFourDataset implements ConnectFourDataset {
    private final Set<BoardWithMove> trainingDataset;
    private final Set<BoardWithMove> testDataset;

    @Override
    public Set<BoardWithMove> getTrainingSet() {
        return trainingDataset;
    }

    @Override
    public Set<BoardWithMove> getTestSet() {
        return testDataset;
    }
}
