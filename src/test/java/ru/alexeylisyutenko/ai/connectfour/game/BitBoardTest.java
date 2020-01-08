package ru.alexeylisyutenko.ai.connectfour.game;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.assertBoardArrayEquals;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.constructBoardArray;
import static ru.alexeylisyutenko.ai.connectfour.util.BitBoardHelper.bitmapToString;

class BitBoardTest {

    private static ConsoleBoardVisualizer visualizer = new ConsoleBoardVisualizer();

    @Test
    @Disabled
    void someOperationsDemo() {
        Board board = new BitBoard();

        visualizer.visualize(board);
        System.out.println("Current player: " + board.getCurrentPlayerId());
        System.out.println();

        board = board.makeMove(3);
        visualizer.visualize(board);
        System.out.println("Current player: " + board.getCurrentPlayerId());
        System.out.println();

        BitBoard bitBoard = new BitBoard(
                new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 2, 1, 2, 0, 0},
                        {0, 0, 1, 2, 1, 0, 0},
                        {2, 1, 2, 2, 2, 1, 0}
                }, 1);
        visualizer.visualize(bitBoard);
        System.out.println("Current player: " + bitBoard.getCurrentPlayerId());
        System.out.println();

        bitBoard.printInternals();

    }

    @Test
    void getCurrentPlayerIdMustWorkProperly() {
        Board board1 = new BitBoard(0, 0, 1, 0);
        assertEquals(1, board1.getCurrentPlayerId());

        Board board2 = new BitBoard(0, 0, 2, 0);
        assertEquals(2, board2.getCurrentPlayerId());
    }

    @Test
    void getOtherPlayerIdMustWorkProperly() {
        Board board1 = new BitBoard(0, 0, 1, 0);
        assertEquals(2, board1.getOtherPlayerId());

        Board board2 = new BitBoard(0, 0, 2, 0);
        assertEquals(1, board2.getOtherPlayerId());
    }

    @Test
    void getCellPlayerIdMustWorkProperly() {
        Board board = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 0},
                {1, 0, 1, 0, 2, 0, 0}
        }, 1);
        assertEquals(0, board.getCellPlayerId(4, 0));
        assertEquals(1, board.getCellPlayerId(5, 0));
        assertEquals(2, board.getCellPlayerId(5, 4));
    }

    @Test
    void getTopEltInColumnMustWorkProperly() {
        Board board = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 1, 2},
                {0, 0, 0, 0, 0, 2, 1},
                {0, 0, 0, 0, 2, 1, 2},
                {0, 0, 1, 0, 1, 2, 1},
                {0, 0, 2, 0, 2, 1, 2},
                {0, 1, 1, 0, 2, 2, 1}
        }, 1);
        assertEquals(0, board.getTopEltInColumn(0));
        assertEquals(1, board.getTopEltInColumn(1));
        assertEquals(1, board.getTopEltInColumn(2));
        assertEquals(0, board.getTopEltInColumn(3));
        assertEquals(2, board.getTopEltInColumn(4));
        assertEquals(1, board.getTopEltInColumn(5));
        assertEquals(2, board.getTopEltInColumn(6));

        board = new BitBoard(new int[][]{
                {1, 0, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 2},
                {2, 0, 1, 0, 0, 2, 1},
                {1, 0, 2, 0, 1, 1, 2},
                {2, 1, 1, 0, 2, 2, 1}
        }, 1);
        assertEquals(1, board.getTopEltInColumn(0));
        assertEquals(1, board.getTopEltInColumn(1));
        assertEquals(1, board.getTopEltInColumn(2));
        assertEquals(0, board.getTopEltInColumn(3));
        assertEquals(1, board.getTopEltInColumn(4));
        assertEquals(2, board.getTopEltInColumn(5));
        assertEquals(2, board.getTopEltInColumn(6));
    }

    @Test
    void getHeightOfColumnMustWorkProperly() {
        Board board = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 2},
                {0, 0, 0, 0, 0, 0, 2},
                {0, 0, 0, 0, 2, 0, 2},
                {0, 0, 1, 0, 1, 0, 2},
                {0, 0, 2, 0, 2, 0, 2},
                {0, 1, 1, 0, 2, 0, 2}
        }, 1);
        assertEquals(-1, board.getHeightOfColumn(6));
        assertEquals(6, board.getHeightOfColumn(5));
        assertEquals(1, board.getHeightOfColumn(4));
        assertEquals(2, board.getHeightOfColumn(2));
        assertEquals(4, board.getHeightOfColumn(1));

        board = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 2},
                {0, 0, 0, 0, 0, 1, 2},
                {0, 0, 0, 0, 2, 2, 2},
                {0, 0, 0, 1, 1, 1, 2},
                {0, 0, 2, 2, 2, 2, 2},
                {0, 1, 1, 1, 2, 1, 2}
        }, 1);
        assertEquals(-1, board.getHeightOfColumn(6));
        assertEquals(0, board.getHeightOfColumn(5));
        assertEquals(1, board.getHeightOfColumn(4));
        assertEquals(2, board.getHeightOfColumn(3));
        assertEquals(3, board.getHeightOfColumn(2));
        assertEquals(4, board.getHeightOfColumn(1));
        assertEquals(6, board.getHeightOfColumn(0));
    }

    @Test
    void getLongestChainMustWorkProperly() {
        Board board1 = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 2},
                {0, 0, 1, 0, 1, 2, 1},
                {0, 0, 2, 0, 2, 1, 2},
                {0, 1, 1, 2, 2, 2, 1}
        }, 1);
        assertEquals(4, board1.getLongestChain(2));
        assertEquals(3, board1.getLongestChain(1));

        Board board2 = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        }, 1);
        assertEquals(0, board2.getLongestChain(2));
        assertEquals(0, board2.getLongestChain(1));

        Board board3 = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, 2, 1, 2},
                {1, 1, 1, 1, 2, 1, 2}
        }, 1);
        assertEquals(4, board3.getLongestChain(2));
        assertEquals(4, board3.getLongestChain(1));
    }

    @Test
    void getChainCellsMustWorkProperly() {
        Board board = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 1, 2, 0, 0},
                {0, 0, 1, 2, 1, 0, 0},
                {2, 1, 2, 2, 2, 1, 0}
        }, 1);

        Set<List<Cell>> chainCellsPlayer2 = board.getChainCells(2);
        Set<List<Cell>> expectedSetPlayer2 = Set.of(
                List.of(new Cell(5, 0)),
                List.of(new Cell(5, 2)),
                List.of(new Cell(5, 3)),
                List.of(new Cell(5, 4)),
                List.of(new Cell(4, 3)),
                List.of(new Cell(3, 2)),
                List.of(new Cell(3, 4)),
                List.of(new Cell(5, 4), new Cell(5, 3), new Cell(5, 2)),
                List.of(new Cell(5, 3), new Cell(4, 3)),
                List.of(new Cell(5, 2), new Cell(4, 3), new Cell(3, 4)),
                List.of(new Cell(5, 4), new Cell(4, 3), new Cell(3, 2))
        );
        assertEquals(expectedSetPlayer2, chainCellsPlayer2);

        Set<List<Cell>> chainCellsPlayer1 = board.getChainCells(1);
        Set<List<Cell>> expectedSetPlayer1 = Set.of(
                List.of(new Cell(5, 1)),
                List.of(new Cell(4, 2)),
                List.of(new Cell(3, 3)),
                List.of(new Cell(4, 4)),
                List.of(new Cell(5, 5)),
                List.of(new Cell(5, 1), new Cell(4, 2), new Cell(3, 3)),
                List.of(new Cell(5, 5), new Cell(4, 4), new Cell(3, 3))
        );
        assertEquals(expectedSetPlayer1, chainCellsPlayer1);
    }

    @Test
    void makeMoveMustWorkProperly() {
        Board board = new BitBoard();

        assertEquals(1, board.getCurrentPlayerId());
        assertBoardArrayEquals(board, new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        });

        Board board1 = board.makeMove(3);
        assertEquals(2, board1.getCurrentPlayerId());
        assertBoardArrayEquals(board1, new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0}
        });
        assertBoardArrayEquals(board, new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        });

        Board board2 = board1.makeMove(3).makeMove(3).makeMove(3).makeMove(3).makeMove(3);
        assertEquals(1, board2.getCurrentPlayerId());
        assertBoardArrayEquals(board2, new int[][]{
                {0, 0, 0, 2, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0}
        });

        InvalidMoveException exception = assertThrows(InvalidMoveException.class, () -> board2.makeMove(3));
        assertEquals(3, exception.getColumn());
        assertEquals(board2, exception.getBoard());
    }

    @Test
    void getNumberOfTokensOnBoardMustWorkProperly() {
        Board board1 = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        }, 1);
        assertEquals(0, board1.getNumberOfTokensOnBoard());

        Board board2 = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 2},
                {0, 0, 1, 0, 1, 2, 1},
                {0, 0, 2, 0, 2, 1, 2},
                {0, 1, 1, 2, 2, 2, 1}
        }, 1);
        assertEquals(16, board2.getNumberOfTokensOnBoard());
    }

    @Test
    void getWinnerIdMustWorkProperly() {
        Board board1 = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 2},
                {0, 0, 1, 0, 1, 2, 1},
                {0, 0, 2, 0, 2, 1, 2},
                {0, 1, 1, 1, 2, 2, 1}
        }, 1);
        assertEquals(0, board1.getWinnerId());

        Board board2 = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0},
                {0, 0, 0, 1, 2, 0, 2},
                {0, 0, 1, 2, 1, 2, 1},
                {0, 0, 2, 1, 2, 1, 2},
                {0, 1, 1, 1, 2, 2, 2}
        }, 1);
        assertEquals(2, board2.getWinnerId());

        Board board3 = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 2, 0, 2},
                {0, 1, 1, 0, 1, 2, 1},
                {0, 1, 2, 0, 2, 1, 2},
                {0, 1, 1, 1, 2, 2, 1}
        }, 1);
        assertEquals(1, board3.getWinnerId());
    }

    @Test
    void isTieMustWorkProperly() {
        Board board1 = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 2},
                {0, 0, 1, 0, 1, 2, 1},
                {0, 0, 2, 0, 2, 1, 2},
                {0, 1, 1, 1, 2, 2, 1}
        }, 1);
        assertFalse(board1.isTie());

        Board board2 = new BitBoard(new int[][]{
                {1, 1, 2, 2, 2, 1, 2},
                {2, 2, 1, 1, 2, 1, 1},
                {1, 1, 1, 2, 1, 2, 1},
                {1, 1, 2, 2, 1, 1, 1},
                {2, 1, 2, 1, 2, 1, 2},
                {1, 2, 1, 2, 1, 2, 1}
        }, 1);
        assertTrue(board2.isTie());
    }

    @Test
    void isGameOverMustWorkProperly() {
        Board board1 = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 2},
                {0, 0, 1, 0, 1, 2, 1},
                {0, 0, 2, 0, 2, 1, 2},
                {0, 1, 1, 1, 2, 2, 1}
        }, 1);
        assertFalse(board1.isGameOver());

        Board board2 = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0},
                {0, 0, 0, 1, 2, 0, 2},
                {0, 0, 1, 2, 1, 2, 1},
                {0, 0, 2, 1, 2, 1, 2},
                {0, 1, 1, 1, 2, 2, 1}
        }, 1);
        assertTrue(board2.isGameOver());

        Board board3 = new BitBoard(new int[][]{
                {1, 1, 2, 2, 2, 1, 2},
                {2, 2, 1, 1, 2, 1, 1},
                {1, 1, 1, 2, 1, 2, 1},
                {1, 1, 2, 2, 1, 1, 1},
                {2, 1, 2, 1, 2, 1, 2},
                {1, 2, 1, 2, 1, 2, 1}
        }, 1);
        assertTrue(board3.isGameOver());
    }

    @Test
    void getIdMustWorkProperly() {
        Board board1 = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 2},
                {0, 0, 1, 0, 1, 2, 1},
                {0, 0, 2, 0, 2, 1, 2},
                {0, 1, 1, 1, 2, 2, 1}
        }, 1);
        assertEquals(92707949330817L, board1.getId());

        Board board2 = new BitBoard(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        }, 1);
        assertEquals(4432676798593L, board2.getId());

        Board board3 = new BitBoard(new int[][]{
                {1, 1, 2, 2, 2, 1, 2},
                {2, 2, 1, 1, 2, 1, 1},
                {1, 1, 1, 2, 1, 2, 1},
                {1, 1, 2, 2, 1, 1, 1},
                {2, 1, 2, 1, 2, 1, 2},
                {1, 2, 1, 2, 1, 2, 1}
        }, 1);
        assertEquals(413093617629037L, board3.getId());
    }

}