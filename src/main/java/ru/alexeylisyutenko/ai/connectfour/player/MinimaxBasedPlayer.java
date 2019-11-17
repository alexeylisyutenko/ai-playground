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

    public MinimaxBasedPlayer(SearchFunction searchFunction, EvaluationFunction evaluationFunction, int depth) {
        this.searchFunction = searchFunction;
        this.evaluationFunction = evaluationFunction;
        this.depth = depth;
    }

    @Override
    public void requestMove(GameContext gameContext) {
        Move move = searchFunction.search(gameContext.getBoard(), depth, evaluationFunction);
        System.out.println(String.format("MINIMAX: Decided on column %d with rating %d", move.getColumn(), move.getScore()));
        gameContext.makeMove(move.getColumn());
    }
}
