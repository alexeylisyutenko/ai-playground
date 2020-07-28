package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.helper;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BoardSetHelpers {
    public static void printMovesDistribution(List<BoardWithMove> evaluatedBoards) {
        Map<Integer, Long> columnStats = evaluatedBoards.stream().map(BoardWithMove::getMove)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println("Move distribution = " + columnStats);
    }

    public static void printSingleBoardSetInformation(Set<Board> boards) {
        IntSummaryStatistics statistics = boards.stream().mapToInt(Board::getNumberOfTokensOnBoard).summaryStatistics();
        System.out.println("Boards statistics = " + statistics);

        Map<Integer, Long> tokensStatistics = boards.stream().collect(Collectors.groupingBy(board -> board.getNumberOfTokensOnBoard() / 5, Collectors.counting()));
        System.out.println("Number of tokens distribution = " + tokensStatistics);

        Map<Boolean, Long> canBeFinishedInOneStep = boards.stream().collect(Collectors.groupingBy(BoardSetHelpers::canBeFinishedInOneMove, Collectors.counting()));
        System.out.println("Can be finished in one step distribution = " + canBeFinishedInOneStep);
    }

    private static boolean canBeFinishedInOneMove(Board board) {
        List<Pair<Integer, Board>> allNextMoves = MinimaxHelper.getAllNextMoves(board);
        for (Pair<Integer, Board> nextMove : allNextMoves) {
            Board nextMoveBoard = nextMove.getRight();
            if (nextMoveBoard.isGameOver()) {
                return true;
            }
        }
        return false;
    }
}
