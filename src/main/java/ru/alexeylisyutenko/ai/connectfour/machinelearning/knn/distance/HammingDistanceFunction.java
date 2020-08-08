package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.distance;

import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature.FeatureVector;

public class HammingDistanceFunction implements DistanceFunction {
    @Override
    public Double distance(FeatureVector fv1, FeatureVector fv2) {
        int sum = 0;
        for (int i = 0; i < fv1.getDimension(); i++) {
            if (Double.compare(fv1.get(i), fv2.get(i)) != 0) {
                sum++;
            }
        }
        return (double)sum;
    }
}
