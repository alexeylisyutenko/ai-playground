package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDataset;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.distance.DistanceFunction;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature.BoardToFeatureVectorConverter;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature.FeatureVector;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;

import java.util.Set;
import java.util.stream.Collectors;

public class NearestNeighbor {
    private final DistanceFunction distanceFunction;
    private final BoardToFeatureVectorConverter featureVectorConverter;
    private final Set<Pair<FeatureVector, Integer>> vectorsWithMoves;

    public NearestNeighbor(ConnectFourDataset connectFourDataset, DistanceFunction distanceFunction, BoardToFeatureVectorConverter featureVectorConverter) {
        this.distanceFunction = distanceFunction;
        this.featureVectorConverter = featureVectorConverter;
        this.vectorsWithMoves = convertDatasetToVectorsWithMoves(connectFourDataset.getTrainingSet(), featureVectorConverter);
    }

    public int predict(Board board) {
        FeatureVector featureVector = featureVectorConverter.convert(board);
        double minimumDistance = Double.MAX_VALUE;
        Pair<FeatureVector, Integer> best = null;
        for (Pair<FeatureVector, Integer> vectorWithMove : vectorsWithMoves) {
            Double currentDistance = distanceFunction.distance(featureVector, vectorWithMove.getLeft());
            if (currentDistance < minimumDistance) {
                minimumDistance = currentDistance;
                best = vectorWithMove;
            }
        }
        return best.getRight();
    }

    private Set<Pair<FeatureVector, Integer>> convertDatasetToVectorsWithMoves(Set<BoardWithMove> dataset, BoardToFeatureVectorConverter featureVectorConverter) {
        return dataset.stream()
                .map(boardWithMove -> {
                    FeatureVector featureVector = featureVectorConverter.convert(boardWithMove.getBoard());
                    return Pair.of(featureVector, boardWithMove.getMove());
                })
                .collect(Collectors.toSet());
    }

}
