package ru.alexeylisyutenko.ai.connectfour.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.stage.Stage;

public class JavaFxGame extends Application {

    private final static int FRAME_WIDTH = 420;
    private final static int FRAME_HEIGHT = 360;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Connect Four Game");

        StackPane p = new StackPane();

        Group group = createCanvasGroup();
        p.getChildren().add(group);
        StackPane.setAlignment(group, Pos.CENTER);

        primaryStage.setScene(new Scene(p, 1024, 768));
        primaryStage.show();
    }

    private Group createCanvasGroup() {
        Group group = new Group();
        Canvas canvas = new Canvas(FRAME_WIDTH, FRAME_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawFrame(gc);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        canvas.setEffect(dropShadow);

        drawTokenInBoardCoordinates(5, 0, 1, gc);
        drawTokenInBoardCoordinates(4, 0, 1, gc);
        drawTokenInBoardCoordinates(3, 0, 1, gc);
        drawTokenInBoardCoordinates(5, 2, 2, gc);
        drawTokenInBoardCoordinates(5, 3, 2, gc);
        drawTokenInBoardCoordinates(5, 4, 1, gc);
        drawTokenInBoardCoordinates(5, 5, 2, gc);
        drawTokenInBoardCoordinates(4, 3, 1, gc);
        drawTokenInBoardCoordinates(4, 4, 1, gc);
        drawTokenInBoardCoordinates(3, 4, 2, gc);
        drawTokenInBoardCoordinates(5, 6, 2, gc);

        group.getChildren().add(canvas);
        return group;
    }

    void drawTokenInBoardCoordinates(int row, int column, int playerId, GraphicsContext gc) {
        if (row < 0 || row > 5) {
            throw new IllegalArgumentException("Incorrect row value");
        }
        if (column < 0 || column > 6) {
            throw new IllegalArgumentException("Incorrect column value");
        }

        double x = 10 + column * 60;
        double y = 10 + row * 60;

        if (playerId == 1) {
            drawRedToken(x, y, gc);
        } else {
            drawYellowToken(x, y, gc);
        }
    }

    private void drawRedToken(double x, double y, GraphicsContext gc) {
//        Color lighterColor = Color.rgb(254, 193, 193);
//        Color baseColor = Color.rgb(200, 0, 0);
        Color lighterColor = Color.RED;
        Color baseColor = Color.DARKRED;

        drawToken(x, y, baseColor, lighterColor, gc);
    }

    private void drawYellowToken(double x, double y, GraphicsContext gc) {
        Color lighterColor = Color.LIGHTYELLOW;
        Color baseColor = Color.DARKORANGE;

//        Color lighterColor = Color.rgb(242, 240, 210);
//        Color baseColor = Color.rgb(178, 170, 48);

        drawToken(x, y, baseColor, lighterColor, gc);
    }

    private void drawToken(double x, double y, Color baseColor, Color lighterColor, GraphicsContext gc) {
        Stop[] stops;
        LinearGradient gradient;

        // Outer circle
        stops = new Stop[]{new Stop(0, lighterColor), new Stop(1, baseColor)};
        gradient = new LinearGradient(0.5, 0, 0.5, 1, true, CycleMethod.NO_CYCLE, stops);
        gc.setFill(gradient);
        gc.fillOval(x, y, 40, 40);
        gc.fill();
        gc.stroke();

        // Inner circle
        stops = new Stop[]{new Stop(0, baseColor), new Stop(1, lighterColor)};
        gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        gc.setFill(gradient);
        gc.fillOval(x + 3, y + 3, 34, 34);
        gc.fill();
        gc.stroke();
    }

    private void drawFrame(GraphicsContext gc) {
        Color frameColor = Color.DARKBLUE;

        // Square
        LinearGradient lg = new LinearGradient(0, 0, 1, 1, true,
                CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.BLUE),
                new Stop(1.0, frameColor));


        gc.beginPath();
        gc.setFill(lg);
        gc.fillRoundRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT, 30, 30);
        gc.fill();

        // Empty slots
        for (int row = 0; row < 6; row++) {
            for (int column = 0; column < 7; column++) {
                double x = 10 + column * 60;
                double y = 10 + row * 60;
                drawEmptySlot(x, y, gc);
            }
        }
    }

    private void drawEmptySlot(double x, double y, GraphicsContext gc) {
        Stop[] stops;
        LinearGradient gradient;

        // Circle shadow
        gc.beginPath();
        stops = new Stop[]{new Stop(0, Color.GRAY), new Stop(1, Color.WHITE)};
        gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        gc.setFill(gradient);
        gc.fillOval(x, y, 40, 40);
        gc.fill();
        gc.setStroke(Color.WHITE);

        // Circle
        Color circleColor = Color.rgb(217, 217, 255);
        gc.setFill(circleColor);
        gc.fillOval(x + 2, y + 2, 38, 38);
        gc.fill();
        gc.setStroke(Color.WHITE);
        gc.stroke();
    }


}
