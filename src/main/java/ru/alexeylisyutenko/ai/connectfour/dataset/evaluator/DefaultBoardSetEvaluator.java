package ru.alexeylisyutenko.ai.connectfour.dataset.evaluator;

import lombok.AllArgsConstructor;
import ru.alexeylisyutenko.ai.connectfour.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.evaluation.InternalEvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.search.iterativedeepening.IterativeDeepeningSearchFunction;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DefaultBoardSetEvaluator implements BoardSetEvaluator {
    public static final int CONSOLE_REFRESH_PERIOD = 1000;

    private final int iterativeDeepeningTimeout;

    @Override
    public List<BoardWithMove> evaluate(Set<Board> boards, String boardSetName) {
        Objects.requireNonNull(boards, "boards cannot be null");
        Objects.requireNonNull(boardSetName, "boardSetName cannot be null");

        List<BoardWithMove> boardWithMoves;
        AtomicInteger progressCounter = new AtomicInteger();
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        try {
            schedulePeriodicProgressConsolePrinting(boardSetName, boards.size(), progressCounter, scheduledExecutorService);
            boardWithMoves = evaluateBoards(boards, progressCounter);
        } finally {
            shutdownExecutorServiceAndAwaitTermination(scheduledExecutorService);
        }
        return boardWithMoves;
    }

    private void schedulePeriodicProgressConsolePrinting(String boardSetName,
                                                         int boardsSize,
                                                         AtomicInteger progressCounter,
                                                         ScheduledExecutorService scheduledExecutorService) {
        scheduledExecutorService.scheduleAtFixedRate(
                () -> System.out.printf("%s evaluation: %d/%d\r", boardSetName, progressCounter.get(), boardsSize), 0, CONSOLE_REFRESH_PERIOD, TimeUnit.MILLISECONDS);
    }

    private void shutdownExecutorServiceAndAwaitTermination(ScheduledExecutorService scheduledExecutorService) {
        scheduledExecutorService.shutdownNow();
        try {
            scheduledExecutorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private List<BoardWithMove> evaluateBoards(Set<Board> boards, AtomicInteger progressCounter) {
        EvaluationFunction evaluationFunction = new InternalEvaluationFunction();
        return boards.stream().parallel().map(board -> {
            ThreadLocal<SearchFunction> threadLocal = ThreadLocal.withInitial(
                    () -> new IterativeDeepeningSearchFunction(iterativeDeepeningTimeout, false));
            Move move = threadLocal.get().search(board, 1, evaluationFunction);
            progressCounter.incrementAndGet();
            return new BoardWithMove(board, move.getColumn());
        }).collect(Collectors.toList());
    }
}
