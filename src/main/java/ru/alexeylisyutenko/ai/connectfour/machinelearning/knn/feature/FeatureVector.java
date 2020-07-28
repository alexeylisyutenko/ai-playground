package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature;

public interface FeatureVector {
    double get(int index);

    int getDimension();
}
