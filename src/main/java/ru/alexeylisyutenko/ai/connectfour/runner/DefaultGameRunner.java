package ru.alexeylisyutenko.ai.connectfour.runner;

import ru.alexeylisyutenko.ai.connectfour.Board;
import ru.alexeylisyutenko.ai.connectfour.DefaultBoard;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;
import ru.alexeylisyutenko.ai.connectfour.player.Player;

// TODO: Refactor the code.
// TODO: Add move history.

public class DefaultGameRunner implements GameRunner {
    private static final int DEFAULT_TIMEOUT = 10;

    private final int timeout;
    private final Player player1;
    private final Player player2;
    private final Board initialBoard;
    private final GameEventListener gameEventListener;

    private GameState state;
    private Board board;

    public DefaultGameRunner(Player player1, Player player2, int timeout, Board initialBoard, GameEventListener gameEventListener) {
        this.player1 = player1;
        this.player2 = player2;
        this.timeout = timeout;
        this.initialBoard = initialBoard;
        this.state = GameState.STOPPED;
        this.gameEventListener = gameEventListener;
    }

    public DefaultGameRunner(Player player1, Player player2) {
        this(player1, player2, DEFAULT_TIMEOUT, new DefaultBoard(), null);
    }

    public DefaultGameRunner(Player player1, Player player2, GameEventListener gameEventListener) {
        this(player1, player2, DEFAULT_TIMEOUT, new DefaultBoard(), gameEventListener);
    }

    @Override
    public int getTimeLimit() {
        return timeout;
    }

    @Override
    public Player getPlayer1() {
        return player1;
    }

    @Override
    public Player getPlayer2() {
        return player2;
    }

    @Override
    public void startGame() {
        // You can start the game only when it is in the STOPPED state.
        if (state != GameState.STOPPED) {
            throw new IllegalStateException("Game is already started. You should stop current game first.");
        }

        // Call gameStarted methods for both players.
        notifyPlayersGameStarted();

        // Initialize board.
        board = initialBoard;

        // Invoke game started event.
        invokeGameStartedEvent();

        // Process further game state transitions.
        processGameStateTransition();
    }

    @Override
    public void stopGame() {
        // TODO: Implement.
    }

    private void makeMove(int column) {
        Board newBoard;
        try {
            newBoard = this.board.makeMove(column);
        } catch (InvalidMoveException e) {
            // Invoke illegal move attempted event.
            invokeIllegalMoveAttemptedEvent(e.getColumn());

            // Request for a move again.
            requestNextPlayerMove();

            return;
        }

        board = newBoard;

        // Invoke move made event.
        invokeMoveMadeEvent(column);

        //
        processGameStateTransition();
    }

    private void processGameStateTransition() {
        int winnerId = board.getWinnerId();
        if (winnerId != 0) {
            finishGame(GameResult.normalVictory(winnerId, getLoserId(winnerId)));
        } else if (board.isTie()) {
            finishGame(GameResult.tie());
        } else {
            requestNextPlayerMove();
        }
    }

    private void finishGame(GameResult gameResult) {
        notifyPlayersGameFinished(gameResult);
        invokeGameFinishedEvent(gameResult);
        state = GameState.STOPPED;
    }

    private void requestNextPlayerMove() {
        if (board.getCurrentPlayerId() == 1) {
            player1.requestMove(new DefaultGameContext());
            state = GameState.WAITING_FOR_PLAYER1_MOVE;
        } else {
            player2.requestMove(new DefaultGameContext());
            state = GameState.WAITING_FOR_PLAYER2_MOVE;
        }
        invokeMoveRequestedEvent();
    }

    private void notifyPlayersGameStarted() {
        player1.gameStarted(1);
        player2.gameStarted(2);
    }

    private int getLoserId(int winnerId) {
        return winnerId == 1 ? 2 : 1;
    }

    private void notifyPlayersGameFinished(GameResult gameResult) {
        player1.gameFinished(gameResult);
        player2.gameFinished(gameResult);
    }

    private void invokeGameStartedEvent() {
        if (gameEventListener != null) {
            gameEventListener.gameStarted(this, board);
        }
    }

    private void invokeMoveMadeEvent(int column) {
        if (gameEventListener != null) {
            gameEventListener.moveMade(this, board.getOtherPlayerId(), column, board);
        }
    }

    private void invokeIllegalMoveAttemptedEvent(int column) {
        if (gameEventListener != null) {
            gameEventListener.illegalMoveAttempted(this, board.getCurrentPlayerId(), column, board);
        }
    }

    private void invokeGameFinishedEvent(GameResult gameResult) {
        if (gameEventListener != null) {
            gameEventListener.gameFinished(this, gameResult);
        }
    }

    private void invokeMoveRequestedEvent() {
        if (gameEventListener != null) {
            gameEventListener.moveRequested(this, board.getCurrentPlayerId(), board);
        }
    }

    // TODO: Add playerId here and add check so that only player whose turn it is could make a move.
    private class DefaultGameContext implements GameContext {
        @Override
        public int getTimeout() {
            return DefaultGameRunner.this.timeout;
        }

        @Override
        public Board getBoard() {
            return DefaultGameRunner.this.board;
        }

        @Override
        public void makeMove(int column) {
            DefaultGameRunner.this.makeMove(column);
        }
    }

}
