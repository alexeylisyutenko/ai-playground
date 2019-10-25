package ru.alexeylisyutenko.ai.connectfour.game;

import com.google.common.collect.ImmutableList;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;

import java.util.LinkedList;
import java.util.List;

// TODO: Refactor the code.

public class DefaultGameRunner implements GameRunner {
    private static final int DEFAULT_TIMEOUT = 10;

    private final int timeout;
    private final Player player1;
    private final Player player2;
    private final Board initialBoard;
    private final GameEventListener gameEventListener;
    private final List<Board> boardHistory;

    private GameState state;
    private Board board;

    public DefaultGameRunner(Player player1, Player player2, int timeout, Board initialBoard, GameEventListener gameEventListener) {
        this.player1 = player1;
        this.player2 = player2;
        this.timeout = timeout;
        this.initialBoard = initialBoard;
        this.state = GameState.STOPPED;
        this.gameEventListener = gameEventListener;
        this.boardHistory = new LinkedList<>();
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
    public GameState getState() {
        return state;
    }

    @Override
    public List<Board> getBoardHistory() {
        return ImmutableList.copyOf(boardHistory);
    }

    @Override
    public void startGame() {
        if (state != GameState.STOPPED) {
            throw new IllegalStateException("Game is already started. You should stop current main first.");
        }
        boardHistory.clear();
        board = initialBoard;
        boardHistory.add(board);
        notifyPlayersGameStarted();
        invokeGameStartedEvent();
        processGameStateTransition();
    }

    @Override
    public void stopGame() {
        if (state == GameState.STOPPED) {
            return;
        }
        finishGame(GameResult.stoppedGame());
    }

    private void makeMove(int column) {
        Board nextBoard;
        try {
            nextBoard = this.board.makeMove(column);
        } catch (InvalidMoveException e) {
            invokeIllegalMoveAttemptedEvent(e.getColumn());
            requestNextPlayerMove();
            return;
        }
        board = nextBoard;
        boardHistory.add(board);
        invokeMoveMadeEvent(column);
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
        // TODO: Fix a problem here. As soon as we call player1 request move and as soon as it is
        //  blocking all the game goes inside recursive call chain. We need introduce asynchronous game event processing.

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

    private void notifyPlayersGameFinished(GameResult gameResult) {
        player1.gameFinished(gameResult);
        player2.gameFinished(gameResult);
    }

    private int getLoserId(int winnerId) {
        return winnerId == 1 ? 2 : 1;
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
        public List<Board> getBoardHistory() {
            return DefaultGameRunner.this.getBoardHistory();
        }

        @Override
        public void makeMove(int column) {
            DefaultGameRunner.this.makeMove(column);
        }
    }

}
