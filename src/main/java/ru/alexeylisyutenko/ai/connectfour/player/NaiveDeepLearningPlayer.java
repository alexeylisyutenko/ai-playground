package ru.alexeylisyutenko.ai.connectfour.player;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import ru.alexeylisyutenko.ai.connectfour.exception.ConnectFourException;
import ru.alexeylisyutenko.ai.connectfour.game.GameContext;

import java.io.File;
import java.io.IOException;

public class NaiveDeepLearningPlayer extends AbstractPlayer {

    private final MultiLayerNetwork network;

    public NaiveDeepLearningPlayer() {
        try {
            this.network = MultiLayerNetwork.load(new File("deeplearing-models/naive-model-199"), false);
        } catch (IOException e) {
            throw new ConnectFourException(e.getMessage(), e);
        }
    }

    @Override
    public void requestMove(GameContext gameContext) {

    }
}
