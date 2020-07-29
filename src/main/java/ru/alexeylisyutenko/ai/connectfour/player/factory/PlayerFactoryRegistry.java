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
        factories.add(new RandomPlayerFactoryFactory());
        factories.add(new BasicEvaluationPlayerFactory());
        factories.add(new FocusedEvaluationPlayerFactory());
        factories.add(new EmptyEvaluationPlayerFactory());
        factories.add(new BetterEvaluationPlayerFactory());
        factories.add(new RandomizedEvaluationPlayerFactory());
        factories.add(new InternalEvaluationPlayerFactory());
        factories.add(new NearestNeighborsPlayerFactory());
        FACTORIES = Collections.unmodifiableList(factories);
    }

    private PlayerFactoryRegistry() {
    }

    public static List<PlayerFactory> getAll() {
        return FACTORIES;
    }

}
