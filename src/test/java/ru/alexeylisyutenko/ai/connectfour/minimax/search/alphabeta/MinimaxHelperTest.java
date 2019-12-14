package ru.alexeylisyutenko.ai.connectfour.minimax.search.alphabeta;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.DefaultBoard;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.constructBoardArray;

class MinimaxHelperTest {

    @Test
    void allNextMovesIteratorMustWorkProperly() {
        int[] boardArray = constructBoardArray(new int[][]{
                {2, 0, 0, 1, 0, 0, 0},
                {1, 0, 0, 2, 0, 0, 0},
                {2, 0, 0, 1, 0, 0, 0},
                {1, 0, 2, 1, 2, 0, 0},
                {2, 0, 1, 2, 1, 0, 0},
                {2, 1, 2, 2, 2, 1, 0}
        });
        Board board = new DefaultBoard(boardArray, 1);

        Iterator<Pair<Integer, Board>> iterator = MinimaxHelper.getAllNextMovesIterator(board);

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(4), iterator.next().getLeft());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(2), iterator.next().getLeft());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(5), iterator.next().getLeft());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next().getLeft());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(6), iterator.next().getLeft());

        assertFalse(iterator.hasNext());
    }

    @Test
    void allNextMovesIteratorOnEmptyBoardMustWorkProperly() {
        Board board = new DefaultBoard();

        Iterator<Pair<Integer, Board>> iterator = MinimaxHelper.getAllNextMovesIterator(board);

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(3), iterator.next().getLeft());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(4), iterator.next().getLeft());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(2), iterator.next().getLeft());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(5), iterator.next().getLeft());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next().getLeft());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(6), iterator.next().getLeft());

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(0), iterator.next().getLeft());

        assertFalse(iterator.hasNext());
    }

    @Test
    void allNextMovesIteratorOnFullBoardMustWorkProperly() {
        int[] boardArray = constructBoardArray(new int[][]{
                {2, 2, 1, 1, 2, 1, 2},
                {1, 1, 2, 2, 1, 2, 1},
                {2, 2, 1, 1, 2, 1, 2},
                {1, 1, 2, 1, 2, 2, 1},
                {2, 1, 1, 2, 1, 2, 1},
                {2, 1, 2, 2, 2, 1, 2}
        });
        Board board = new DefaultBoard(boardArray, 1);

        Iterator<Pair<Integer, Board>> iterator = MinimaxHelper.getAllNextMovesIterator(board);

        assertFalse(iterator.hasNext());
    }
}