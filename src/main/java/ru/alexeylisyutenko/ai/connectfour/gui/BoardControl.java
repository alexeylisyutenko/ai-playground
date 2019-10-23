package ru.alexeylisyutenko.ai.connectfour.gui;

import javafx.animation.TranslateTransition;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.Objects;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

class BoardControl extends Region {
    public final static int SCENE_WIDTH = 1024;
    public final static int SCENE_HEIGHT = 768;
    public final static int TOKEN_RADIUS = 20;
    public final static int TOKEN_MARGIN = 10;
    public final static int CELL_SIZE = (TOKEN_RADIUS + TOKEN_MARGIN) * 2;
    public final static int GRID_OUTER_MARGIN = 10;
    public final static int GRID_HEIGHT = CELL_SIZE * BOARD_HEIGHT + GRID_OUTER_MARGIN * 2;
    public final static int GRID_WIDTH = CELL_SIZE * BOARD_WIDTH + GRID_OUTER_MARGIN * 2;

    private final Token[][] tokens;
    private final Pane tokenPane;

    public BoardControl() {
        tokens = new Token[BOARD_HEIGHT][BOARD_WIDTH];
        tokenPane = createTokenPane();
        getChildren().add(tokenPane);
        getChildren().add(createGrid());

        setMinSize(GRID_WIDTH, GRID_HEIGHT);
        setMaxSize(GRID_WIDTH, GRID_HEIGHT);
    }

    /**
     *
     * @param row
     * @param column
     * @param color
     */
    public void displayToken(int row, int column, Color color) {
        Objects.requireNonNull(color, "color cannot be null");
        validateRowNumber(row);
        validateColumnNumber(column);

        hideToken(row, column);
        Token token = new Token(color);
        token.setCenterX(calculateTokenCenter(column));
        token.setCenterY(calculateTokenCenter(row));
        tokenPane.getChildren().add(token);
        tokens[row][column] = token;
    }

    /**
     *
     */
    public void displayTokenWithAnimation(int row, int column, Color color) {
        Objects.requireNonNull(color, "color cannot be null");
        validateRowNumber(row);
        validateColumnNumber(column);

        hideToken(row, column);

        double startY = calculateTokenCenter(-1);
        double distance = calculateTokenCenter(row) - startY;
        double speed = CELL_SIZE * BOARD_HEIGHT / 0.5;
        double duration = distance / speed;

        Token token = new Token(color);
        token.setCenterX(calculateTokenCenter(column));
        token.setCenterY(startY);
        tokenPane.getChildren().add(token);
        tokens[row][column] = token;

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), token);
        translateTransition.setToY(distance);
        translateTransition.play();
    }

    /**
     *
     * @param row
     * @param column
     */
    public void hideToken(int row, int column) {
        validateRowNumber(row);
        validateColumnNumber(column);

        if (tokens[row][column] != null) {
            tokenPane.getChildren().remove(tokens[row][column]);
            tokens[row][column] = null;
        }
    }

    /**
     *
     */
    public void hideAll() {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                tokens[row][column] = null;
            }
        }
        tokenPane.getChildren().clear();
    }

    private double calculateTokenCenter(int boardCoordinate) {
        return GRID_OUTER_MARGIN + TOKEN_MARGIN + TOKEN_RADIUS + boardCoordinate * CELL_SIZE;
    }

    private Pane createTokenPane() {
        Pane tokenPane = new Pane();
        tokenPane.setPrefSize(GRID_WIDTH, GRID_HEIGHT);
        return tokenPane;
    }

    private void validateColumnNumber(int column) {
        if (column < 0 || column >= BOARD_WIDTH) {
            throw new IllegalArgumentException(String.format("Incorrect column number '%d', it must be between '%d' and '%d'", column, 0, BOARD_WIDTH - 1));
        }
    }

    private void validateRowNumber(int row) {
        if (row < 0 || row >= BOARD_HEIGHT) {
            throw new IllegalArgumentException(String.format("Incorrect row number '%d', it must be between '%d' and '%d'", row, 0, BOARD_HEIGHT - 1));
        }
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

    static class Token extends Circle {
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
