package ru.alexeylisyutenko.ai.connectfour.player.impl;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.Board;
import ru.alexeylisyutenko.ai.connectfour.Cell;
import ru.alexeylisyutenko.ai.connectfour.player.GameResult;
import ru.alexeylisyutenko.ai.connectfour.player.Player;
import ru.alexeylisyutenko.ai.connectfour.runner.GameContext;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.alexeylisyutenko.ai.connectfour.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.Constants.BOARD_WIDTH;

public class RandomPlayer implements Player {

    private int playerId;

    @Override
    public void setId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void requestMove(GameContext gameContext) {
        Optional<Integer> winningMove = calculateWinningMove(gameContext);
        if (winningMove.isPresent()) {
            gameContext.makeMove(winningMove.get());
            return;
        }

        Optional<Integer> blockingMove = calculateBlockingMove(gameContext);
        if (blockingMove.isPresent()) {
            gameContext.makeMove(blockingMove.get());
            return;
        }

        Optional<Integer> increasingMove = calculateIncreasingChainMove(gameContext);
        if (increasingMove.isPresent()) {
            gameContext.makeMove(increasingMove.get());
            return;
        }

        makeRandomMove(gameContext);
    }

    private Optional<Integer> calculateIncreasingChainMove(GameContext gameContext) {
        Board board = gameContext.getBoard();

        Comparator<List<Cell>> listSizeComparator = Comparator.comparing(List::size);
        Comparator<List<Cell>> listSizeComparatorReversed = listSizeComparator.reversed();
        List<List<Cell>> chains = board.getChainCells(playerId).stream()
                .filter(cells -> cells.size() > 1)
                .sorted(listSizeComparatorReversed)
                .collect(Collectors.toList());

        for (List<Cell> chain : chains) {
            Optional<Integer> move = calculateMoveForChainEnds(board, chain);
            if (move.isPresent()) {
                return move;
            }
        }

        return Optional.empty();
    }

    private Optional<Integer> calculateMoveForChainEnds(Board board, List<Cell> chain) {
        Pair<Cell, Cell> chainEnds = calculateChainEnds(chain);
        Cell lastChainEnd = chainEnds.getLeft();
        Cell firstChainEnd = chainEnds.getRight();

        if (isValidCell(lastChainEnd) && isPossibleToOccupyCell(board, lastChainEnd)) {
            return Optional.of(lastChainEnd.getColumn());
        } else if (isValidCell(firstChainEnd) && isPossibleToOccupyCell(board, firstChainEnd)) {
            return Optional.of(firstChainEnd.getColumn());
        }
        return Optional.empty();
    }

    private Optional<Integer> calculateBlockingMove(GameContext gameContext) {
        Board board = gameContext.getBoard();

        // Not allow other player to finish a chain.
        int otherPlayerId = board.getOtherPlayerId();
        Set<List<Cell>> chains = board.getChainCells(otherPlayerId).stream()
                .filter(cells -> cells.size() > 2).collect(Collectors.toSet());

        for (List<Cell> chain : chains) {
            if (!isDangerousChainAlreadyBlocked(board, chain)) {
                Optional<Integer> move = calculateMoveForChainEnds(board, chain);
                if (move.isPresent()) {
                    return move;
                }
            }
        }

        return Optional.empty();
    }

    private Optional<Integer> calculateWinningMove(GameContext gameContext) {
        Board board = gameContext.getBoard();

        Set<List<Cell>> winningChains = board.getChainCells(playerId).stream()
                .filter(cells -> cells.size() == 3).collect(Collectors.toSet());
        for (List<Cell> chain : winningChains) {
            Optional<Integer> move = calculateMoveForChainEnds(board, chain);
            if (move.isPresent()) {
                return move;
            }
        }
        return Optional.empty();
    }

    private boolean isDangerousChainAlreadyBlocked(Board board, List<Cell> dangerousChain) {
        Pair<Cell, Cell> chainEnds = calculateChainEnds(dangerousChain);
        Cell firstBlockingCell = chainEnds.getLeft();
        Cell secondBlockingCell = chainEnds.getRight();

        boolean firstIsBlocked;
        if (isValidCell(firstBlockingCell)) {
            firstIsBlocked = board.getCellPlayerId(firstBlockingCell.getRow(), firstBlockingCell.getColumn()) == playerId;
        } else {
            firstIsBlocked = true;
        }

        boolean secondIsBlocked;
        if (isValidCell(secondBlockingCell)) {
            secondIsBlocked = board.getCellPlayerId(secondBlockingCell.getRow(), secondBlockingCell.getColumn()) == playerId;
        } else {
            secondIsBlocked = true;
        }

        return firstIsBlocked && secondIsBlocked;
    }

    private boolean isPossibleToOccupyCell(Board board, Cell blockingCell) {
        int heightOfColumn = board.getHeightOfColumn(blockingCell.getColumn());
        return heightOfColumn == blockingCell.getRow() || heightOfColumn == 6;
    }

    private boolean isValidCell(Cell cell) {
        return cell.getRow() >= 0 && cell.getRow() < BOARD_HEIGHT &&
                cell.getColumn() >= 0 && cell.getColumn() < BOARD_WIDTH;
    }

    private void makeRandomMove(GameContext gameContext) {
        while (true) {
            int column = RandomUtils.nextInt(0, 7);
            if (gameContext.getBoard().getHeightOfColumn(column) != -1) {
                gameContext.makeMove(column);
                break;
            }
        }
    }

    private Pair<Cell, Cell> calculateChainEnds(List<Cell> chain) {
        Pair<Integer, Integer> direction = calculateChainDirection(chain);

        Cell lastCell = chain.get(chain.size() - 1);
        Cell lastChainEnd = new Cell(lastCell.getRow() + direction.getLeft(), lastCell.getColumn() + direction.getRight());

        Cell firstCell = chain.get(0);
        Cell firstChainEnd = new Cell(firstCell.getRow() - direction.getLeft(), firstCell.getColumn() - direction.getRight());

        return Pair.of(lastChainEnd, firstChainEnd);
    }

    private Pair<Integer, Integer> calculateChainDirection(List<Cell> chain) {
        Cell cell1 = chain.get(0);
        Cell cell2 = chain.get(1);
        return Pair.of(cell2.getRow() - cell1.getRow(), cell2.getColumn() - cell1.getColumn());
    }

    @Override
    public void gameFinished(GameResult gameResult) {
        // Do nothing.
    }
}
