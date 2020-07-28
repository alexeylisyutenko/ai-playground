package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset;

import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.evaluator.BoardSetEvaluator;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.evaluator.DefaultBoardSetEvaluator;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.generator.ConnectFourDatasetGenerator;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.generator.DefaultConnectFourDatasetGenerator;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.serializer.BoardWithMoveSerializer;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.serializer.DefaultBoardWithMoveSerializer;

public class ConnectFourDatasetGeneratorApplication {
    public static void main(String[] args) {
        BoardSetEvaluator boardSetEvaluator = new DefaultBoardSetEvaluator(2000);
        BoardWithMoveSerializer boardWithMoveSerializer = new DefaultBoardWithMoveSerializer();
        ConnectFourDatasetGenerator generator = new DefaultConnectFourDatasetGenerator(boardSetEvaluator, boardWithMoveSerializer);
        generator.generate(10000, 20000, "training_set.bin", "test_set.bin");
    }
}
