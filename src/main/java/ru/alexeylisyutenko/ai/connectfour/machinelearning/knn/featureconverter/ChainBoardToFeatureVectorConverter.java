package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.featureconverter;

import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.Cell;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature.ArrayListFeatureVector;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature.FeatureVector;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class ChainBoardToFeatureVectorConverter implements BoardToFeatureVectorConverter {
    @Override
    public FeatureVector convert(Board board) {
        Set<List<Cell>> currentPlayerChains = board.getChainCells(board.getCurrentPlayerId());
        Map<Cell, Long> currentPlayerCells = currentPlayerChains.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Set<List<Cell>> otherPlayerChains = board.getChainCells(board.getOtherPlayerId());
        Map<Cell, Long> otherPlayerCells = otherPlayerChains.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Double> vector = new ArrayList<>(BOARD_WIDTH * BOARD_HEIGHT);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                Cell cell = new Cell(row, column);
                double val;
                if (currentPlayerCells.containsKey(cell)) {
                    val = currentPlayerCells.get(cell);
                } else if (otherPlayerCells.containsKey(cell)) {
                    val = -otherPlayerCells.get(cell);
                } else {
                    val = 0.0;
                }
                vector.add(val);
            }
        }
        return new ArrayListFeatureVector(vector);
    }
}
