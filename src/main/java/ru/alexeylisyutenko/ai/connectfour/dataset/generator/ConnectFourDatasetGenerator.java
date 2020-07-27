package ru.alexeylisyutenko.ai.connectfour.dataset.generator;

/**
 * Connect Four game board dataset generator.
 * <br>
 * Implementation of this interface generates datasets which contain boards along with optimal moves.
 * Generated dataset can be used to train various machine learning models.
 */
public interface ConnectFourDatasetGenerator {
    /**
     * Generate Connect Four game board dataset.
     *
     * @param samplesInEachClassCount number of training samples for each class
     * @param testSamplesCount        number of test samples
     * @param trainingDataFileName    training set file name
     * @param testDataFileName        test set file name
     */
    void generate(int samplesInEachClassCount, int testSamplesCount, String trainingDataFileName, String testDataFileName);
}
