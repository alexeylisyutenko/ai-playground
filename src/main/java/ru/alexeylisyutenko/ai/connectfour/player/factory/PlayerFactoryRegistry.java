package ru.alexeylisyutenko.ai.connectfour.player.factory;

import ru.alexeylisyutenko.ai.connectfour.player.factory.impl.*;

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
        factories.add(new RandomPlayerFactory());
        factories.add(new BasicEvaluationPlayer());
        factories.add(new FocusedEvaluationPlayer());
        factories.add(new EmptyEvaluationPlayer());
        factories.add(new BetterEvaluationPlayer());
        factories.add(new RandomizedEvaluationPlayer());
        FACTORIES = Collections.unmodifiableList(factories);
    }

    private PlayerFactoryRegistry() {
    }

    public static List<PlayerFactory> getAll() {
        return FACTORIES;
    }

}
