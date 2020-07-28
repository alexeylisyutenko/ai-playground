package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature;

import ru.alexeylisyutenko.ai.connectfour.game.Board;

public interface BoardToFeatureVectorConverter {
    FeatureVector convert(Board board);
}
