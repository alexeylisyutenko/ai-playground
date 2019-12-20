package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.game.GameContext;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;

/**
 * Player based on minimax algorithm and it's modifications like alpha-beta and so on.
 */
public class MinimaxBasedPlayer extends AbstractPlayer {
    protected final SearchFunction searchFunction;
    protected final EvaluationFunction evaluationFunction;
    protected final int depth;
    private final boolean verbose;

    public MinimaxBasedPlayer(SearchFunction searchFunction, EvaluationFunction evaluationFunction, int depth) {
        this(searchFunction, evaluationFunction, depth, true);
    }

    public MinimaxBasedPlayer(SearchFunction searchFunction, EvaluationFunction evaluationFunction, int depth, boolean verbose) {
        this.searchFunction = searchFunction;
        this.evaluationFunction = evaluationFunction;
        this.depth = depth;
        this.verbose = verbose;
    }

    @Override
    public void requestMove(GameContext gameContext) {
        Move move = searchFunction.search(gameContext.getBoard(), depth, evaluationFunction);
        if (verbose) {
            System.out.println(String.format("MINIMAX: Decided on column %d with rating %d", move.getColumn(), move.getScore()));
        }
        gameContext.makeMove(move.getColumn());
    }
}
