package ru.alexeylisyutenko.ai.connectfour.minimax.evaluation;

import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class BestEvaluationFunction implements EvaluationFunction {

    private static final Map<Integer, Integer> lineScores = LineScoreCalculator.createPrecomputedScores();

    @Override
    public int evaluate(Board board) {
        int score;
        if (board.isGameOver()) {
            score = -1000000 + board.getNumberOfTokensOnBoard();
        } else {
            score = 0;
            score += getHorizontalLinesScore(board);
            score += getVerticalLinesScore(board);
            score += getDiagonalLinesScore(board);
        }
        return score;
    }

    private int getHorizontalLinesScore(Board board) {
        int score = 0;
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            int line = LineKeys.computeLineKey(
                    getRelativePlayerId(board, i, 0),
                    getRelativePlayerId(board, i, 1),
                    getRelativePlayerId(board, i, 2),
                    getRelativePlayerId(board, i, 3),
                    getRelativePlayerId(board, i, 4),
                    getRelativePlayerId(board, i, 5),
                    getRelativePlayerId(board, i, 6)
            );
            score += lineScores.get(line);
        }
        return score;
    }

    private int getVerticalLinesScore(Board board) {
        int score = 0;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            int line = LineKeys.computeLineKey(
                    getRelativePlayerId(board, 0, i),
                    getRelativePlayerId(board, 1, i),
                    getRelativePlayerId(board, 2, i),
                    getRelativePlayerId(board, 3, i),
                    getRelativePlayerId(board, 4, i),
                    getRelativePlayerId(board, 5, i)
            );
            score += lineScores.get(line);
        }
        return score;
    }

    private int getDiagonalLinesScore(Board board) {
        int score = 0;

        for (int row = 0; row < BOARD_HEIGHT - 3; row++) {
            for (int column = 0; column < BOARD_WIDTH - 3; column++) {
                int line = LineKeys.computeLineKey(
                        getRelativePlayerId(board, row, column),
                        getRelativePlayerId(board, row + 1, column + 1),
                        getRelativePlayerId(board, row + 2, column + 2),
                        getRelativePlayerId(board, row + 3, column + 3)
                );
                score += lineScores.get(line);
            }
        }

        for (int row = BOARD_HEIGHT - 3; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH - 3; column++) {
                int line = LineKeys.computeLineKey(
                        getRelativePlayerId(board, row, column),
                        getRelativePlayerId(board, row - 1, column + 1),
                        getRelativePlayerId(board, row - 2, column + 2),
                        getRelativePlayerId(board, row - 3, column + 3)
                );
                score += lineScores.get(line);
            }
        }

        return score;
    }

    private int getRelativePlayerId(Board board, int row, int column) {
        if (board.getCellPlayerId(row, column) == board.getCurrentPlayerId()) {
            return 1;
        } else if (board.getCellPlayerId(row, column) == board.getOtherPlayerId()) {
            return 2;
        } else {
            return 0;
        }
    }

    private static class LineKeys {
        public static int computeLineKey(int... line) {
            int key = 0;
            int bit = 0;

            // Add main bits.
            for (int i = 0; i < line.length; i++) {
                if (line[i] == 1) {
                    key = key | (0b01 << bit);
                }
                if (line[i] == 2) {
                    key = key | (0b10 << bit);
                }
                bit += 2;
            }

            // Add line size bits.
            switch (line.length) {
                case 4:
                    break;
                case 5:
                    key = key | 0x40000000;
                    break;
                case 6:
                    key = key | 0xC0000000;
                    break;
                case 7:
                    key = key | 0xF0000000;
                    break;
                default:
                    throw new IllegalArgumentException("Illegal line length: " + line.length);
            }

            return key;
        }
    }

    private static class LineScoreCalculator {
        public static Map<Integer, Integer> createPrecomputedScores() {
            Map<Integer, Integer> lineScores = new HashMap<>();
            preCompute(lineScores, new int[0], 7);
            preCompute(lineScores, new int[0], 6);
            preCompute(lineScores, new int[0], 5);
            preCompute(lineScores, new int[0], 4);
            return Collections.unmodifiableMap(lineScores);
        }

        private static void preCompute(Map<Integer, Integer> lineScores, int[] line, int lineLength) {
            if (lineLength == 0) {
                lineScores.put(LineKeys.computeLineKey(line), evaluateLine(line));
                return;
            }
            for (int i = 0; i < 3; i++) {
                int[] newLine = Arrays.copyOf(line, line.length + 1);
                newLine[newLine.length - 1] = i;
                preCompute(lineScores, newLine, lineLength - 1);
            }
        }

        private static int evaluateLine(int[] line) {
            int score = 0;
            for (int index = 0; index < line.length - 3; index++) {
                score += evaluateFor4TokenCombination(line, index);
            }
            return score;
        }

        private static int evaluateFor4TokenCombination(int[] line, int index) {
            int currentPlayerTokens = 0;
            int otherPlayerTokens = 0;

            for (int i = 0; i < 4; i++) {
                int cellPlayerId = line[index + i];
                if (cellPlayerId == 1) {
                    currentPlayerTokens++;
                } else if (cellPlayerId == 2) {
                    otherPlayerTokens++;
                }
            }

            if (currentPlayerTokens != 0 && otherPlayerTokens == 0) {
                return currentPlayerTokens * currentPlayerTokens;
            } else if (currentPlayerTokens == 0 && otherPlayerTokens != 0) {
                return -(otherPlayerTokens * otherPlayerTokens);
            } else {
                return 0;
            }
        }
    }

}
