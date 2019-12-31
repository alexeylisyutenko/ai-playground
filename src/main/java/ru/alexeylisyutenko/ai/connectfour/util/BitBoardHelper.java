package ru.alexeylisyutenko.ai.connectfour.util;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;

public class BitBoardHelper {
    private BitBoardHelper() {
    }

    public static String bitmapToString(long bitmap) {
        StringBuilder sb = new StringBuilder();
        for (int row = BOARD_HEIGHT; row >= 0; row--) {
            for (int column = 0; column < BOARD_WIDTH; column++) {
                int bitNumber = row + column * (BOARD_HEIGHT + 1);

                long mask = 1L << bitNumber;
                if ((bitmap & mask) == 0) {
                    sb.append("0  ");
                } else {
                    sb.append("1  ");
                }
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
