package ru.alexeylisyutenko.ai.connectfour.dataset;

import ru.alexeylisyutenko.ai.connectfour.dataset.evaluator.BoardSetEvaluator;
import ru.alexeylisyutenko.ai.connectfour.dataset.evaluator.DefaultBoardSetEvaluator;
import ru.alexeylisyutenko.ai.connectfour.dataset.generator.ConnectFourDatasetGenerator;
import ru.alexeylisyutenko.ai.connectfour.dataset.generator.DefaultConnectFourDatasetGenerator;
import ru.alexeylisyutenko.ai.connectfour.dataset.serializer.BoardWithMoveSerializer;
import ru.alexeylisyutenko.ai.connectfour.dataset.serializer.DefaultBoardWithMoveSerializer;

public class ConnectFourDatasetGeneratorApplication {
    public static void main(String[] args) {
        BoardSetEvaluator boardSetEvaluator = new DefaultBoardSetEvaluator(5);
        BoardWithMoveSerializer boardWithMoveSerializer = new DefaultBoardWithMoveSerializer();
        ConnectFourDatasetGenerator generator = new DefaultConnectFourDatasetGenerator(boardSetEvaluator, boardWithMoveSerializer);
        generator.generate(10000, 20000, "training_set.bin", "test_set.bin");
    }
}
