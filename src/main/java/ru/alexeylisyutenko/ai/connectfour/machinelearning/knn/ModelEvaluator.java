package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn;

import com.jakewharton.fliptables.FlipTable;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDataset;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDatasets;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.distance.*;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.featureconverter.BoardToFeatureVectorConverter;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.featureconverter.ChainBoardToFeatureVectorConverter;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.featureconverter.PlainBoardToFeatureVectorConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModelEvaluator {
    private static final ConnectFourDataset CONNECT_FOUR_DATASET = ConnectFourDatasets.connectFourDataset();

    private final List<Pair<String, DistanceFunction>> distanceFunctions = List.of(
            Pair.of("manhattan distance", new ManhattanDistanceFunction()),
            Pair.of("euclidean distance", new EuclideanDistanceFunction()),
            Pair.of("max distance", new MaxDistanceFunction()),
            Pair.of("hamming distance", new HammingDistanceFunction())
    );

    private final List<Pair<String, BoardToFeatureVectorConverter>> converters = List.of(
            Pair.of("plain converter", new PlainBoardToFeatureVectorConverter())
    );

    private final List<Integer> kValues = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20);


    public static void main(String[] args) {
        ModelEvaluator modelEvaluator = new ModelEvaluator();
        modelEvaluator.findBetterHyperparameters();
    }

    /**
     * Try to find better hyperparameters of a knn model.
     */
    public void findBetterHyperparameters() {
        List<HyperparametersSearchResult> results = new ArrayList<>();

        // Search for better hyperparameters.
        for (Pair<String, BoardToFeatureVectorConverter> converter : converters) {
            for (Pair<String, DistanceFunction> distanceFunction : distanceFunctions) {
                for (Integer k : kValues) {
                    NearestNeighbor nearestNeighbor = new DefaultNearestNeighbor(CONNECT_FOUR_DATASET.getTrainingSet(), distanceFunction.getValue(), converter.getValue());
                    System.out.printf("Evaluation started: %s + %s + %s...%n", converter.getKey(), distanceFunction.getKey(), k);

                    ModelEvaluationResult modelEvaluationResult = doEvaluateModel(nearestNeighbor, k, CONNECT_FOUR_DATASET.getTestSet());
                    printModelEvaluationResult(modelEvaluationResult);

                    results.add(HyperparametersSearchResult.builder()
                            .boardToFeatureVectorConverterName(converter.getKey())
                            .distanceFunctionName(distanceFunction.getKey())
                            .k(k)
                            .rate(modelEvaluationResult.getRate())
                            .build());
                }
            }
        }

        // Print the results of search.
        String[] header = {"Converter", "Distance function", "K", "Rate"};
        String[][] data = results.stream().map(hyperparametersSearchResult -> {
            return new String[]{
                    hyperparametersSearchResult.getBoardToFeatureVectorConverterName(),
                    hyperparametersSearchResult.getDistanceFunctionName(),
                    hyperparametersSearchResult.getK() + "",
                    hyperparametersSearchResult.rate + ""
            };
        }).toArray(String[][]::new);
        System.out.println(FlipTable.of(header, data));
    }

    /**
     * Evaluate a knn model with particular hyperparameters.
     */
    public void evaluateModel(DistanceFunction distanceFunction, BoardToFeatureVectorConverter converter, int k, Set<BoardWithMove> testDataset) {
        NearestNeighbor nearestNeighbor = new DefaultNearestNeighbor(CONNECT_FOUR_DATASET.getTrainingSet(), distanceFunction, converter);
        ModelEvaluationResult modelEvaluationResult = doEvaluateModel(nearestNeighbor, k, testDataset);

        printModelEvaluationResult(modelEvaluationResult);
    }

    private void printModelEvaluationResult(ModelEvaluationResult modelEvaluationResult) {
        System.out.printf("Rate = %f (%d/%d)%n", modelEvaluationResult.getRate(), modelEvaluationResult.getCount(), modelEvaluationResult.getDatasetSize());
        System.out.println();
    }

    private ModelEvaluationResult doEvaluateModel(NearestNeighbor nearestNeighbor, int k, Set<BoardWithMove> testDataset) {
        long count = testDataset.parallelStream()
                .filter(boardWithMove -> {
                    int predicted = nearestNeighbor.predict(boardWithMove.getBoard(), k);
                    return predicted == boardWithMove.getMove();
                })
                .count();
        double rate = ((double) count / testDataset.size()) * 100;
        return new ModelEvaluationResult(count, rate, testDataset.size());
    }

    @Value
    public static class ModelEvaluationResult {
        long count;
        double rate;
        long datasetSize;
    }

    @Value
    @Builder
    public static class HyperparametersSearchResult {
        String distanceFunctionName;
        String boardToFeatureVectorConverterName;
        int k;
        double rate;
    }
}
