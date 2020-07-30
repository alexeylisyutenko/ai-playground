package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn;

import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDataset;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.distance.DistanceFunction;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature.FeatureVector;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.featureconverter.BoardToFeatureVectorConverter;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.util.SortUtils;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultNearestNeighbor implements NearestNeighbor {
    private final DistanceFunction distanceFunction;
    private final BoardToFeatureVectorConverter featureVectorConverter;
    private final Set<FutureVectorWithMove> vectorsWithMoves;

    public DefaultNearestNeighbor(Set<BoardWithMove> trainingSet, DistanceFunction distanceFunction, BoardToFeatureVectorConverter featureVectorConverter) {
        this.distanceFunction = distanceFunction;
        this.featureVectorConverter = featureVectorConverter;
        this.vectorsWithMoves = convertDatasetToVectorsWithMoves(trainingSet, featureVectorConverter);
    }

    @Override
    public int predict(Board board, int k) {
        Objects.requireNonNull(board, "board cannot be null");
        if (k < 1) {
            throw new IllegalArgumentException("k must be positive");
        }

        FeatureVector featureVector = featureVectorConverter.convert(board);

        // Filter impossible moves.
        Set<Integer> possibleMoves = MinimaxHelper.getAllNextMoves(board)
                .stream().map(Pair::getLeft).collect(Collectors.toSet());

        // Calculate distances.
        ArrayList<DistanceWithMove> distancesWithMoves = vectorsWithMoves.parallelStream()
                .filter(futureVectorWithMove -> possibleMoves.contains(futureVectorWithMove.getMove()))
                .map(futureVectorWithMove -> {
                    double distance = distanceFunction.distance(featureVector, futureVectorWithMove.getFeatureVector());
                    return new DistanceWithMove(distance, futureVectorWithMove.getMove());
                })
                .collect(Collectors.toCollection(ArrayList::new));

        // Find k smallest elements.
        List<DistanceWithMove> kNearestNeighbors = SortUtils.kSmallest(distancesWithMoves, Comparator.comparing(DistanceWithMove::getDistance), k);

        // Take average of k nearest neighbors.
        double average = kNearestNeighbors.stream().mapToInt(DistanceWithMove::getMove).average().orElseThrow();
        return (int) Math.round(average);
    }

    private Set<FutureVectorWithMove> convertDatasetToVectorsWithMoves(Set<BoardWithMove> dataset, BoardToFeatureVectorConverter featureVectorConverter) {
        return dataset.stream()
                .map(boardWithMove -> {
                    FeatureVector featureVector = featureVectorConverter.convert(boardWithMove.getBoard());
                    return new FutureVectorWithMove(featureVector, boardWithMove.getMove());
                })
                .collect(Collectors.toSet());
    }

    @Value
    private static class FutureVectorWithMove {
        FeatureVector featureVector;
        int move;
    }

    @Value
    private static class DistanceWithMove {
        double distance;
        int move;
    }
}
