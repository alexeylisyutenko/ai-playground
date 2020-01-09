package ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.*;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal.TranspositionTableAlphaBetaTimeoutBasedSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal.TranspositionTableYBWCAlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.timeoutbasedsearchfunction.DefaultTimeoutBasedSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.timeoutbasedsearchfunction.TimeoutBasedSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable.*;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class IterativeDeepeningSearchFunction implements SearchFunction {
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    private final int timeout;
    private final TimeoutBasedSearchFunction timeoutBasedSearchFunction;
    private Move bestMove;

    public IterativeDeepeningSearchFunction(int timeout) {
        this.timeout = timeout;
        this.timeoutBasedSearchFunction = createTimeBaseSearchFunction();
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
            System.out.println(String.format("ITERATIVE DEEPENING: Depth: %d, result: %s", currentDepth, moveOptional));
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
