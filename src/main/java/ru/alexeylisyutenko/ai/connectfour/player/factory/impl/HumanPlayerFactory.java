package ru.alexeylisyutenko.ai.connectfour.player.factory.impl;

import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.main.gui.boardcontrol.BoardControl;
import ru.alexeylisyutenko.ai.connectfour.main.gui.player.GuiPlayer;
import ru.alexeylisyutenko.ai.connectfour.player.factory.AbstractPlayerFactory;
import ru.alexeylisyutenko.ai.connectfour.player.factory.PlayerFactory;

import java.util.Map;
import java.util.Objects;

public class HumanPlayerFactory extends AbstractPlayerFactory {
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
