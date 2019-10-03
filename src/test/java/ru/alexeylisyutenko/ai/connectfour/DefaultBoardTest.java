package ru.alexeylisyutenko.ai.connectfour;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.exception.InvalidMoveException;
import ru.alexeylisyutenko.ai.connectfour.visualizer.BoardVisualizer;
import ru.alexeylisyutenko.ai.connectfour.visualizer.ConsoleBoardVisualizer;

import static org.junit.jupiter.api.Assertions.*;
import static ru.alexeylisyutenko.ai.connectfour.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.assertBoardArrayEquals;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.constructBoardArray;

class DefaultBoardTest {

    BoardVisualizer visualizer = new ConsoleBoardVisualizer();

    @Test
    void someOperationsDemo() {
        Board board = new DefaultBoard();

        Board board1 = board.makeMove(3).makeMove(4).makeMove(3).makeMove(0).makeMove(3).makeMove(1).makeMove(3);
        visualizer.visualize(board1);

        System.out.println();
        System.out.println("Longest chain: " + board1.getLongestChain(1));
        System.out.println("Is game over: " + board1.isGameOver());
        System.out.println("Winner ID: " + board1.getWinnerId());
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
        throw new IllegalStateException("Not implemented yet");
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

}