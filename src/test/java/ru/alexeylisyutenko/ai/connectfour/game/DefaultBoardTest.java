package ru.alexeylisyutenko.ai.connectfour.game;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;
import ru.alexeylisyutenko.ai.connectfour.console.visualizer.BoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.console.visualizer.ConsoleBoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.Cell;
import ru.alexeylisyutenko.ai.connectfour.game.DefaultBoard;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.assertBoardArrayEquals;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.constructBoardArray;

class DefaultBoardTest {

    @Test
    void someOperationsDemo() {
        BoardVisualizer visualizer = new ConsoleBoardVisualizer();

        int[] boardArray = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 1, 2, 0, 0},
                {0, 0, 1, 2, 1, 0, 0},
                {2, 1, 2, 2, 2, 1, 0}
        });

        DefaultBoard board = new DefaultBoard(boardArray, 1);
        visualizer.visualize(board);

        Set<List<Cell>> chains = board.getChainCells(1);
        for (List<Cell> cells : chains) {
            System.out.println(cells);
        }
    }

    @Test
    void getCurrentPlayerIdMustWorkProperly() {
        Board board1 = new DefaultBoard(new int[BOARD_HEIGHT * BOARD_WIDTH], 1);
        assertEquals(1, board1.getCurrentPlayerId());

        Board board2 = new DefaultBoard(new int[BOARD_HEIGHT * BOARD_WIDTH], 2);
        assertEquals(2, board2.getCurrentPlayerId());
    }

    @Test
    void getOtherPlayerIdMustWorkProperly() {
        Board board1 = new DefaultBoard(new int[BOARD_HEIGHT * BOARD_WIDTH], 1);
        assertEquals(2, board1.getOtherPlayerId());

        Board board2 = new DefaultBoard(new int[BOARD_HEIGHT * BOARD_WIDTH], 2);
        assertEquals(1, board2.getOtherPlayerId());
    }

    @Test
    void getCellPlayerIdMustWorkProperly() {
        int[] boardArray = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 0},
                {1, 0, 1, 0, 2, 0, 0}
        });
        Board board = new DefaultBoard(boardArray, 1);
        assertEquals(0, board.getCellPlayerId(4, 0));
        assertEquals(1, board.getCellPlayerId(5, 0));
        assertEquals(2, board.getCellPlayerId(5, 4));
    }

    @Test
    void getTopEltInColumnMustWorkProperly() {
        int[] boardArray = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 0},
                {0, 0, 1, 0, 1, 0, 0},
                {0, 0, 2, 0, 2, 0, 0},
                {0, 1, 1, 0, 2, 0, 0}
        });
        Board board = new DefaultBoard(boardArray, 1);
        assertEquals(0, board.getTopEltInColumn(0));
        assertEquals(1, board.getTopEltInColumn(1));
        assertEquals(1, board.getTopEltInColumn(2));
        assertEquals(2, board.getTopEltInColumn(4));
    }

    @Test
    void getHeightOfColumnMustWorkProperly() {
        int[] boardArray = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 2},
                {0, 0, 0, 0, 0, 0, 2},
                {0, 0, 0, 0, 2, 0, 2},
                {0, 0, 1, 0, 1, 0, 2},
                {0, 0, 2, 0, 2, 0, 2},
                {0, 1, 1, 0, 2, 0, 2}
        });
        Board board = new DefaultBoard(boardArray, 1);
        assertEquals(-1, board.getHeightOfColumn(6));
        assertEquals(6, board.getHeightOfColumn(5));
        assertEquals(1, board.getHeightOfColumn(4));
        assertEquals(2, board.getHeightOfColumn(2));
        assertEquals(4, board.getHeightOfColumn(1));
    }

    @Test
    void makeMoveMustWorkProperly() {
        Board board = new DefaultBoard();

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
    void getLongestChainMustWorkProperly() {
        int[] boardArray1 = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 2},
                {0, 0, 1, 0, 1, 2, 1},
                {0, 0, 2, 0, 2, 1, 2},
                {0, 1, 1, 2, 2, 2, 1}
        });
        Board board1 = new DefaultBoard(boardArray1, 1);
        assertEquals(4, board1.getLongestChain(2));
        assertEquals(3, board1.getLongestChain(1));

        int[] boardArray2 = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        });
        Board board2 = new DefaultBoard(boardArray2, 1);
        assertEquals(0, board2.getLongestChain(2));
        assertEquals(0, board2.getLongestChain(1));

        int[] boardArray3 = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, 2, 1, 2},
                {1, 1, 1, 1, 2, 1, 2}
        });
        Board board3 = new DefaultBoard(boardArray3, 1);
        assertEquals(4, board3.getLongestChain(2));
        assertEquals(4, board3.getLongestChain(1));
    }

    @Test
    void getChainCellsMustWorkProperly() {
        int[] boardArray = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 1, 2, 0, 0},
                {0, 0, 1, 2, 1, 0, 0},
                {2, 1, 2, 2, 2, 1, 0}
        });
        Board board = new DefaultBoard(boardArray, 1);

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
    void getNumberOfTokensOnBoardMustWorkProperly() {
        int[] boardArray1 = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        });
        Board board1 = new DefaultBoard(boardArray1, 1);
        assertEquals(0, board1.getNumberOfTokensOnBoard());

        int[] boardArray2 = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 2},
                {0, 0, 1, 0, 1, 2, 1},
                {0, 0, 2, 0, 2, 1, 2},
                {0, 1, 1, 2, 2, 2, 1}
        });
        Board board2 = new DefaultBoard(boardArray2, 1);
        assertEquals(16, board2.getNumberOfTokensOnBoard());
    }

    @Test
    void getWinnerIdMustWorkProperly() {
        int[] boardArray1 = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 2},
                {0, 0, 1, 0, 1, 2, 1},
                {0, 0, 2, 0, 2, 1, 2},
                {0, 1, 1, 1, 2, 2, 1}
        });
        Board board1 = new DefaultBoard(boardArray1, 1);
        assertEquals(0, board1.getWinnerId());

        int[] boardArray2 = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0},
                {0, 0, 0, 1, 2, 0, 2},
                {0, 0, 1, 2, 1, 2, 1},
                {0, 0, 2, 1, 2, 1, 2},
                {0, 1, 1, 1, 2, 2, 1}
        });
        Board board2 = new DefaultBoard(boardArray2, 1);
        assertEquals(2, board2.getWinnerId());

        int[] boardArray3 = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 2, 0, 2},
                {0, 1, 1, 0, 1, 2, 1},
                {0, 1, 2, 0, 2, 1, 2},
                {0, 1, 1, 1, 2, 2, 1}
        });
        Board board3 = new DefaultBoard(boardArray3, 1);
        assertEquals(1, board3.getWinnerId());
    }

    @Test
    void isGameOverMustWorkProperly() {
        int[] boardArray1 = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 2},
                {0, 0, 1, 0, 1, 2, 1},
                {0, 0, 2, 0, 2, 1, 2},
                {0, 1, 1, 1, 2, 2, 1}
        });
        Board board1 = new DefaultBoard(boardArray1, 1);
        assertFalse(board1.isGameOver());

        int[] boardArray2 = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0},
                {0, 0, 0, 1, 2, 0, 2},
                {0, 0, 1, 2, 1, 2, 1},
                {0, 0, 2, 1, 2, 1, 2},
                {0, 1, 1, 1, 2, 2, 1}
        });
        Board board2 = new DefaultBoard(boardArray2, 1);
        assertTrue(board2.isGameOver());

        int[] boardArray3 = constructBoardArray(new int[][]{
                {1, 1, 2, 2, 2, 1, 2},
                {2, 2, 1, 1, 2, 1, 1},
                {1, 1, 1, 2, 1, 2, 1},
                {1, 1, 2, 2, 1, 1, 1},
                {2, 1, 2, 1, 2, 1, 2},
                {1, 2, 1, 2, 1, 2, 1}
        });
        Board board3 = new DefaultBoard(boardArray3, 1);
        assertTrue(board3.isGameOver());
    }

    @Test
    void isTieMustWorkProperly() {
        int[] boardArray1 = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 2},
                {0, 0, 1, 0, 1, 2, 1},
                {0, 0, 2, 0, 2, 1, 2},
                {0, 1, 1, 1, 2, 2, 1}
        });
        Board board1 = new DefaultBoard(boardArray1, 1);
        assertFalse(board1.isTie());

        int[] boardArray2 = constructBoardArray(new int[][]{
                {1, 1, 2, 2, 2, 1, 2},
                {2, 2, 1, 1, 2, 1, 1},
                {1, 1, 1, 2, 1, 2, 1},
                {1, 1, 2, 2, 1, 1, 1},
                {2, 1, 2, 1, 2, 1, 2},
                {1, 2, 1, 2, 1, 2, 1}
        });
        Board board2 = new DefaultBoard(boardArray2, 1);
        assertTrue(board2.isTie());
    }

}