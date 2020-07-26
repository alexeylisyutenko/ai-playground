package ru.alexeylisyutenko.ai.connectfour.dataset.serializer;

import ru.alexeylisyutenko.ai.connectfour.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.io.ByteArrayOutputStream;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class DefaultBoardWithMoveSerializer implements BoardWithMoveSerializer {
    @Override
    public byte[] serialize(BoardWithMove boardWithMove) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        // Serialize a move as a first byte in a sample.
        os.write(boardWithMove.getMove());

        // Serialize a board.
        Board board = boardWithMove.getBoard();
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                int cellByte;
                int cellPlayerId = board.getCellPlayerId(row, column);
                if (cellPlayerId == board.getCurrentPlayerId()) {
                    cellByte = 1;
                } else if (cellPlayerId == board.getOtherPlayerId()) {
                    cellByte = 2;
                } else {
                    cellByte = 0;
                }
                os.write(cellByte);
            }
        }
        return os.toByteArray();
    }
}
