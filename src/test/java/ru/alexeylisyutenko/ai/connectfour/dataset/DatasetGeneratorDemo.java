package ru.alexeylisyutenko.ai.connectfour.dataset;

import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.DefaultGameRunner;
import ru.alexeylisyutenko.ai.connectfour.game.GameRunner;
import ru.alexeylisyutenko.ai.connectfour.game.Player;
import ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.*;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta.AlphaBetaSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal.TranspositionTableAlphaBetaTimeoutBasedSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.IterativeDeepeningSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.plain.MultithreadedMinimaxSearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable.CacheBasedBestMoveTable;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.transpositiontable.CacheBasedTranspositionTable;
import ru.alexeylisyutenko.ai.connectfour.player.MinimaxBasedPlayer;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class DatasetGeneratorDemo {

    private static final ConsoleBoardVisualizer consoleBoardVisualizer = new ConsoleBoardVisualizer();

    @Test
    public void generateDataset() {
        // TODO: Construct a board stream.

        // Generate random boards.
//        Set<Board> boards = generateBoards();
        System.out.println("Generating boards...");
        Set<Board> boards = generateGenuineBoards(50000);

        // Evaluate each board.
        System.out.println("Evaluating boards...");
//        List<BoardWithMove> evaluatedBoards = evaluateBoards(boards);
        List<BoardWithMove> evaluatedBoards = List.of();

        System.out.println("Boards statistics:");
        IntSummaryStatistics statistics = boards.stream().mapToInt(Board::getNumberOfTokensOnBoard).summaryStatistics();
        System.out.println(statistics);

        Map<Integer, Long> tokensStatistics = boards.stream()
                .collect(Collectors.groupingBy(
                        board -> board.getNumberOfTokensOnBoard() / 5,
                        Collectors.counting()));
        System.out.println("Number of tokens distribution = " + tokensStatistics);

        Map<Boolean, Long> canBeFinishedInOneStep = boards.stream()
                .collect(Collectors.groupingBy(this::canBeFinishedInOneMove, Collectors.counting()));
        System.out.println("Can be finished in one step distribution = " + canBeFinishedInOneStep);


        Map<Integer, Long> columnStats = evaluatedBoards.stream().map(BoardWithMove::getMove).map(Move::getColumn)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println();
        System.out.println("Column histogram:");
        System.out.println(columnStats);
    }

    private Set<Board> generateBoards() {
        HashSet<Board> boards = new HashSet<>();
        while (boards.size() < 7000) {
            Board board = BoardHelpers.constructRandomNonFinishedBoard(0, BOARD_HEIGHT * BOARD_WIDTH);
            boards.add(board);
        }
        return boards;
    }

    private List<BoardWithMove> evaluateBoards(Set<Board> boards) {
        CachingEvaluationFunction evaluationFunction = new CachingEvaluationFunction(new InternalEvaluationFunction());
        AtomicInteger counter = new AtomicInteger();
        return boards.stream().parallel().map(board -> {
            ThreadLocal<SearchFunction> threadLocal =
                    ThreadLocal.withInitial(() -> new IterativeDeepeningSearchFunction(100, false));
            Move move = threadLocal.get().search(board, 1, evaluationFunction);

            String message = String.format("%s : move = %s, tokens = %d, counter = %d",
                    Thread.currentThread().getName(), move.toString(), board.getNumberOfTokensOnBoard(), counter.incrementAndGet());
            System.out.println(message);

            return new BoardWithMove(board, move);
        }).collect(Collectors.toList());
    }

    private boolean canBeFinishedInOneMove(Board board) {
        List<Pair<Integer, Board>> allNextMoves = MinimaxHelper.getAllNextMoves(board);
        Collections.shuffle(allNextMoves);
        for (Pair<Integer, Board> nextMove : allNextMoves) {
            Board nextMoveBoard = nextMove.getRight();
            if (nextMoveBoard.isGameOver()) {
                return true;
            }
        }
        return false;
    }

    private Set<Board> generateGenuineBoards(int count) {
        Player player1 = new MinimaxBasedPlayer(new AlphaBetaSearchFunction(), new RandomizedEvaluationFunction(), 3, false);
        Player player2 = new MinimaxBasedPlayer(new AlphaBetaSearchFunction(), new RandomizedEvaluationFunction(), 3, false);
        GameRunner gameRunner = new DefaultGameRunner(player1, player2, null);

        HashSet<Board> boards = new HashSet<>();
        while (boards.size() < count) {
            try {
                gameRunner.startGame().get();
                gameRunner.awaitGameStop();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            boards.addAll(gameRunner.getBoardHistory());
        }
        return boards;
    }

    @Value
    private static class BoardWithMove {
        Board board;
        Move move;
    }

}
