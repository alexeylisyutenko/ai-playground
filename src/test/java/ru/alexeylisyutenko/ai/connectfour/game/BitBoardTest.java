package ru.alexeylisyutenko.ai.connectfour.game;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;
import ru.alexeylisyutenko.ai.connectfour.main.console.visualizer.ConsoleBoardVisualizer;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.assertBoardArrayEquals;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.constructBoardArray;

class BitBoardTest {

    private static ConsoleBoardVisualizer visualizer = new ConsoleBoardVisualizer();

    @Test
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

}