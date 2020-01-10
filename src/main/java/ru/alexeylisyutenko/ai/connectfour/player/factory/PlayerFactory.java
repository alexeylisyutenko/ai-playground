package ru.alexeylisyutenko.ai.connectfour.player.factory;

import ru.alexeylisyutenko.ai.connectfour.game.Player;

import java.util.Map;

/**
 * Various {@link Player} implementations factory.
 */
public interface PlayerFactory {
    /**
     * Returns a name of {@link Player} implementation of the factory.
     *
     * @return name of {@link Player} implementation
     */
    String getImplementationName();

    /**
     * Shows if depth argument is required while creating a {@link Player} by this factory.
     *
     * @return true - depth argument is required, false - otherwise
     */
    boolean isDepthArgumentRequired();

    /**
     * Shows if timeout argument is required while creating a {@link Player} by this factory.
     *
     * @return true - depth argument is required, false - otherwise
     */
    boolean isTimeoutArgumentRequited();

    /**
     * Create a player.
     *
     * @param depth depth of minimax analysis
     * @param timeout timeout of a single search operation
     * @param additionalArguments additional arguments
     * @return new {@link Player}
     */
    Player create(Integer depth, Integer timeout, Map<String, ?> additionalArguments);
}
