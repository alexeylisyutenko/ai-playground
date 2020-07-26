package ru.alexeylisyutenko.ai.connectfour.dataset.generator;

/**
 *
 */
public interface ConnectFourDatasetGenerator {
    /**
     *
     * @param samplesInEachClassCount
     * @param testSamplesCount
     * @param trainingDataFileName
     * @param testDataFileName
     */
    void generate(int samplesInEachClassCount, int testSamplesCount, String trainingDataFileName, String testDataFileName);
}
