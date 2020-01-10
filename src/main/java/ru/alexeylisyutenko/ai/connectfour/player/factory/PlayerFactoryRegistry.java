package ru.alexeylisyutenko.ai.connectfour.player.factory;

import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.main.gui.boardcontrol.BoardControl;
import ru.alexeylisyutenko.ai.connectfour.main.gui.player.GuiPlayer;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.InternalEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.IterativeDeepeningSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.player.MinimaxBasedPlayer;

import java.util.*;

/**
 * Global registry for PlayerFactory implementations.
 */
public final class PlayerFactoryRegistry {
    private static final List<PlayerFactory> FACTORIES;

    static {
        ArrayList<PlayerFactory> factories = new ArrayList<>();
        factories.add(new HumanPlayerFactory());
        factories.add(new IterativeDeepeningPlayerFactory());
        FACTORIES = Collections.unmodifiableList(factories);
    }

    private PlayerFactoryRegistry() {
    }

    public static List<PlayerFactory> getAll() {
        return FACTORIES;
    }

    private static class HumanPlayerFactory implements PlayerFactory {
        @Override
        public String getImplementationName() {
            return "Human player";
        }

        @Override
        public boolean isDepthArgumentRequired() {
            return false;
        }

        @Override
        public boolean isTimeoutArgumentRequited() {
            return false;
        }

        @Override
        public Player create(Integer depth, Integer timeout, Map<String, ?> additionalArguments) {
            Objects.requireNonNull(additionalArguments, "additionalArguments cannot be null");
            BoardControl boardControl = (BoardControl) additionalArguments.get("boardControl");
            return new GuiPlayer(boardControl);
        }
    }

    private static class IterativeDeepeningPlayerFactory implements PlayerFactory {
        @Override
        public String getImplementationName() {
            return "Iterative deepening";
        }

        @Override
        public boolean isDepthArgumentRequired() {
            return false;
        }

        @Override
        public boolean isTimeoutArgumentRequited() {
            return true;
        }

        @Override
        public Player create(Integer depth, Integer timeout, Map<String, ?> additionalArguments) {
            Objects.requireNonNull(timeout, "timeout cannot be null");
            if (timeout < 0) {
                throw new IllegalArgumentException("Illegal timeout: " + timeout);
            }
            return new MinimaxBasedPlayer(new IterativeDeepeningSearchFunction(timeout), new CachingEvaluationFunction(new InternalEvaluationFunction()), 1);
        }
    }
}
