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
        connectFourDataset.getTrainingSet().stream().limit(3).forEach(boardWithMove -> {
            Board board = boardWithMove.getBoard();

            System.out.println("Current player: " + board.getCurrentPlayerId() + ", move: " + boardWithMove.getMove());
            visualizer.visualize(board);
            Set<List<Cell>> chainCells = board.getChainCells(1);
            chainCells.forEach(System.out::println);

            System.out.println();
            Map<Cell, Long> cellMap = chainCells.stream().flatMap(Collection::stream).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            System.out.println(cellMap);

            System.out.println();
        });
    }
}