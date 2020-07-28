package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.serializer;

import org.junit.jupiter.api.Test;
import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.game.BitBoard;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.DefaultBoard;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

        assertEquals(44, serializedBytes.length);
        assertArrayEquals(
                new byte[]{
                        3, 1,
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

        assertEquals(44, serializedBytes.length);
        assertArrayEquals(
                new byte[]{
                        6, 2,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0,
                        0, 0, 2, 1, 2, 0, 0,
                        0, 0, 1, 2, 1, 0, 0,
                        2, 1, 2, 2, 2, 1, 0
                }, serializedBytes);
    }

    @Test
    public void deserialize() {
        byte[] bytes = {
                3, 1,
                0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0,
                0, 0, 2, 1, 2, 0, 0,
                0, 0, 1, 2, 1, 0, 0,
                2, 1, 2, 2, 2, 1, 0
        };
        BoardWithMoveSerializer boardWithMoveSerializer = new DefaultBoardWithMoveSerializer();
        BoardWithMove boardWithMove = boardWithMoveSerializer.deserialize(bytes);

        Board expectedBoard = new BitBoard(
                new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 2, 1, 2, 0, 0},
                        {0, 0, 1, 2, 1, 0, 0},
                        {2, 1, 2, 2, 2, 1, 0}
                }, 1);
        assertEquals(expectedBoard, boardWithMove.getBoard());
    }
}