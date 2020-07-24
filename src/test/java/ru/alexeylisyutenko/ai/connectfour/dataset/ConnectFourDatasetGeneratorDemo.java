package ru.alexeylisyutenko.ai.connectfour.dataset;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.dataset.generator.ConnectFourDatasetGenerator;
import ru.alexeylisyutenko.ai.connectfour.dataset.generator.DefaultConnectFourDatasetGenerator;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.util.BoardGenerators;

import java.util.Iterator;

public class ConnectFourDatasetGeneratorDemo {

    private static final ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();

    @Test
    void demo() {
        ConnectFourDatasetGenerator generator = new DefaultConnectFourDatasetGenerator();
        generator.generate(10000, 20000, "training_set.bin", "test_set.bin");
    }

    @Test
    @Disabled
    public void generateDataset() {
        Iterator<Board> boardIterator = BoardGenerators.distinctBoards().iterator();
        consoleBoardVisualizer.visualize(boardIterator.next());
    }

}
