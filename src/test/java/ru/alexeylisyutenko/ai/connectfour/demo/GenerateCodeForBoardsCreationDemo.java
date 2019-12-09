package ru.alexeylisyutenko.ai.connectfour.demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class GenerateCodeForBoardsCreationDemo {

    @Disabled
    @Test
    void generateCode() {
        int boardsCount = 20;

        List<Board> boards = Stream.generate(BoardHelpers::constructRandomNonFinishedBoard)
                .limit(boardsCount).collect(Collectors.toList());

        ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();
        for (Board board : boards) {
            consoleBoardVisualizer.visualize(board);
        }
        System.out.println();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("List.of(\r\n");
        for (Board board : boards) {
            String boardCreationCode = constructBoardCreationJavaCode(board);
            stringBuilder.append(boardCreationCode).append(",\r\n");
        }
        stringBuilder.delete(stringBuilder.length()-3, stringBuilder.length());
        stringBuilder.append("\r\n");
        stringBuilder.append(");");
        String code = stringBuilder.toString();

        System.out.println(code);
    }

    private String constructBoardCreationJavaCode(Board board) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\tnew DefaultBoard(constructBoardArray(new int[][]{").append("\r\n");
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            stringBuilder.append("\t\t{");
            for (int column = 0; column < BOARD_WIDTH; column++) {
                stringBuilder.append(board.getCellPlayerId(row, column));
                if (column != BOARD_WIDTH - 1) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append("}");
            if (row != BOARD_HEIGHT - 1) {
                stringBuilder.append(",");
            }
            stringBuilder.append("\r\n");
        }
        stringBuilder.append("\t}), ").append(board.getCurrentPlayerId()).append(")");
        return stringBuilder.toString();
    }
}
