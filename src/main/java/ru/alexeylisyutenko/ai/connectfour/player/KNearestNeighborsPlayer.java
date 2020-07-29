package ru.alexeylisyutenko.ai.connectfour.player;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.GameContext;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.ConnectFourDatasets;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.NearestNeighbor;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.distance.ManhattanDistanceFunction;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.featureconverter.DefaultBoardToFeatureVectorConverter;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;

import java.util.List;

public class KNearestNeighborsPlayer extends AbstractPlayer {
    private final NearestNeighbor nearestNeighbor =
            new NearestNeighbor(ConnectFourDatasets.connectFourDataset(),
                    new ManhattanDistanceFunction(), new DefaultBoardToFeatureVectorConverter());

    @Override
    public void requestMove(GameContext gameContext) {
        int move = nearestNeighbor.predict(gameContext.getBoard(), 1);

        List<Pair<Integer, Board>> possibleMoves = MinimaxHelper.getAllNextMoves(gameContext.getBoard());
        boolean moveIsValid = possibleMoves.stream().map(Pair::getLeft).anyMatch(m -> m == move);
        if (moveIsValid) {
            gameContext.makeMove(move);
        } else {
            gameContext.makeMove(possibleMoves.get(0).getLeft());
        }
    }
}
