package ru.alexeylisyutenko.ai.connectfour.player;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.GameContext;
import ru.alexeylisyutenko.ai.connectfour.game.GameResult;
import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;

import java.util.List;

/**
 * Abstract player class which implement some basic player functionality like saving playerId when
 * {@link Player#gameStarted(int)} is called.
 */
public abstract class AbstractPlayer implements Player {
    protected int playerId;

    @Override
    public void gameStarted(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void gameFinished(GameResult gameResult) {
    }

    /**
     * Makes a move if it's valid or makes a random move.
     *
     * @param gameContext game context
     * @param move a move to make
     */
    protected void makeMoveIfPossibleOrAny(GameContext gameContext, int move) {
        List<Pair<Integer, Board>> possibleMoves = MinimaxHelper.getAllNextMoves(gameContext.getBoard());
        boolean moveIsValid = possibleMoves.stream().map(Pair::getLeft).anyMatch(m -> m == move);
        if (moveIsValid) {
            gameContext.makeMove(move);
        } else {
            gameContext.makeMove(possibleMoves.get(0).getLeft());
        }
    }
}
