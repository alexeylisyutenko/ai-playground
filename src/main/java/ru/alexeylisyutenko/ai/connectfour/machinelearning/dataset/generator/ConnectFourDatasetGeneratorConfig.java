package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.generator;

import lombok.Builder;
import lombok.Value;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDatasets;

/**
 * Connect Four game board dataset generator configuration.
 */
@Builder
@Value
public class ConnectFourDatasetGeneratorConfig {
    /**
     * Number of training samples for each class.
     */
    int trainingSamplesInEachClassCount;

    /**
     * Number of validation samples.
     */
    int validationSamplesCount;

    /**
     * Number of test samples.
     */
    int testSamplesCount;

    /**
     * Training set file name.
     */
    String trainingDataFileName;

    /**
     * Validation set file name.
     */
    String validationDataFileName;

    /**
     * Test set file name.
     */
    String testDataFileName;
}
