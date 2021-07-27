package ru.alexeylisyutenko.ai.connectfour.player;

import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import ru.alexeylisyutenko.ai.connectfour.exception.ConnectFourException;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.GameContext;

import java.io.IOException;
import java.util.Objects;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class KerasDeepLearningPlayer extends AbstractPlayer {
    public static final String KERAS_MODEL_CONFIG = "deeplearing-models/model_config.json";
    public static final String KERAS_MODEL_WEIGHTS = "deeplearing-models/model_weights.h5";

    private final MultiLayerNetwork network;
    private final boolean verbose;

    public KerasDeepLearningPlayer() {
        this(true);
    }

    public KerasDeepLearningPlayer(boolean verbose) {
        this.verbose = verbose;
        this.network = loadNetwork();
    }

    private MultiLayerNetwork loadNetwork() {
        try {
            return KerasModelImport.importKerasSequentialModelAndWeights(KERAS_MODEL_CONFIG, KERAS_MODEL_WEIGHTS);
        } catch (IOException | InvalidKerasConfigurationException | UnsupportedKerasConfigurationException e) {
            throw new ConnectFourException(e.getMessage(), e);
        }
    }

    @Override
    public void requestMove(GameContext gameContext) {
        INDArray boardIndArray = boardToINDArray(gameContext.getBoard());
        int move = network.predict(boardIndArray)[0];

        INDArray output = network.output(boardIndArray);
        if (verbose) {
            System.out.println("NN output: " + output);
        }

        makeMoveIfPossibleOrAny(gameContext, move);
    }

    private INDArray boardToINDArray(Board board) {
        Objects.requireNonNull(board, "board cannot be null");
        INDArray indArray = Nd4j.create(1, BOARD_HEIGHT, BOARD_WIDTH, 1);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                if (board.getCellPlayerId(row, column) == board.getCurrentPlayerId()) {
                    indArray.putScalar(new int[]{0, row, column, 0}, 1.0);
                } else if (board.getCellPlayerId(row, column) == board.getOtherPlayerId()) {
                    indArray.putScalar(new int[]{0, row, column, 0}, 0.5);
                } else {
                    indArray.putScalar(new int[]{0, row, column, 0}, 0.0);
                }
            }
        }
        return indArray;
    }
}
