package ru.alexeylisyutenko.ai.connectfour.runner;

import ru.alexeylisyutenko.ai.connectfour.Board;
import ru.alexeylisyutenko.ai.connectfour.DefaultBoard;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;
import ru.alexeylisyutenko.ai.connectfour.player.GameResult;
import ru.alexeylisyutenko.ai.connectfour.player.Player;
import ru.alexeylisyutenko.ai.connectfour.visualizer.ConsoleBoardVisualizer;

// TODO: Refactor the code.

public class DefaultGameRunner implements GameRunner {
    private static final int DEFAULT_TIMEOUT = 10;

    private final int timeout;
    private final Player player1;
    private final Player player2;
    private final Board initialBoard;

    private GameState state;
    private Board board;

    public DefaultGameRunner(Player player1, Player player2, int timeout, Board initialBoard) {
        this.player1 = player1;
        this.player2 = player2;
        this.timeout = timeout;
        this.initialBoard = initialBoard;
        this.state = GameState.STOPPED;
    }

    public DefaultGameRunner(Player player1, Player player2) {
        this(player1, player2, DEFAULT_TIMEOUT, new DefaultBoard());
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
        if (state != GameState.STOPPED && state != GameState.FINISHED) {
            throw new IllegalStateException("Game is already started. You should stop current game first.");
        }

        // Initialize ids.
        player1.setId(1);
        player2.setId(2);

        // Initialize board.
        board = initialBoard;

        // TODO: Remove.
        ConsoleBoardVisualizer boardVisualizer = new ConsoleBoardVisualizer();
        boardVisualizer.visualize(board);
        System.out.println();

        // Check if game is already over.
        int winnerId = board.getWinnerId();
        if (winnerId != 0) {
            // We already have a winner on a board.
            if (winnerId == 1) {
                player1.gameFinished(GameResult.PLAYER_WON);
                player2.gameFinished(GameResult.PLAYER_LOST);
            } else {
                player1.gameFinished(GameResult.PLAYER_LOST);
                player2.gameFinished(GameResult.PLAYER_WON);
            }

            state = GameState.FINISHED;

        } else if (board.isTie()) {
            // It's a tie.
            player1.gameFinished(GameResult.TIE);
            player2.gameFinished(GameResult.TIE);

            state = GameState.FINISHED;
        } else {
            if (board.getCurrentPlayerId() == 1) {
                player1.requestMove(new DefaultMoveMaker(), timeout, board);

                state = GameState.WAITING_FOR_PLAYER1_MOVE;
            } else {
                player2.requestMove(new DefaultMoveMaker(), timeout, board);

                state = GameState.WAITING_FOR_PLAYER2_MOVE;
            }
        }
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
            // TODO: Event should be called here.
            System.out.println("Illegal move attempted.  Please try again.");

            // Request for a move again.
            if (board.getCurrentPlayerId() == 1) {
                player1.requestMove(new DefaultMoveMaker(), timeout, board);
                state = GameState.WAITING_FOR_PLAYER1_MOVE;
            } else {
                player2.requestMove(new DefaultMoveMaker(), timeout, board);
                state = GameState.WAITING_FOR_PLAYER2_MOVE;
            }
            return;
        }

        board = newBoard;

        // TODO: Remove this demo console output.
        System.out.println(String.format("Player %d puts a token in column %d", board.getOtherPlayerId(), column));

        // TODO: Here a player made a move. We shuld add event here.
        ConsoleBoardVisualizer boardVisualizer = new ConsoleBoardVisualizer();
        boardVisualizer.visualize(board);
        System.out.println();

        int winnerId = board.getWinnerId();
        if (winnerId != 0) {
            // We already have a winner on a board.
            if (winnerId == 1) {
                player1.gameFinished(GameResult.PLAYER_WON);
                player2.gameFinished(GameResult.PLAYER_LOST);
            } else {
                player1.gameFinished(GameResult.PLAYER_LOST);
                player2.gameFinished(GameResult.PLAYER_WON);
            }

            // TODO: Remove debug output.
            System.out.println(String.format("Win for player %d!", winnerId));

            state = GameState.FINISHED;

        } else if (board.isTie()) {
            // It's a tie.
            player1.gameFinished(GameResult.TIE);
            player2.gameFinished(GameResult.TIE);

            // TODO: Remove debug output.
            System.out.println("It's a tie!  No winner is declared.");

            state = GameState.FINISHED;
        } else {
            if (board.getCurrentPlayerId() == 1) {
                player1.requestMove(new DefaultMoveMaker(), timeout, board);

                state = GameState.WAITING_FOR_PLAYER1_MOVE;
            } else {
                player2.requestMove(new DefaultMoveMaker(), timeout, board);

                state = GameState.WAITING_FOR_PLAYER2_MOVE;
            }
        }
    }

    private class DefaultMoveMaker implements MoveMaker {
        @Override
        public void makeMove(int column) {
            DefaultGameRunner.this.makeMove(column);
        }
    }

}
