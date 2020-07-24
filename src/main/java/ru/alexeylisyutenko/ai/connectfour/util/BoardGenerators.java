package ru.alexeylisyutenko.ai.connectfour.util;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.*;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.BasicEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.CachingEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.EvenBetterEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MultithreadedMinimaxSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.player.MinimaxBasedPlayer;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public final class BoardGenerators {
    private BoardGenerators() {
    }

    /**
     * Constructs a random finished board.
     *
     * @return random finished board
     */
    public static Board constructRandomFinishedBoard() {
        Board board;
        boolean success;
        do {
            board = new DefaultBoard();
            while (!board.isGameOver()) {
                board = findRandomMove(board);
            }
            success = !board.isTie();
        } while (!success);
        return board;
    }

    /**
     *
     * @return
     */
    public static Board constructRandomTieBoard() {
        Board board;
        boolean success;
        do {
            board = new DefaultBoard();
            while (!board.isGameOver()) {
                board = findRandomMove(board);
            }
            success = board.isTie();
        } while (!success);
        return board;
    }

    /**
     * @return
     */
    public static Board constructRandomNonFinishedBoard() {
        return constructRandomNonFinishedBoard(0, 30);
    }

    /**
     * @param lowerBound
     * @param upperBound
     * @return
     */
    public static Board constructRandomNonFinishedBoard(int lowerBound, int upperBound) {
        int moves = RandomUtils.nextInt(lowerBound, upperBound);

        Board board;
        boolean success;
        do {
            board = new BitBoard();
            success = true;
            for (int i = 0; i < moves; i++) {
                Optional<Board> boardOptional = findRandomNonFinishingMove(board);
                if (boardOptional.isPresent()) {
                    board = boardOptional.get();
                } else {
                    success = false;
                    break;
                }
            }
        } while (!success);

        return board;
    }

    private static Board findRandomMove(Board board) {
        List<Pair<Integer, Board>> allNextMoves = MinimaxHelper.getAllNextMoves(board);
        if (allNextMoves.isEmpty()) {
            throw new IllegalArgumentException("It's impossible to generate a move since there are no available moves for the board");
        }
        Collections.shuffle(allNextMoves);
        return board.makeMove(allNextMoves.get(0).getLeft());
    }

    private static Optional<Board> findRandomNonFinishingMove(Board board) {
        List<Pair<Integer, Board>> allNextMoves = MinimaxHelper.getAllNextMoves(board);
        Collections.shuffle(allNextMoves);

        for (Pair<Integer, Board> nextMove : allNextMoves) {
            Board nextMoveBoard = nextMove.getRight();
            if (!nextMoveBoard.isGameOver()) {
                return Optional.of(nextMoveBoard);
            }
        }
        return Optional.empty();
    }

    /**
     * @param count
     * @return
     */
    public static List<Board> generateRandomNonFinishedBoards(int count) {
        HashSet<Board> boards = new HashSet<>();
        while (boards.size() < count) {
            Board board = constructRandomNonFinishedBoard(0, BOARD_HEIGHT * BOARD_WIDTH);
            boards.add(board);
        }
        return new ArrayList<>(boards);
    }

    /**
     * @return
     */
    public static List<Board> generateGenuineGameBoardSequence() {
        Player player1 = new MinimaxBasedPlayer(new MultithreadedMinimaxSearchFunction(), new CachingEvaluationFunction(new EvenBetterEvaluationFunction()), 3, false);
        Player player2 = new MinimaxBasedPlayer(new MultithreadedMinimaxSearchFunction(), new CachingEvaluationFunction(new BasicEvaluationFunction()), 3, false);

        GameRunner gameRunner = new DefaultGameRunner(player1, player2, null);
        try {
            gameRunner.startGame().get();
            gameRunner.awaitGameStop();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        List<Board> boards = new ArrayList<>(gameRunner.getBoardHistory());
        boards.remove(boards.size() - 1);
        return boards;
    }

    /**
     * @return
     */
    public static Stream<Board> distinctBoards() {
        return Stream.generate(new BoardSupplier())
                .distinct()
                .filter(board -> !board.isGameOver());
    }

    /**
     *
     */
    private static class BoardSupplier implements Supplier<Board> {
        private Iterator<Board> boardIterator = Collections.emptyIterator();

        @Override
        public Board get() {
            if (!boardIterator.hasNext()) {
                boardIterator = generateNextPortion();
            }
            return boardIterator.next();
        }

        private Iterator<Board> generateNextPortion() {
            List<Board> realGameBoards = generateGenuineGameBoardSequence();
            List<Board> randomBoards = generateRandomNonFinishedBoards(realGameBoards.size() / 4);
            ArrayList<Board> boards = new ArrayList<>();
            boards.addAll(realGameBoards);
            boards.addAll(randomBoards);
            Collections.shuffle(boards);
            return boards.iterator();
        }
    }

}
