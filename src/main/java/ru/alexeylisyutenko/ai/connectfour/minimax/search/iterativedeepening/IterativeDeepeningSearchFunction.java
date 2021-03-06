package ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal.TranspositionTableAlphaBetaTimeoutBasedSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.timeoutbasedsearchfunction.TimeoutBasedSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable.CacheBasedBestMoveTable;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable.CacheBasedTranspositionTable;

import java.util.Optional;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class IterativeDeepeningSearchFunction implements SearchFunction {

    private final int timeout;
    private final TimeoutBasedSearchFunction timeoutBasedSearchFunction;
    private final boolean verbose;
    private Move bestMove;

    public IterativeDeepeningSearchFunction(int timeout) {
        this(timeout, true);
    }

    public IterativeDeepeningSearchFunction(int timeout, boolean verbose) {
        this.timeout = timeout;
        this.timeoutBasedSearchFunction = createTimeBaseSearchFunction();
        this.verbose = verbose;
    }

    private TimeoutBasedSearchFunction createTimeBaseSearchFunction() {
        return new TranspositionTableAlphaBetaTimeoutBasedSearchFunction(new CacheBasedTranspositionTable(), new CacheBasedBestMoveTable());
    }

    @Override
    public Move search(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            throw new IllegalStateException("Search function was called on a terminal node");
        }

        bestMove = null;
        long endMillis = System.currentTimeMillis() + timeout;
        int currentDepth = 1;
        int depthLimit = BOARD_HEIGHT * BOARD_WIDTH - board.getNumberOfTokensOnBoard();
        while (System.currentTimeMillis() < endMillis && currentDepth <= depthLimit) {
            Optional<Move> moveOptional = timeoutBasedSearchFunction.search(board, currentDepth, evaluationFunction, (int) (endMillis - System.currentTimeMillis()));
            moveOptional.ifPresent(move -> bestMove = move);
            if (verbose) {
                System.out.println(String.format("ITERATIVE DEEPENING: Depth: %d, result: %s", currentDepth, moveOptional));
            }
            currentDepth++;
        }

        if (bestMove == null) {
            bestMove = getSomeMove(board);
        }

        return bestMove;
    }

    private Move getSomeMove(Board board) {
        Pair<Integer, Board> next = MinimaxHelper.getAllNextMoves(board).get(0);
        return new Move(next.getLeft(), 0);
    }
}
