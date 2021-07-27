package ru.alexeylisyutenko.ai.connectfour.player.factory;

import ru.alexeylisyutenko.ai.connectfour.player.factory.impl.*;

import java.util.List;

/**
 * Global registry for PlayerFactory implementations.
 */
public final class PlayerFactoryRegistry {
    private static final List<PlayerFactory> FACTORIES;

    static {
        FACTORIES = List.of(
                new HumanPlayerFactory(),
                new IterativeDeepeningPlayerFactory(),
                new RandomPlayerFactoryFactory(),
                new BasicEvaluationPlayerFactory(),
                new FocusedEvaluationPlayerFactory(),
                new EmptyEvaluationPlayerFactory(),
                new BetterEvaluationPlayerFactory(),
                new RandomizedEvaluationPlayerFactory(),
                new InternalEvaluationPlayerFactory(),
                new NearestNeighborsPlayerFactory(),
                new NaiveDeepLearningPlayerFactory(),
                new KerasDeepLearningPlayerFactory()
        );
    }

    private PlayerFactoryRegistry() {
    }

    public static List<PlayerFactory> getAll() {
        return FACTORIES;
    }

}
