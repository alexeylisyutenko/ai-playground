package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn;

import org.apache.commons.lang3.RandomUtils;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDataset;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDatasets;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.distance.EuclideanDistanceFunction;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature.NaiveBoardToFeatureVectorConverter;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NaiveApproach {
    public static void main(String[] args) {
        ConnectFourDataset connectFourDataset = ConnectFourDatasets.connectFourDataset();
        NearestNeighbor nearestNeighbor = new NearestNeighbor(connectFourDataset, new EuclideanDistanceFunction(), new NaiveBoardToFeatureVectorConverter());

        List<BoardWithMove> testDataset = new ArrayList<>(connectFourDataset.getTestSet());
        long correctCount = testDataset.parallelStream()
                .filter(boardWithMove -> {
                    int predicted = nearestNeighbor.predict(boardWithMove.getBoard());
                    return predicted == boardWithMove.getMove();
                })
                .count();
        double rate = ((double) correctCount / testDataset.size()) * 100;
        System.out.println("Rate = " + rate + ", " + correctCount + "/" + testDataset.size());

//        BoardWithMove boardWithMove = testDataset.get(RandomUtils.nextInt(0, testDataset.size()));
//
//        System.out.println("Current player: " + boardWithMove.getBoard().getCurrentPlayerId() + ", move: " + boardWithMove.getMove());
//        ConsoleBoardVisualizer visualizer = new ConsoleBoardVisualizer();
//        visualizer.visualize(boardWithMove.getBoard());
//
//        int predicted = nearestNeighbor.predict(boardWithMove.getBoard());
//        System.out.println("Predicted: " + predicted);
    }
}
