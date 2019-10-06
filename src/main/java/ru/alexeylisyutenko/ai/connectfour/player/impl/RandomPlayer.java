package ru.alexeylisyutenko.ai.connectfour.player.impl;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.Board;
import ru.alexeylisyutenko.ai.connectfour.Cell;
import ru.alexeylisyutenko.ai.connectfour.player.GameResult;
import ru.alexeylisyutenko.ai.connectfour.player.Player;
import ru.alexeylisyutenko.ai.connectfour.runner.GameContext;

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
        Optional<Integer> blockingMove = calculateBlockingMove(gameContext);
        if (blockingMove.isPresent()) {
            gameContext.makeMove(blockingMove.get());
            return;
        }

        makeRandomMove(gameContext);
    }

    private Optional<Integer> calculateBlockingMove(GameContext gameContext) {
        Board board = gameContext.getBoard();

        // Not allow other player to finish a chain.
        int otherPlayerId = board.getOtherPlayerId();
        Set<List<Cell>> chains = board.getChainCells(otherPlayerId).stream()
                .filter(cells -> cells.size() > 2).collect(Collectors.toSet());

        for (List<Cell> chain : chains) {
            if (!isDangerousChainAlreadyBlocked(board, chain)) {
                Cell firstBlockingCell = calculateFirstCellToBlockChain(chain);
                Cell secondBlockingCell = calculateSecondCellToBlockChain(chain);

                if (isValidCell(firstBlockingCell) && isPossibleToBlock(board, firstBlockingCell)) {
                    return Optional.of(firstBlockingCell.getColumn());
                } else if (isValidCell(secondBlockingCell) && isPossibleToBlock(board, secondBlockingCell)) {
                    return Optional.of(secondBlockingCell.getColumn());
                }
            }
        }

        return Optional.empty();
    }

    private boolean isDangerousChainAlreadyBlocked(Board board, List<Cell> dangerousChain) {
        Cell firstBlockingCell = calculateFirstCellToBlockChain(dangerousChain);
        Cell secondBlockingCell = calculateSecondCellToBlockChain(dangerousChain);

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

    private boolean isPossibleToBlock(Board board, Cell blockingCell) {
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

    private Cell calculateFirstCellToBlockChain(List<Cell> dangerousChain) {
        Cell cell1 = dangerousChain.get(0);
        Cell cell2 = dangerousChain.get(1);
        Pair<Integer, Integer> direction = Pair.of(cell2.getRow() - cell1.getRow(), cell2.getColumn() - cell1.getColumn());
        Cell lastCell = dangerousChain.get(dangerousChain.size() - 1);
        return new Cell(lastCell.getRow() + direction.getLeft(), lastCell.getColumn() + direction.getRight());
    }

    private Cell calculateSecondCellToBlockChain(List<Cell> dangerousChain) {
        Cell cell1 = dangerousChain.get(0);
        Cell cell2 = dangerousChain.get(1);
        Pair<Integer, Integer> direction = Pair.of(cell2.getRow() - cell1.getRow(), cell2.getColumn() - cell1.getColumn());
        Cell lastCell = dangerousChain.get(0);
        return new Cell(lastCell.getRow() - direction.getLeft(), lastCell.getColumn() - direction.getRight());
    }

    @Override
    public void gameFinished(GameResult gameResult) {
        // Do nothing.
    }
}
