package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;

class ConnectFourDatasetsDemo {
    @Test
    void demo() {
        ConsoleBoardVisualizer visualizer = new ConsoleBoardVisualizer();
        ConnectFourDataset connectFourDataset = ConnectFourDatasets.connectFourDataset();
        System.out.println("Training dataset size: " + connectFourDataset.getTrainingSet().size());
        connectFourDataset.getTrainingSet().stream().limit(100).forEach(boardWithMove -> {
            System.out.println("Current player: " + boardWithMove.getBoard().getCurrentPlayerId() + ", move: " + boardWithMove.getMove());
            visualizer.visualize(boardWithMove.getBoard());
            System.out.println();
        });
    }
}