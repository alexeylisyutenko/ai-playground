package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.game.GameContext;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDatasets;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.DefaultNearestNeighbor;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.NearestNeighbor;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.distance.HammingDistanceFunction;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.featureconverter.PlainBoardToFeatureVectorConverter;

public class KNearestNeighborsPlayer extends AbstractPlayer {
    private final NearestNeighbor nearestNeighbor =
            new DefaultNearestNeighbor(ConnectFourDatasets.connectFourDataset().getTrainingSet(),
                    new HammingDistanceFunction(), new PlainBoardToFeatureVectorConverter());

    @Override
    public void requestMove(GameContext gameContext) {
        int move = nearestNeighbor.predict(gameContext.getBoard(), 1);
        makeMoveIfPossibleOrAny(gameContext, move);
    }
}
