package ru.alexeylisyutenko.ai.connectfour.main.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.alexeylisyutenko.ai.connectfour.game.*;
import ru.alexeylisyutenko.ai.connectfour.main.gui.boardcontrol.BoardControl;
import ru.alexeylisyutenko.ai.connectfour.main.gui.gamelistener.GuiGameEventListener;
import ru.alexeylisyutenko.ai.connectfour.main.gui.player.GuiPlayer;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.*;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal.TranspositionTableAlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.IterativeDeepeningSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.player.*;

public class JavaFxGame extends Application {
    private GameRunner gameRunner;

    private BoardControl boardControl;
    private Button startGameButton;
    private Button stopGameButton;
    private Label gameStateLabel;
    private Label timeLabel;
    private Label movesLabel;

    private ComboBox<String> player1ComboBox;
    private TextField player1DepthTextField;
    private TextField player1TimeoutTextField;

    private ComboBox<String> player2ComboBox;
    private TextField player2DepthTextField;
    private TextField player2TimeoutTextField;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Connect Four Game");
        primaryStage.setScene(new Scene(createContent(), GameConstants.SCENE_WIDTH, GameConstants.SCENE_HEIGHT));

        startGameButton.setOnMouseClicked(event -> {
            gameRunner = createGameRunner();
            gameRunner.startGame();
        });
        stopGameButton.setOnMouseClicked(event -> gameRunner.stopGame());
        primaryStage.show();
    }

    private GameRunner createGameRunner() {
//        Player player1 = new GuiPlayer(boardControl);
//        Player player2 = new GuiPlayer(boardControl);
        Player player1 = new MinimaxBasedPlayer(new IterativeDeepeningSearchFunction(60000), new CachingEvaluationFunction(new InternalEvaluationFunction()), 17);
        Player player2 = new MinimaxBasedPlayer(new IterativeDeepeningSearchFunction(60000), new CachingEvaluationFunction(new InternalEvaluationFunction()), 13);
        return new DefaultGameRunner(player1, player2, new GuiGameEventListener(boardControl, gameStateLabel, timeLabel, movesLabel));
    }

    @Override
    public void stop() {
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
        // Start game button.
        startGameButton = new Button("Start Game");
        startGameButton.setPrefWidth(200);
        VBox.setMargin(startGameButton, new Insets(10));

        // Stop game button.
        stopGameButton = new Button("Stop Game");
        stopGameButton.setPrefWidth(200);
        VBox.setMargin(stopGameButton, new Insets(0, 10, 10, 10));

        // Player 1 settings.
        Parent player1SettingsControls = createPlayer1SettingsControls();
        VBox.setMargin(player1SettingsControls, new Insets(50, 0, 0, 0));

        // Player 2 settings.
        Parent player2SettingsControls =createPlayer2SettingsControls();
        VBox.setMargin(player2SettingsControls, new Insets(50, 0, 0, 0));

        VBox vBox = new VBox(startGameButton, stopGameButton, player1SettingsControls, player2SettingsControls);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
        return vBox;
    }

    // TODO: Refactor!
    private Parent createPlayer1SettingsControls() {
        Label label = new Label("Player 1");
        label.setPrefWidth(200);
        label.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(label, new Insets(0, 10, 0, 10));

        player1ComboBox = new ComboBox<>();
        player1ComboBox.getItems().addAll("Real player", "Iterative deepening", "Plain Minimax");
        player1ComboBox.setPrefWidth(200);
        VBox.setMargin(player1ComboBox, new Insets(5, 10, 0, 10));

        player1DepthTextField = new TextField("10");
        player1TimeoutTextField = new TextField("1000");
        HBox depthTimeoutHBox = new HBox(
                new VBox(new Label("Depth"), player1DepthTextField),
                new VBox(new Label("Timeout"), player1TimeoutTextField));
        depthTimeoutHBox.setPrefWidth(200);
        VBox.setMargin(depthTimeoutHBox, new Insets(5, 10, 10, 10));

        return new VBox(label, player1ComboBox, depthTimeoutHBox);
    }

    private Parent createPlayer2SettingsControls() {
        Label label = new Label("Player 2");
        label.setPrefWidth(200);
        label.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(label, new Insets(0, 10, 0, 10));

        player2ComboBox = new ComboBox<>();
        player2ComboBox.getItems().addAll("Real player", "Iterative deepening", "Plain Minimax");
        player2ComboBox.setPrefWidth(200);
        VBox.setMargin(player2ComboBox, new Insets(5, 10, 0, 10));

        player2DepthTextField = new TextField("10");
        player2TimeoutTextField = new TextField("1000");
        HBox depthTimeoutHBox = new HBox(
                new VBox(new Label("Depth"), player2DepthTextField),
                new VBox(new Label("Timeout"), player2TimeoutTextField));
        depthTimeoutHBox.setPrefWidth(200);
        VBox.setMargin(depthTimeoutHBox, new Insets(5, 10, 10, 10));

        return new VBox(label, player2ComboBox, depthTimeoutHBox);
    }

    private Parent createGameArea() {
        StackPane boardStackPane = new StackPane();

        boardControl = new BoardControl();
        boardStackPane.getChildren().add(boardControl);
        StackPane.setAlignment(boardControl, Pos.CENTER);

        gameStateLabel = new Label("Press 'Start Game' to start.");
        gameStateLabel.setFont(new Font(24));
        gameStateLabel.setPadding(new Insets(10));

        timeLabel = new Label("Move time: 00:00");
        timeLabel.setPadding(new Insets(0, 10, 0, 0));

        movesLabel = new Label("Moves: 0");
        movesLabel.setPadding(new Insets(0, 0, 0, 10));
        HBox timeAndMovesHBox = new HBox(timeLabel, movesLabel);
        timeAndMovesHBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(gameStateLabel, timeAndMovesHBox);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxHeight(80);

        boardStackPane.getChildren().add(vBox);
        StackPane.setAlignment(vBox, Pos.TOP_CENTER);

        return boardStackPane;
    }

}
