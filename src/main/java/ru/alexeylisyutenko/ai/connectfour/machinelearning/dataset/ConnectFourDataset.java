package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset;

import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;

import java.util.Set;

/**
 * Connect Four Game dataset.
 */
public interface ConnectFourDataset {
    /**
     * Get a training set.
     *
     * @return training dataset
     */
    Set<BoardWithMove> getTrainingSet();

    /**
     * Get a test set.
     *
     * @return test dataset
     */
    Set<BoardWithMove> getTestSet();
}