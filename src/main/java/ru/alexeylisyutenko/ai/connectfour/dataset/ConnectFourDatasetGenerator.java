package ru.alexeylisyutenko.ai.connectfour.dataset;

public interface ConnectFourDatasetGenerator {
    void generate(int samplesInEachClassCount, int testSamplesCount, String trainingDataFileName, String testDataFileName);
}
