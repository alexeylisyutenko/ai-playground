package ru.alexeylisyutenko.ai.connectfour.gui;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang3.RandomUtils;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class JavaFxGame extends Application {
    private final static int SCENE_WIDTH = 1024;
    private final static int SCENE_HEIGHT = 768;

    private final static int TOKEN_RADIUS = 20;
    private final static int TOKEN_MARGIN = 10;
    private final static int GRID_OUTER_MARGIN = 10;

    private final static int CELL_SIZE = (TOKEN_RADIUS + TOKEN_MARGIN) * 2;
    private final static int GRID_WIDTH = CELL_SIZE * BOARD_WIDTH + GRID_OUTER_MARGIN * 2;
    private final static int GRID_HEIGHT = CELL_SIZE * BOARD_HEIGHT + GRID_OUTER_MARGIN * 2;

    private Pane tokenPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Connect Four Game");
        primaryStage.setScene(new Scene(createContent(), SCENE_WIDTH, SCENE_HEIGHT));
        primaryStage.show();
    }

    private Parent createContent() {
        StackPane root = new StackPane();

        Pane tokenPane = new Pane();
        tokenPane.setMaxWidth(GRID_WIDTH);
        tokenPane.setMaxHeight(GRID_HEIGHT);
        tokenPane.setMinWidth(GRID_WIDTH);
        tokenPane.setMinHeight(GRID_HEIGHT);

//        addDemoTokens(tokenPane);
        root.getChildren().add(tokenPane);
        StackPane.setAlignment(tokenPane, Pos.CENTER);

        Shape grid = createGrid();
        root.getChildren().add(grid);
        StackPane.setAlignment(grid, Pos.CENTER);

        // Start demo animation.
        root.setOnMouseClicked(event -> {
            int column = RandomUtils.nextInt(0, BOARD_WIDTH);

            Color color;
            boolean red = RandomUtils.nextBoolean();
            if (red) {
                color = Color.RED;
            } else {
                color = Color.DARKORANGE;
            }

            Token token = new Token(color);
            token.setCenterX(GRID_OUTER_MARGIN + TOKEN_MARGIN + TOKEN_RADIUS + CELL_SIZE * column);
            token.setCenterY(GRID_OUTER_MARGIN + TOKEN_MARGIN + TOKEN_RADIUS - CELL_SIZE);
            tokenPane.getChildren().add(token);

            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), token);
            translateTransition.setToY(6 * CELL_SIZE);
            translateTransition.play();
        });

        return root;
    }

    private Shape createGrid() {
        Shape shape = createGridShape();
        shape.setFill(Color.DARKBLUE);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(225.0);
        light.setElevation(45.0);
        Lighting lighting = new Lighting(light);
        lighting.setSurfaceScale(0.5);
        lighting.setSpecularConstant(0.8);
        shape.setEffect(lighting);

        return shape;
    }

    private Shape createGridShape() {
        Rectangle rectangle = new Rectangle(GRID_WIDTH, GRID_HEIGHT);
        rectangle.setArcHeight(30.0);
        rectangle.setArcWidth(30.0);
        Shape shape = rectangle;
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                Circle circle = new Circle(TOKEN_RADIUS);
                circle.setCenterX(GRID_OUTER_MARGIN + TOKEN_MARGIN + TOKEN_RADIUS + column * CELL_SIZE);
                circle.setCenterY(GRID_OUTER_MARGIN + TOKEN_MARGIN + TOKEN_RADIUS + row * CELL_SIZE);
                shape = Shape.subtract(shape, circle);
            }
        }
        return shape;
    }

    private void addDemoTokens(Pane tokenPane) {
        Token token = new Token(Color.RED);
        token.setCenterX(GRID_OUTER_MARGIN + TOKEN_MARGIN + TOKEN_RADIUS);
        token.setCenterY(GRID_OUTER_MARGIN + TOKEN_MARGIN + TOKEN_RADIUS);
        tokenPane.getChildren().add(token);

        Token token2 = new Token(Color.DARKORANGE);
        token2.setCenterX(GRID_OUTER_MARGIN + TOKEN_MARGIN + TOKEN_RADIUS + CELL_SIZE);
        token2.setCenterY(GRID_OUTER_MARGIN + TOKEN_MARGIN + TOKEN_RADIUS);
        tokenPane.getChildren().add(token2);
    }

    private static class Token extends Circle {
        public Token(Color color) {
            super(TOKEN_RADIUS, color);
            setupLighting();
        }

        private void setupLighting() {
            Light.Distant light = new Light.Distant();
            light.setAzimuth(225.0);
            light.setElevation(55.0);
            Lighting lighting = new Lighting(light);
            lighting.setSurfaceScale(1.5);
            setEffect(lighting);
        }
    }

}
