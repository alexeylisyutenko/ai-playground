package ru.alexeylisyutenko.ai.connectfour.helper;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.*;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BasicEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.EvenBetterEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MultithreadedMinimaxSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.player.MinimaxBasedPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public final class BoardHelpers {
    private BoardHelpers() {
    }

    public static int[] constructBoardArray(int[][] boardDoubleArray) {
        validateArraySize(boardDoubleArray);
        int[] boardArray = new int[BOARD_HEIGHT * BOARD_WIDTH];
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                boardArray[BOARD_WIDTH * i + j] = boardDoubleArray[i][j];
            }
        }
        return boardArray;
    }

    private static void validateArraySize(int[][] boardDoubleArray) {
        if (boardDoubleArray.length != BOARD_HEIGHT) {
            throw new IllegalArgumentException("Incorrect outer array size");
        }
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            if (boardDoubleArray[i].length != BOARD_WIDTH) {
                throw new IllegalArgumentException("Incorrect inner array size");
            }
        }
    }

    public static void assertBoardArrayEquals(Board board, int[][] boardDoubleArray) {
        validateArraySize(boardDoubleArray);
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                String message = String.format("Incorrect cell value with row '%d' and column '%d'", row, column);
                assertEquals(boardDoubleArray[row][column], board.getCellPlayerId(row, column), message);
            }
        }
    }

    public static Board constructRandomNonFinishedBoard() {
        return constructRandomNonFinishedBoard(0, 30);
    }

    public static Board constructRandomNonFinishedBoard(int lowerBound, int upperBound) {
        int moves = RandomUtils.nextInt(lowerBound, upperBound);

        Board board = new DefaultBoard();

        boolean success;
        do {
            try {
                for (int i = 0; i < moves; i++) {
                    board = findRandomNonFinishingMove(board);
                }
                success = true;
            } catch (RuntimeException ignore) {
                success = false;
            }
        } while (!success);

        return board;
    }

    private static Board findRandomNonFinishingMove(Board board) {
        List<Pair<Integer, Board>> allNextMoves = MinimaxHelper.getAllNextMoves(board);
        Collections.shuffle(allNextMoves);

        for (Pair<Integer, Board> nextMove : allNextMoves) {
            Board nextMoveBoard = nextMove.getRight();
            if (!nextMoveBoard.isGameOver()) {
                return nextMoveBoard;
            }
        }
        throw new RuntimeException("There is no non finishing move for this board");
    }

    public static List<Board> generateGenuineGameBoardSequence() {
        Player player1 = new MinimaxBasedPlayer(new MultithreadedMinimaxSearchFunction(), new CachingEvaluationFunction(new EvenBetterEvaluationFunction()), 4);
        Player player2 = new MinimaxBasedPlayer(new MultithreadedMinimaxSearchFunction(), new CachingEvaluationFunction(new BasicEvaluationFunction()), 4);

        GameRunner gameRunner = new DefaultGameRunner(player1, player2, null);
        gameRunner.startGame();
        try {
            gameRunner.awaitGameStart();
            gameRunner.awaitGameStop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        List<Board> boards = new ArrayList<>(gameRunner.getBoardHistory());
        boards.remove(boards.size() - 1);
        return boards;
    }

}
