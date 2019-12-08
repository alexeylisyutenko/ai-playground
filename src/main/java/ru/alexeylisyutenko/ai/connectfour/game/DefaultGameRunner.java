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
    public GameState getGameState() {
        return mutableState.getGameState();
    }

    @Override
    public List<Board> getBoardHistory() {
        return ImmutableList.copyOf(mutableState.getBoardHistory());
    }

    @Subscribe
    private void gameStartRequestedEvent(GameStartRequestedEvent event) {
        if (getGameState() != GameState.STOPPED) {
            return;
        }

        mutableState.getBoardHistory().clear();
        mutableState.getBoardHistory().add(initialBoard);
        mutableState.setBoard(initialBoard);
        notifyPlayersGameStarted();
        invokeGameStartedEvent();
        processGameStateTransition();
    }

    @Subscribe
    private void gameStopRequestedEvent(GameStopRequestedEvent event) {
        if (getGameState() == GameState.STOPPED) {
            return;
        }
        finishGame(GameResult.stoppedGame());
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

    @Override
    public void awaitGameStart() throws InterruptedException {
        mutableState.awaitGameStart();
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
            gameEventListener.gameFinished(this, gameResult);
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

        public synchronized void awaitGameStart() throws InterruptedException {
            while (gameState == GameState.STOPPED) {
                wait();
            }
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
    private final static class GameStartRequestedEvent {
    }

    /**
     * Event that occurs when user request game stop.
     */
    private final static class GameStopRequestedEvent {
    }

    /**
     * Event that occurs when a player {@link Player} makes a move.
     */
    @Value
    private final static class MoveRequestedEvent {
        private final int column;
        private final int playerId;
    }
}
