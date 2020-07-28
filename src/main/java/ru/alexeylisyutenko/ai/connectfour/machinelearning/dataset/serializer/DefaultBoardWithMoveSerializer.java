package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.serializer;

import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.exception.ConnectFourDatasetException;
import ru.alexeylisyutenko.ai.connectfour.game.BitBoard;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class DefaultBoardWithMoveSerializer implements BoardWithMoveSerializer {
    public static final int RECORD_SIZE = (BOARD_HEIGHT * BOARD_WIDTH + 2);

    @Override
    public byte[] serialize(BoardWithMove boardWithMove) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        // Serialize a move as a first byte in a sample.
        os.write(boardWithMove.getMove());

        // Serialize current player id.
        os.write(boardWithMove.getBoard().getCurrentPlayerId());

        // Serialize a board.
        Board board = boardWithMove.getBoard();
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                os.write(board.getCellPlayerId(row, column));
            }
        }
        return os.toByteArray();
    }

    @Override
    public BoardWithMove deserialize(byte[] bytes) {
        Objects.requireNonNull(bytes, "bytes cannot be null");
        if (bytes.length != RECORD_SIZE) {
            throw new ConnectFourDatasetException(String.format("bytes length must be equal to %d", RECORD_SIZE));
        }

        int move = bytes[0];
        if (move < 0 || move >= BOARD_WIDTH) {
            throw new ConnectFourDatasetException("Illegal move found: " + move);
        }

        int currentPlayerId = bytes[1];
        if (currentPlayerId != 1 && currentPlayerId != 2) {
            throw new ConnectFourDatasetException("Illegal current player id found: " + currentPlayerId);
        }

        int currentBytesIndex = 2;
        int[][] boardDoubleArray = new int[BOARD_HEIGHT][BOARD_WIDTH];
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                int currentByte = bytes[currentBytesIndex];
                if (currentByte < 0 || currentByte > 2) {
                    throw new ConnectFourDatasetException(String.format("Illegal board byte found at position %d: %s", currentBytesIndex, currentByte));
                }
                boardDoubleArray[row][column] = currentByte;
                currentBytesIndex++;
            }
        }

        BitBoard board = new BitBoard(boardDoubleArray, currentPlayerId);
        return new BoardWithMove(board, move);
    }
}
