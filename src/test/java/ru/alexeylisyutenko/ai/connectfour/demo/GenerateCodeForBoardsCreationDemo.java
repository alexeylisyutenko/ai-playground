package ru.alexeylisyutenko.ai.connectfour.demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.generateGenuineGameBoardSequence;

public class GenerateCodeForBoardsCreationDemo {

    @Test
    void generateGenuineGameBoardsCode() throws InterruptedException {
        int games = 1;

        List<Board> boards = new ArrayList<>();
        for (int i = 0; i < games; i++) {
            boards.addAll(generateGenuineGameBoardSequence());
        }
        String code = generateListOfCreationCode(boards);
        System.out.println(code);
    }

    @Test
    void generateGenuineGameBoardsArrayCreationCode() {
        List<Board> boards = new ArrayList<>();
//        boards.addAll(Stream.generate(BoardHelpers::constructRandomTieBoard).distinct().limit(10).collect(Collectors.toList()));
//        boards.addAll(Stream.generate(BoardHelpers::constructRandomFinishedBoard).distinct().limit(10).collect(Collectors.toList()));
        boards.addAll(Stream.generate(BoardHelpers::constructRandomNonFinishedBoard).distinct().limit(30).collect(Collectors.toList()));
        String s = generateListOfCreationCodeForBoardArrays(boards);
        System.out.println(s);
    }

    @Disabled
    @Test
    void generateCode() {
        int boardsCount = 100;

        List<Board> boards = Stream.generate(BoardHelpers::constructRandomNonFinishedBoard)
                .distinct().limit(boardsCount).collect(Collectors.toList());

        ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();
        for (Board board : boards) {
            consoleBoardVisualizer.visualize(board);
        }
        System.out.println();

        String code = generateListOfCreationCode(boards);
        System.out.println(code);
    }

    private String generateListOfCreationCodeForBoardArrays(List<Board> boards) {
        StringBuilder sb = new StringBuilder();
        sb.append("List.of(\r\n");
        for (Board board : boards) {
            String boardCreationCode = constructBoardArrayCurrentPlayerIdPairCreation(board);
            sb.append(boardCreationCode).append(",\r\n");
        }
        sb.delete(sb.length() - 3, sb.length());
        sb.append("\r\n");
        sb.append(");");
        return sb.toString();
    }

    private String constructBoardArrayCurrentPlayerIdPairCreation(Board board) {
        StringBuilder sb = new StringBuilder();
        sb.append("\tPair.of(");
        sb.append(constructBoardArrayCreation(board));
        sb.append(", ").append(board.getCurrentPlayerId()).append(")");
        return sb.toString();
    }

    private String generateListOfCreationCode(List<Board> boards) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("List.of(\r\n");
        for (Board board : boards) {
            String boardCreationCode = constructBoardCreationJavaCode(board);
            stringBuilder.append(boardCreationCode).append(",\r\n");
        }
        stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length());
        stringBuilder.append("\r\n");
        stringBuilder.append(");");
        return stringBuilder.toString();
    }

    private String constructBoardCreationJavaCode(Board board) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\tnew DefaultBoard(constructBoardArray(");
        stringBuilder.append(constructBoardArrayCreation(board));
        stringBuilder.append("), ").append(board.getCurrentPlayerId()).append(")");
        return stringBuilder.toString();
    }

    private String constructBoardArrayCreation(Board board) {
        StringBuilder sb = new StringBuilder();

        sb.append("new int[][]{").append("\r\n");
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            sb.append("\t\t{");
            for (int column = 0; column < BOARD_WIDTH; column++) {
                sb.append(board.getCellPlayerId(row, column));
                if (column != BOARD_WIDTH - 1) {
                    sb.append(", ");
                }
            }
            sb.append("}");
            if (row != BOARD_HEIGHT - 1) {
                sb.append(",");
            }
            sb.append("\r\n");
        }
        sb.append("\t}");

        return sb.toString();
    }

}
