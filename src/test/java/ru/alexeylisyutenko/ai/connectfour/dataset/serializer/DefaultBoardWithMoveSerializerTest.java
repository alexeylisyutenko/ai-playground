package ru.alexeylisyutenko.ai.connectfour.dataset.serializer;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.game.DefaultBoard;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.assertBoardArrayEquals;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.constructBoardArray;

class DefaultBoardWithMoveSerializerTest {
    @Test
    void serialize() {
        int[] boardArray = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 1, 2, 0, 0},
                {0, 0, 1, 2, 1, 0, 0},
                {2, 1, 2, 2, 2, 1, 0}
        });
        DefaultBoard board = new DefaultBoard(boardArray, 1);
        BoardWithMove boardWithMove = new BoardWithMove(board, 3);

        BoardWithMoveSerializer boardWithMoveSerializer = new DefaultBoardWithMoveSerializer();
        byte[] serializedBytes = boardWithMoveSerializer.serialize(boardWithMove);

        assertEquals(43, serializedBytes.length);
        assertArrayEquals(
                new byte[] {
                        3,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 2, 1, 2, 0, 0,
                        0, 0, 1, 2, 1, 0, 0,
                        2, 1, 2, 2, 2, 1, 0
                }, serializedBytes);
    }

    @Test
    void serializeInvertedCurrentPlayerId() {
        int[] boardArray = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 1, 2, 0, 0},
                {0, 0, 1, 2, 1, 0, 0},
                {2, 1, 2, 2, 2, 1, 0}
        });
        DefaultBoard board = new DefaultBoard(boardArray, 2);
        BoardWithMove boardWithMove = new BoardWithMove(board, 6);

        BoardWithMoveSerializer boardWithMoveSerializer = new DefaultBoardWithMoveSerializer();
        byte[] serializedBytes = boardWithMoveSerializer.serialize(boardWithMove);

        assertEquals(43, serializedBytes.length);
        assertArrayEquals(
                new byte[] {
                        6,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 1, 2, 1, 0, 0,
                        0, 0, 2, 1, 2, 0, 0,
                        1, 2, 1, 1, 1, 2, 0
                }, serializedBytes);
    }
}