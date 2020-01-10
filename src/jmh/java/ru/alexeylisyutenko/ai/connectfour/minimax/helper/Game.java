package ru.alexeylisyutenko.ai.connectfour.minimax.helper;

import ru.alexeylisyutenko.ai.connectfour.game.BitBoard;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.RandomizedEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MinimaxSearchFunction;

public class Game {
    private final static MinimaxSearchFunction minimaxSearchFunction = new MinimaxSearchFunction();
    private final static EvaluationFunction randomizedEvaluationFunction = new RandomizedEvaluationFunction();

    private Game() {
    }

    public static void runGame(SearchFunction searchFunction, EvaluationFunction evaluationFunction, int depth) {
        Board board = new BitBoard();
        while (!board.isGameOver()) {
            Move move;
            if (board.getCurrentPlayerId() == 1) {
                move = minimaxSearchFunction.search(board, 3, randomizedEvaluationFunction);
            } else {
                move = searchFunction.search(board, depth, evaluationFunction);
            }
            board = board.makeMove(move.getColumn());
        }
    }
}
