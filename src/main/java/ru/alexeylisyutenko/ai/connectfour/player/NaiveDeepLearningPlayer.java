package ru.alexeylisyutenko.ai.connectfour.player;

import org.apache.commons.lang3.tuple.Pair;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import ru.alexeylisyutenko.ai.connectfour.exception.ConnectFourException;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.GameContext;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static ru.alexeylisyutenko.ai.connectfour.machinelearning.deeplearning.ConnectFourDatasetForDeepLearningHelpers.boardToINDArrayFlattened;

public class NaiveDeepLearningPlayer extends AbstractPlayer {
    private static final String DEEPLEARING_MODEL_FILENAME = "deeplearing-models/naive-model.bin";
    private final static double[] MEAN_BOARD_ARRAY = {
            -0.0094, -0.0128, -0.0125, -0.0177, -0.0141, -0.0117, -0.0138,
            -0.0156, -0.0102, -0.0107, -0.0158, -0.0127, -0.0115, -0.0120,
            -0.0061, -0.0086, -0.0168, -0.0178, -0.0176, -0.0098, -0.0073,
            -0.0076, -0.0122, -0.0158, -0.0075, -0.0170, -0.0154, -0.0067,
            -0.0119, -0.0201, -0.0115, -0.0048, -0.0107, -0.0217, -0.0119,
            -0.0090, -0.0168, -0.0133, 0.0024, -0.0107, -0.0132, -0.0103
    };
    private final static INDArray MEAN_BOARD_ARRAY_FLATTENED = Nd4j.create(MEAN_BOARD_ARRAY, MEAN_BOARD_ARRAY.length);

    private final MultiLayerNetwork network;

    public NaiveDeepLearningPlayer() {
        this.network = loadNetwork();
    }

    private MultiLayerNetwork loadNetwork() {
        try {
            return MultiLayerNetwork.load(new File(DEEPLEARING_MODEL_FILENAME), false);
        } catch (IOException e) {
            throw new ConnectFourException(e.getMessage(), e);
        }
    }

    @Override
    public void requestMove(GameContext gameContext) {
        INDArray boardIndArray = boardToINDArrayFlattened(gameContext.getBoard()).sub(MEAN_BOARD_ARRAY_FLATTENED).reshape(1, -1);
        int move = network.predict(boardIndArray)[0];

        List<Pair<Integer, Board>> possibleMoves = MinimaxHelper.getAllNextMoves(gameContext.getBoard());
        boolean moveIsValid = possibleMoves.stream().map(Pair::getLeft).anyMatch(m -> m == move);
        if (moveIsValid) {
            gameContext.makeMove(move);
        } else {
            gameContext.makeMove(possibleMoves.get(0).getLeft());
        }
    }
}
