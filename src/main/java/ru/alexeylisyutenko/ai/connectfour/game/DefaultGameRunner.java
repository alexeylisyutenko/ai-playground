package ru.alexeylisyutenko.ai.connectfour.game;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.*;
import lombok.Value;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@SuppressWarnings("UnstableApiUsage")
public class DefaultGameRunner implements GameRunner {
    private static final int DEFAULT_TIMEOUT = 10;

    private final ExecutorService eventExecutorService;
    private final EventBus eventBus;
    private final int timeout;
    private final Player player1;
    private final Player player2;
    private final Board initialBoard;
    private final GameEventListener gameEventListener;
    private final MutableState mutableState;

    public DefaultGameRunner(Player player1, Player player2, int timeout, Board initialBoard, GameEventListener gameEventListener) {
        this.eventExecutorService = Executors.newSingleThreadExecutor();
        this.eventBus = new AsyncEventBus(eventExecutorService);
        this.player1 = player1;
        this.player2 = player2;
        this.timeout = timeout;
        this.initialBoard = initialBoard;
        this.gameEventListener = gameEventListener;
        this.mutableState = new MutableState();
        eventBus.register(this);
    }

    public DefaultGameRunner(Player player1, Player player2, GameEventListener gameEventListener) {
        this(player1, player2, DEFAULT_TIMEOUT, new BitBoard(), gameEventListener);
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
    public GameState getGameState() {
        return mutableState.getGameState();
    }

    @Override
    public List<Board> getBoardHistory() {
        return ImmutableList.copyOf(mutableState.getBoardHistory());
    }

    @Subscribe
    private void gameStartRequestedEvent(GameStartRequestedEvent event) {
        try {
            if (getGameState() != GameState.STOPPED) {
                return;
            }

            mutableState.getBoardHistory().clear();
            mutableState.getBoardHistory().add(initialBoard);
            mutableState.setBoard(initialBoard);
            notifyPlayersGameStarted();
            invokeGameStartedEvent();
            processGameStateTransition();
        } finally {
            event.getFuture().finish();
        }
    }

    @Subscribe
    private void gameStopRequestedEvent(GameStopRequestedEvent event) {
        try {
            if (getGameState() == GameState.STOPPED) {
                return;
            }
            finishGame(GameResult.stoppedGame());
        } finally {
            event.getFuture().finish();
        }
    }

    @Subscribe
    private void moveRequestedEvent(MoveRequestedEvent event) {
        if (getGameState() == GameState.STOPPED) {
            return;
        }

        Board nextBoard;
        try {
            nextBoard = mutableState.getBoard().makeMove(event.getColumn());
        } catch (InvalidMoveException e) {
            invokeIllegalMoveAttemptedEvent(e.getColumn());
            requestNextPlayerMove();
            return;
        }
        mutableState.setBoard(nextBoard);
        mutableState.getBoardHistory().add(nextBoard);
        invokeMoveMadeEvent(event.getColumn());
        processGameStateTransition();
    }

    @Override
    public Future<Void> startGame() {
        GameRunnerFuture future = new GameRunnerFuture();
        eventBus.post(new GameStartRequestedEvent(future));
        return future;
    }

    @Override
    public Future<Void> stopGame() {
        GameRunnerFuture future = new GameRunnerFuture();
        eventBus.post(new GameStopRequestedEvent(future));
        return future;
    }

    @Override
    public void shutdown() {
        eventExecutorService.shutdown();
    }

    @Override
    public void awaitGameStop() throws InterruptedException {
        mutableState.awaitGameStop();
    }

    private void processGameStateTransition() {
        int winnerId = mutableState.getBoard().getWinnerId();
        if (winnerId != 0) {
            finishGame(GameResult.normalVictory(winnerId, getLoserId(winnerId)));
        } else if (mutableState.getBoard().isTie()) {
            finishGame(GameResult.tie());
        } else {
            requestNextPlayerMove();
        }
    }

    private void finishGame(GameResult gameResult) {
        notifyPlayersGameFinished(gameResult);
        invokeGameFinishedEvent(gameResult);
        mutableState.setGameState(GameState.STOPPED);
    }

    private void requestNextPlayerMove() {
        invokeMoveRequestedEvent();
        if (mutableState.getBoard().getCurrentPlayerId() == 1) {
            player1.requestMove(createGameContext(1));
            mutableState.setGameState(GameState.WAITING_FOR_PLAYER1_MOVE);
        } else {
            player2.requestMove(createGameContext(2));
            mutableState.setGameState(GameState.WAITING_FOR_PLAYER2_MOVE);
        }
    }

    private DefaultGameContext createGameContext(int playerId) {
        return new DefaultGameContext(eventBus, timeout, mutableState.getBoard(), mutableState.getBoardHistory(), playerId);
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
            gameEventListener.gameStarted(this, mutableState.getBoard());
        }
    }

    private void invokeMoveMadeEvent(int column) {
        if (gameEventListener != null) {
            gameEventListener.moveMade(this, mutableState.getBoard().getOtherPlayerId(), column, mutableState.getBoard());
        }
    }

    private void invokeIllegalMoveAttemptedEvent(int column) {
        if (gameEventListener != null) {
            gameEventListener.illegalMoveAttempted(this, mutableState.getBoard().getCurrentPlayerId(), column, mutableState.getBoard());
        }
    }

    private void invokeGameFinishedEvent(GameResult gameResult) {
        if (gameEventListener != null) {
            gameEventListener.gameFinished(this, gameResult, mutableState.getBoard());
        }
    }

    private void invokeMoveRequestedEvent() {
        if (gameEventListener != null) {
            gameEventListener.moveRequested(this, mutableState.getBoard().getCurrentPlayerId(), mutableState.getBoard());
        }
    }

    /**
     * Class which contains mutable state of the {@link DefaultGameRunner} and contains all required synchronization.
     */
    private static class MutableState {
        private final List<Board> boardHistory;
        private GameState gameState;
        private Board board;

        public MutableState() {
            this.boardHistory = Collections.synchronizedList(new LinkedList<>());
            this.gameState = GameState.STOPPED;
            this.board = null;
        }

        public List<Board> getBoardHistory() {
            return boardHistory;
        }

        public synchronized GameState getGameState() {
            return gameState;
        }

        public synchronized void setGameState(GameState gameState) {
            this.gameState = gameState;
            notifyAll();
        }

        public synchronized Board getBoard() {
            return board;
        }

        public synchronized void setBoard(Board board) {
            this.board = board;
        }

        public synchronized void awaitGameStop() throws InterruptedException {
            while (gameState != GameState.STOPPED) {
                wait();
            }
        }
    }

    /**
     * Context passed to {@link Player} objects when move is requested.
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
     * Event that occurs when user request game start.
     */
    @Value
    private static class GameStartRequestedEvent {
        GameRunnerFuture future;
    }

    /**
     * Event that occurs when user request game stop.
     */
    @Value
    private static class GameStopRequestedEvent {
        GameRunnerFuture future;
    }

    /**
     * Event that occurs when a player {@link Player} makes a move.
     */
    @Value
    private static class MoveRequestedEvent {
        int column;
        int playerId;
    }

    /**
     * A {@link Future} which doesn't have any result and which is used in {@link GameRunner#startGame()} and
     * {@link GameRunner#stopGame()} methods to report completion of start and stop operations.
     */
    private static class GameRunnerFuture implements Future<Void> {
        private final CountDownLatch countDownLatch = new CountDownLatch(1);

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return countDownLatch.getCount() == 0;
        }

        @Override
        public Void get() throws InterruptedException {
            countDownLatch.await();
            return null;
        }

        @Override
        public Void get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
            if (countDownLatch.await(timeout, unit)) {
                return null;
            } else {
                throw new TimeoutException();
            }
        }

        public void finish() {
            countDownLatch.countDown();
        }
    }
}
