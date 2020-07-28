package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class ArrayListFeatureVector implements FeatureVector {
    private final List<Double> vector;

    public ArrayListFeatureVector(List<Double> vector) {
        this.vector = new ArrayList<>(vector);
    }

    @Override
    public double get(int index) {
        return vector.get(index);
    }

    @Override
    public int getDimension() {
        return vector.size();
    }
}
