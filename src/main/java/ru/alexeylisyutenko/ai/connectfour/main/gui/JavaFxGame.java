package ru.alexeylisyutenko.ai.connectfour.main.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.alexeylisyutenko.ai.connectfour.game.*;
import ru.alexeylisyutenko.ai.connectfour.main.gui.boardcontrol.BoardControl;
import ru.alexeylisyutenko.ai.connectfour.main.gui.gamelistener.GuiGameEventListener;
import ru.alexeylisyutenko.ai.connectfour.main.gui.player.GuiPlayer;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BetterEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.FocusedEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.YBWCAlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.player.*;

public class JavaFxGame extends Application {
    private GameRunner gameRunner;

    private BoardControl boardControl;
    private Button startGameButton;
    private Button stopGameButton;
    private Label gameStateLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Connect Four Game");
        primaryStage.setScene(new Scene(createContent(), GameConstants.SCENE_WIDTH, GameConstants.SCENE_HEIGHT));

        gameRunner = createGameRunner();

        startGameButton.setOnMouseClicked(event -> gameRunner.startGame());
        stopGameButton.setOnMouseClicked(event -> gameRunner.stopGame());
        primaryStage.show();
    }

    private GameRunner createGameRunner() {
        Player player1 = new GuiPlayer(boardControl);
//        Player player1 = new MultithreadedPlainMinimaxPlayer(9);
//        Player player1 = new MinimaxBasedPlayer(new YBWCAlphaBetaSearchFunction(), new CachingEvaluationFunction(new FocusedEvaluationFunction()), 9);
        Player player2 = new MinimaxBasedPlayer(new YBWCAlphaBetaSearchFunction(), new CachingEvaluationFunction(new BetterEvaluationFunction()), 10);
//        Player player1 = new FocusedAlphaBetaPlayer(8);
//        Player player2 = new YBWCFocusedAlphaBetaPlayer(9);
//        Player player2 = new GuiPlayer(boardControl);
//        Player player2 = new PlainMinimaxPlayer();
        return new DefaultGameRunner(player1, player2, new GuiGameEventListener(boardControl, gameStateLabel));
    }

    @Override
    public void stop() throws Exception {
        gameRunner.shutdown();
    }

    private Parent createContent() {
        HBox root = new HBox();

        Parent controlArea = createControlArea();
        root.getChildren().add(controlArea);

        Parent gameArea = createGameArea();
        root.getChildren().add(gameArea);
        HBox.setHgrow(gameArea, Priority.ALWAYS);

        return root;
    }

    private Parent createControlArea() {
        startGameButton = new Button("Start Game");
        startGameButton.setPrefWidth(200);
        stopGameButton = new Button("Stop Game");
        stopGameButton.setPrefWidth(200);
        VBox vBox = new VBox(startGameButton, stopGameButton);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(10);
        vBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
        vBox.setPadding(new Insets(10));
        return vBox;
    }

    private Parent createGameArea() {
        StackPane boardStackPane = new StackPane();

        boardControl = new BoardControl();
        boardStackPane.getChildren().add(boardControl);
        StackPane.setAlignment(boardControl, Pos.CENTER);

        gameStateLabel = new Label("Press 'Start Game' to start.");
        gameStateLabel.setFont(new Font(24));
        gameStateLabel.setPadding(new Insets(10));
        boardStackPane.getChildren().add(gameStateLabel);
        StackPane.setAlignment(gameStateLabel, Pos.TOP_CENTER);

        return boardStackPane;
    }

}
