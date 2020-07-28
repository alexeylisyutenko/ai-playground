package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.featureconverter;

import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature.ArrayListFeatureVector;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.feature.FeatureVector;

import java.util.ArrayList;
import java.util.List;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class DefaultBoardToFeatureVectorConverter implements BoardToFeatureVectorConverter {
    @Override
    public FeatureVector convert(Board board) {
        List<Double> vector = new ArrayList<>(BOARD_WIDTH * BOARD_HEIGHT);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_HEIGHT; column++) {
                if (board.getCellPlayerId(row, column) == board.getCurrentPlayerId()) {
                    vector.add(1.0);
                } else if (board.getCellPlayerId(row, column) == board.getOtherPlayerId()) {
                    vector.add(-1.0);
                } else {
                    vector.add(0.0);
                }
            }
        }
        return new ArrayListFeatureVector(vector);
    }
}
