package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.Cell;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

class ConnectFourDatasetsDemo {
    @Test
    void demo() {
        ConsoleBoardVisualizer visualizer = new ConsoleBoardVisualizer();
        ConnectFourDataset connectFourDataset = ConnectFourDatasets.connectFourDataset();
        System.out.println("Training dataset size: " + connectFourDataset.getTrainingSet().size());
        connectFourDataset.getTrainingSet().stream().limit(10).forEach(boardWithMove -> {
            Board board = boardWithMove.getBoard();

            System.out.println("Current player: " + board.getCurrentPlayerId() + ", move: " + boardWithMove.getMove());
            visualizer.visualize(board);

            System.out.println();
        });
    }
}