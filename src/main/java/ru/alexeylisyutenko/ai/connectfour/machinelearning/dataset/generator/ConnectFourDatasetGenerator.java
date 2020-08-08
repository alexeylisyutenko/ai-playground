package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.generator;

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
     * @param config generator configuration
     */
    void generate(ConnectFourDatasetGeneratorConfig config);
}
