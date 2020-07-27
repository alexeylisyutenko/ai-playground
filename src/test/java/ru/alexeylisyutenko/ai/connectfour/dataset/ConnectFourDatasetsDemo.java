package ru.alexeylisyutenko.ai.connectfour.dataset;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;

import static org.junit.jupiter.api.Assertions.*;

class ConnectFourDatasetsDemo {
    @Test
    void demo() {
        ConsoleBoardVisualizer visualizer = new ConsoleBoardVisualizer();
        ConnectFourDataset connectFourDataset = ConnectFourDatasets.connectFourDataset();
        System.out.println("Training dataset size: " + connectFourDataset.getTrainingSet().size());
        connectFourDataset.getTrainingSet().stream().limit(10).forEach(boardWithMove -> {
            System.out.println("Move: " + boardWithMove.getMove());
            visualizer.visualize(boardWithMove.getBoard());
            System.out.println();
        });
    }
}