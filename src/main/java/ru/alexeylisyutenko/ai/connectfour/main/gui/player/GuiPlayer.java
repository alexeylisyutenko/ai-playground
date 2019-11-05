package ru.alexeylisyutenko.ai.connectfour.main.gui.player;

import javafx.event.EventHandler;
import ru.alexeylisyutenko.ai.connectfour.game.GameContext;
import ru.alexeylisyutenko.ai.connectfour.game.GameResult;
import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.main.gui.boardcontrol.BoardControl;

/**
 * Real human player which is controlled via GUI.
 */
public class GuiPlayer implements Player {
    private final BoardControl boardControl;
    private EventHandler<BoardControl.ColumnClickEvent> eventHandler;

    public GuiPlayer(BoardControl boardControl) {
        this.boardControl = boardControl;
    }

    @Override
    public void gameStarted(int playerId) {
        // Do nothing
    }

    @Override
    public void requestMove(GameContext gameContext) {
        eventHandler = event -> {
            int column = event.getColumn();
            gameContext.makeMove(column);
            removeBoardControlColumnClickedEventHandler();
        };
        boardControl.addEventHandler(BoardControl.ColumnClickEvent.COLUMN_CLICKED, eventHandler);
    }

    private void removeBoardControlColumnClickedEventHandler() {
        if (eventHandler != null) {
            boardControl.removeEventHandler(BoardControl.ColumnClickEvent.COLUMN_CLICKED, eventHandler);
            eventHandler = null;
        }
    }

    @Override
    public void gameFinished(GameResult gameResult) {
        removeBoardControlColumnClickedEventHandler();
    }
}
