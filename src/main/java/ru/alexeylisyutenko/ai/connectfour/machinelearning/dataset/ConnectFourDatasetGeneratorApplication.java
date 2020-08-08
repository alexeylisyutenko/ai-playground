package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset;

import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.evaluator.BoardSetEvaluator;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.evaluator.DefaultBoardSetEvaluator;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.generator.ConnectFourDatasetGenerator;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.generator.ConnectFourDatasetGeneratorConfig;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.generator.DefaultConnectFourDatasetGenerator;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.serializer.BoardWithMoveSerializer;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.serializer.DefaultBoardWithMoveSerializer;

public class ConnectFourDatasetGeneratorApplication {
    public static void main(String[] args) {
        BoardSetEvaluator boardSetEvaluator = new DefaultBoardSetEvaluator(1000);
        BoardWithMoveSerializer boardWithMoveSerializer = new DefaultBoardWithMoveSerializer();
        ConnectFourDatasetGenerator generator = new DefaultConnectFourDatasetGenerator(boardSetEvaluator, boardWithMoveSerializer);

        ConnectFourDatasetGeneratorConfig generatorConfig = ConnectFourDatasetGeneratorConfig.builder()
                .trainingSamplesInEachClassCount(20000)
                .validationSamplesCount(40000)
                .testSamplesCount(40000)
                .trainingDataFileName("training_set.bin")
                .validationDataFileName("validation_set.bin")
                .testDataFileName("test_set.bin")
                .build();
        generator.generate(generatorConfig);
    }
}
