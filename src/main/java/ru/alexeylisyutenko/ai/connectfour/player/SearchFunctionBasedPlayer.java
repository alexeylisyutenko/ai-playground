package ru.alexeylisyutenko.ai.connectfour.player;

import ru.alexeylisyutenko.ai.connectfour.game.GameContext;
import ru.alexeylisyutenko.ai.connectfour.minimax.BestMove;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;

/**
 *
 */
public class SearchFunctionBasedPlayer extends AbstractPlayer {
    private final SearchFunction searchFunction;
    private final EvaluationFunction evaluationFunction;
    private final int depth;

    public SearchFunctionBasedPlayer(SearchFunction searchFunction, EvaluationFunction evaluationFunction, int depth) {
        this.searchFunction = searchFunction;
        this.evaluationFunction = evaluationFunction;
        this.depth = depth;
    }

    @Override
    public void requestMove(GameContext gameContext) {
        BestMove bestMove = searchFunction.search(gameContext.getBoard(), depth, evaluationFunction);
        System.out.println(String.format("SEARCH: Decided on column %d with rating %d", bestMove.getColumn(), bestMove.getScore()));
        gameContext.makeMove(bestMove.getColumn());
    }
}
