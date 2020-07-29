package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn;

import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDataset;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDatasets;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.distance.ManhattanDistanceFunction;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.distance.MaxDistanceFunction;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.featureconverter.DefaultBoardToFeatureVectorConverter;

import java.util.ArrayList;
import java.util.List;

public class NaiveApproach {
    public static void main(String[] args) {
        ConnectFourDataset connectFourDataset = ConnectFourDatasets.connectFourDataset();
        NearestNeighbor nearestNeighbor = new NearestNeighbor(connectFourDataset, new MaxDistanceFunction(), new DefaultBoardToFeatureVectorConverter());

        List<BoardWithMove> testDataset = new ArrayList<>(connectFourDataset.getTestSet());
        long correctCount = testDataset.parallelStream()
                .filter(boardWithMove -> {
                    int predicted = nearestNeighbor.predict(boardWithMove.getBoard(), 1);
                    return predicted == boardWithMove.getMove();
                })
                .count();
        double rate = ((double) correctCount / testDataset.size()) * 100;
        System.out.println("Rate = " + rate + ", " + correctCount + "/" + testDataset.size());
    }
}
