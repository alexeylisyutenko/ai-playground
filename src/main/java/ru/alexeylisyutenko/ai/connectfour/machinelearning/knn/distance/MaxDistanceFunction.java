package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.distance;

import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature.FeatureVector;

public class MaxDistanceFunction implements DistanceFunction {
    @Override
    public Double distance(FeatureVector fv1, FeatureVector fv2) {
        if (fv1.getDimension() != fv2.getDimension()) {
            throw new IllegalArgumentException("Dimensions of future vectors are different");
        }

        double maxDelta = Double.MIN_VALUE;
        for (int i = 0; i < fv1.getDimension(); i++) {
            double delta = Math.abs(fv1.get(i) - fv2.get(i));
            if (delta > maxDelta) {
                maxDelta = delta;
            }
        }
        return maxDelta;
    }
}
