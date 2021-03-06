package ru.alexeylisyutenko.ai.connectfour.main.gui.boardcontrol;

import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class BoardControl extends Region {
    public final static int TOKEN_RADIUS = 20;
    public final static int TOKEN_MARGIN = 10;
    public final static int CELL_SIZE = (TOKEN_RADIUS + TOKEN_MARGIN) * 2;
    public final static int GRID_OUTER_MARGIN = 10;
    public final static int GRID_HEIGHT = CELL_SIZE * BOARD_HEIGHT + GRID_OUTER_MARGIN * 2;
    public final static int GRID_WIDTH = CELL_SIZE * BOARD_WIDTH + GRID_OUTER_MARGIN * 2;

    private final Token[][] tokens;
    private final Pane tokenPane;
    private final List<Rectangle> columnRectangles;
    private final Shape[][] crosses;

    public BoardControl() {
        tokens = new Token[BOARD_HEIGHT][BOARD_WIDTH];
        tokenPane = createTokenPane();
        columnRectangles = createColumnRectangles();
        crosses = new Shape[BOARD_HEIGHT][BOARD_WIDTH];

        getChildren().add(tokenPane);
        getChildren().add(createGrid());
        getChildren().addAll(columnRectangles);
    }

    /**
     * Display a token in a particular position defined by row and column.
     *
     * @param row    row of the token
     * @param column column of the token
     * @param color  token color
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
     * Display and animate a token in a particular position defined by row and column.
     *
     * @param row    row of the token
     * @param column column of the token
     * @param color  token color
     */
    public void displayTokenWithAnimation(int row, int column, Color color) {
        Objects.requireNonNull(color, "color cannot be null");
        validateRowNumber(row);
        validateColumnNumber(column);

        hideToken(row, column);

        double startY = calculateTokenCenter(-1);
        double distance = calculateTokenCenter(row) - startY;
        double speed = CELL_SIZE * BOARD_HEIGHT / 0.3;
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
     * Displays a cross mark in a particular position defined by row and column.
     *
     * @param row    row of a cross
     * @param column column of a cross
     */
    public void displayCross(int row, int column) {
        validateRowNumber(row);
        validateColumnNumber(column);

        if (crosses[row][column] != null) {
            return;
        }

        double centerX = calculateTokenCenter(column);
        double centerY = calculateTokenCenter(row);

        Shape cross = createCrossShape(centerX, centerY);
        tokenPane.getChildren().addAll(cross);
        crosses[row][column] = cross;
    }

    private Shape createCrossShape(double centerX, double centerY) {
        Line line1 = new Line();
        line1.setStartX(centerX - 7.07);
        line1.setStartY(centerY - 7.07);
        line1.setEndX(centerX + 7.07);
        line1.setEndY(centerY + 7.07);
        line1.setStrokeWidth(2.0);

        Line line2 = new Line();
        line2.setStartX(centerX + 7.07);
        line2.setStartY(centerY - 7.07);
        line2.setEndX(centerX - 7.07);
        line2.setEndY(centerY + 7.07);
        line2.setStrokeWidth(2.0);

        return Shape.union(line1, line2);
    }

    /**
     * Hide a token in a particular position.
     *
     * @param row    token row
     * @param column token column
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
     * Hide all tokens and all crosses on the board.
     */
    public void hideAll() {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                tokens[row][column] = null;
                crosses[row][column] = null;
            }
        }
        tokenPane.getChildren().clear();
    }

    @Override
    protected double computeMinWidth(double height) {
        return GRID_WIDTH;
    }

    @Override
    protected double computeMinHeight(double width) {
        return GRID_HEIGHT;
    }

    @Override
    protected double computeMaxWidth(double height) {
        return GRID_WIDTH;
    }

    @Override
    protected double computeMaxHeight(double width) {
        return GRID_HEIGHT;
    }

    private List<Rectangle> createColumnRectangles() {
        List<Rectangle> rectangles = new ArrayList<>();
        for (int column = 0; column < BOARD_WIDTH; column++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setHeight(GRID_HEIGHT);
            rectangle.setWidth(CELL_SIZE);
            rectangle.setX(GRID_OUTER_MARGIN + column * CELL_SIZE);
            rectangle.setY(0.0);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.rgb(200, 200, 50, 0.2)));
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));

            int finalColumn = column;
            rectangle.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    this.fireEvent(new ColumnClickEvent(finalColumn));
                }
            });

            rectangles.add(rectangle);
        }
        return rectangles;
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

    private static class Token extends Circle {
        Token(Color color) {
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

    /**
     * An {@link Event} subclass used specifically in BoardControl for representing column click events.
     */
    public static class ColumnClickEvent extends Event {
        public static final EventType<ColumnClickEvent> COLUMN_CLICKED = new EventType<>(Event.ANY, "COLUMN_CLICKED");

        private final int column;

        public ColumnClickEvent(int column) {
            super(COLUMN_CLICKED);
            this.column = column;
        }

        public int getColumn() {
            return column;
        }
    }
}
