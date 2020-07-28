package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.distance;

import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature.FeatureVector;

public interface DistanceFunction {
    Double distance(FeatureVector fv1, FeatureVector fv2);
}
