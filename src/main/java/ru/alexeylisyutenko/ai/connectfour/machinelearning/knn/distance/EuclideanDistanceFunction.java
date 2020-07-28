package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.distance;

import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature.FeatureVector;

public class EuclideanDistanceFunction implements DistanceFunction {
    @Override
    public Double distance(FeatureVector fv1, FeatureVector fv2) {
        if (fv1.getDimension() != fv2.getDimension()) {
            throw new IllegalArgumentException("Dimensions of future vectors are different");
        }

        double sum = 0.0;
        for (int i = 0; i < fv1.getDimension(); i++) {
            sum += Math.pow(fv1.get(i) - fv2.get(i), 2.0);
        }
        return Math.sqrt(sum);
    }
}
