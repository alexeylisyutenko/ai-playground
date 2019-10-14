package ru.alexeylisyutenko.ai.connectfour.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class JavaFxGameOption2 extends Application {
    private final static int SCENE_WIDTH = 1024;
    private final static int SCENE_HEIGHT = 768;

    private final static int TOKEN_RADIUS = 20;
    private final static int TOKEN_MARGIN = 10;
    private final static int GRID_OUTER_MARGIN = 10;

    private final static int CELL_SIZE = (TOKEN_RADIUS + TOKEN_MARGIN) * 2;
    private final static int GRID_WIDTH = CELL_SIZE * BOARD_WIDTH + GRID_OUTER_MARGIN * 2;
    private final static int GRID_HEIGHT = CELL_SIZE * BOARD_HEIGHT + GRID_OUTER_MARGIN * 2;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Connect Four Game");
        primaryStage.setScene(new Scene(createContent(), SCENE_WIDTH, SCENE_HEIGHT));
        primaryStage.show();
    }

    private Parent createContent() {
        StackPane stackPane = new StackPane();
        Shape grid = createGrid();
        stackPane.getChildren().add(grid);
        StackPane.setAlignment(grid, Pos.CENTER);
        return stackPane;
    }

    private Shape createGrid() {
        Rectangle rectangle = new Rectangle(GRID_WIDTH, GRID_HEIGHT);
        rectangle.setArcHeight(30.0);
        rectangle.setArcWidth(30.0);

        Shape shape = rectangle;

        // Cut holes in shape.
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                Circle circle = new Circle(TOKEN_RADIUS);
                circle.setCenterX(GRID_OUTER_MARGIN + TOKEN_MARGIN + TOKEN_RADIUS + column * CELL_SIZE);
                circle.setCenterY(GRID_OUTER_MARGIN + TOKEN_MARGIN + TOKEN_RADIUS + row * CELL_SIZE);
                shape = Shape.subtract(shape, circle);
            }
        }
        shape.setFill(Color.BLUE);

        // Add light.
        Light.Distant light = new Light.Distant();
        light.setAzimuth(225.0);
        light.setElevation(45.0);
        Lighting lighting = new Lighting(light);
        lighting.setSurfaceScale(0.5);
        lighting.setSpecularConstant(0.8);

        // Add shadow effect.
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        dropShadow.setInput(lighting);

        shape.setEffect(dropShadow);

        return shape;
    }

}
