package ru.alexeylisyutenko.ai.connectfour.game;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.Value;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO: Refactor the code.
// TODO: DefaultGameRunner must have a separate thread for event processing.
// TODO: Check for concurrency issues.

public class DefaultGameRunner implements GameRunner {
    private static final int DEFAULT_TIMEOUT = 10;

    private final ExecutorService eventExecutorService;
    private final EventBus eventBus;
    private final int timeout;
    private final Player player1;
    private final Player player2;
    private final Board initialBoard;
    private final GameEventListener gameEventListener;

    // TODO: Extract this 3 fields to separate class and add synchronization.
    private final List<Board> boardHistory;
    private GameState state;
    private Board board;
    //

    public DefaultGameRunner(Player player1, Player player2, int timeout, Board initialBoard, GameEventListener gameEventListener) {
        this.eventExecutorService = Executors.newSingleThreadExecutor();
        this.eventBus = new AsyncEventBus(eventExecutorService);
        this.player1 = player1;
        this.player2 = player2;
        this.timeout = timeout;
        this.initialBoard = initialBoard;
        this.gameEventListener = gameEventListener;

        this.boardHistory = Collections.synchronizedList(new LinkedList<>());
        this.state = GameState.STOPPED;
        this.board = null;

        eventBus.register(this);
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
    public synchronized GameState getState() {
        return state;
    }

    private synchronized void setState(GameState state) {
        this.state = state;
    }

    @Override
    public List<Board> getBoardHistory() {
        return ImmutableList.copyOf(boardHistory);
    }

    private synchronized Board getBoard() {
        return board;
    }

    private synchronized void setBoard(Board board) {
        this.board = board;
    }

    @Subscribe
    private void gameStartRequestedEvent(GameStartRequestedEvent event) {
        if (getState() != GameState.STOPPED) {
            return;
        }

        boardHistory.clear();
        setBoard(initialBoard);
        boardHistory.add(getBoard());

        notifyPlayersGameStarted();
        invokeGameStartedEvent();
        processGameStateTransition();
    }

    @Subscribe
    private void gameStopRequestedEvent(GameStopRequestedEvent event) {
        if (getState() == GameState.STOPPED) {
            return;
        }
        finishGame(GameResult.stoppedGame());
    }

    @Subscribe
    private void moveRequestedEvent(MoveRequestedEvent event) {
        if (getState() == GameState.STOPPED) {
            return;
        }

        Board nextBoard;
        try {
            nextBoard = this.board.makeMove(event.getColumn());
        } catch (InvalidMoveException e) {
            invokeIllegalMoveAttemptedEvent(e.getColumn());
            requestNextPlayerMove();
            return;
        }
        setBoard(nextBoard);
        boardHistory.add(getBoard());
        invokeMoveMadeEvent(event.getColumn());
        processGameStateTransition();
    }

    @Override
    public void startGame() {
        eventBus.post(new GameStartRequestedEvent());
    }

    @Override
    public void stopGame() {
        eventBus.post(new GameStopRequestedEvent());
    }

    @Override
    public void shutdown() {
        eventExecutorService.shutdown();
    }

    private void processGameStateTransition() {
        int winnerId = getBoard().getWinnerId();
        if (winnerId != 0) {
            finishGame(GameResult.normalVictory(winnerId, getLoserId(winnerId)));
        } else if (getBoard().isTie()) {
            finishGame(GameResult.tie());
        } else {
            requestNextPlayerMove();
        }
    }

    private void finishGame(GameResult gameResult) {
        notifyPlayersGameFinished(gameResult);
        invokeGameFinishedEvent(gameResult);
        setState(GameState.STOPPED);
    }

    private void requestNextPlayerMove() {
        invokeMoveRequestedEvent();
        if (getBoard().getCurrentPlayerId() == 1) {
            player1.requestMove(new DefaultGameContext(eventBus, timeout, getBoard(), boardHistory, 1));
            setState(GameState.WAITING_FOR_PLAYER1_MOVE);
        } else {
            player2.requestMove(new DefaultGameContext(eventBus, timeout, getBoard(), boardHistory, 2));
            setState(GameState.WAITING_FOR_PLAYER2_MOVE);
        }
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
            gameEventListener.gameStarted(this, getBoard());
        }
    }

    private void invokeMoveMadeEvent(int column) {
        if (gameEventListener != null) {
            gameEventListener.moveMade(this, getBoard().getOtherPlayerId(), column, getBoard());
        }
    }

    private void invokeIllegalMoveAttemptedEvent(int column) {
        if (gameEventListener != null) {
            gameEventListener.illegalMoveAttempted(this, getBoard().getCurrentPlayerId(), column, getBoard());
        }
    }

    private void invokeGameFinishedEvent(GameResult gameResult) {
        if (gameEventListener != null) {
            gameEventListener.gameFinished(this, gameResult);
        }
    }

    private void invokeMoveRequestedEvent() {
        if (gameEventListener != null) {
            gameEventListener.moveRequested(this, getBoard().getCurrentPlayerId(), getBoard());
        }
    }

    /**
     *
     */
    private static class DefaultGameContext implements GameContext {
        private final EventBus eventBus;
        private final int timeout;
        private final Board board;
        private final List<Board> boardHistory;
        private final int playerId;

        DefaultGameContext(EventBus eventBus, int timeout, Board board, List<Board> boardHistory, int playerId) {
            this.eventBus = eventBus;
            this.timeout = timeout;
            this.board = board;
            this.boardHistory = List.copyOf(boardHistory);
            this.playerId = playerId;
        }

        @Override
        public int getTimeout() {
            return timeout;
        }

        @Override
        public Board getBoard() {
            return board;
        }

        @Override
        public List<Board> getBoardHistory() {
            return boardHistory;
        }

        @Override
        public void makeMove(int column) {
            eventBus.post(new MoveRequestedEvent(column, playerId));
        }
    }

    /**
     *
     */
    private final static class GameStartRequestedEvent {
    }

    /**
     *
     */
    private final static class GameStopRequestedEvent {
    }

    /**
     *
     */
    @Value
    private final static class MoveRequestedEvent {
        private final int column;
        private final int playerId;
    }
}
