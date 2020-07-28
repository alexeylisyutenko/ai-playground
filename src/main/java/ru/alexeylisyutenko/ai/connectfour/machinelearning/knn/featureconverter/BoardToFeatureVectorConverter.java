package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.featureconverter;

import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature.FeatureVector;

public interface BoardToFeatureVectorConverter {
    FeatureVector convert(Board board);
}
