package ru.alexeylisyutenko.ai.connectfour.minimax;

import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.DefaultBoard;

import java.util.List;

import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.constructBoardArray;

public final class Boards {
    public static List<Board> getBoardsForBenchmarking() {
        return List.of(
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 1, 0, 0, 0, 0},
                        {0, 1, 2, 0, 0, 0, 0},
                        {0, 2, 2, 0, 0, 2, 0},
                        {0, 2, 1, 0, 1, 1, 0},
                        {1, 1, 2, 2, 2, 1, 2}
                }), 1),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 1, 0},
                        {1, 0, 0, 0, 2, 2, 0},
                        {1, 0, 1, 0, 2, 2, 0}
                }), 1),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 2, 0, 1, 0},
                        {0, 0, 2, 1, 0, 2, 0},
                        {0, 0, 1, 2, 0, 2, 1},
                        {0, 0, 2, 1, 0, 1, 1}
                }), 2),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 1, 0, 0, 0, 0},
                        {0, 1, 1, 0, 0, 0, 0},
                        {2, 1, 2, 0, 2, 0, 0},
                        {2, 2, 2, 0, 2, 2, 0},
                        {1, 1, 1, 0, 1, 2, 1}
                }), 1),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 2, 0, 2, 0, 0},
                        {0, 0, 2, 0, 1, 0, 0},
                        {0, 2, 1, 0, 1, 0, 0},
                        {1, 1, 1, 0, 2, 1, 1},
                        {2, 1, 2, 1, 2, 1, 2},
                        {1, 2, 2, 2, 1, 1, 2}
                }), 2),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 2, 0},
                        {0, 0, 0, 0, 0, 2, 0},
                        {0, 0, 0, 0, 0, 1, 1},
                        {0, 1, 0, 1, 0, 2, 2},
                        {0, 1, 2, 1, 1, 1, 2},
                        {0, 2, 2, 1, 2, 2, 1}
                }), 1),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {2, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 2},
                        {1, 0, 1, 0, 0, 0, 1},
                        {2, 0, 2, 2, 0, 1, 1},
                        {1, 2, 2, 1, 0, 2, 1}
                }), 2),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 2, 0, 0, 0, 0, 0},
                        {0, 2, 0, 1, 0, 0, 0},
                        {0, 2, 0, 2, 1, 2, 0},
                        {1, 1, 0, 2, 1, 1, 2},
                        {1, 2, 0, 2, 2, 1, 2},
                        {1, 1, 2, 1, 1, 2, 1}
                }), 1),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 1, 0},
                        {1, 0, 0, 0, 0, 2, 0},
                        {1, 2, 1, 0, 0, 2, 2}
                }), 1),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 2, 0, 0, 2},
                        {2, 0, 0, 1, 0, 0, 1},
                        {1, 0, 0, 2, 0, 0, 1},
                        {2, 0, 1, 2, 2, 0, 2},
                        {1, 0, 1, 1, 1, 0, 2}
                }), 2),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 1, 0},
                        {0, 0, 0, 0, 0, 1, 0},
                        {0, 0, 0, 0, 0, 2, 0},
                        {0, 0, 0, 2, 0, 2, 0},
                        {0, 1, 0, 2, 0, 2, 0},
                        {0, 1, 1, 2, 1, 1, 0}
                }), 2),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 0, 0}
                }), 2),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 1, 0, 0, 0},
                        {0, 0, 0, 2, 0, 0, 2},
                        {0, 2, 0, 1, 0, 2, 2},
                        {0, 1, 1, 2, 1, 2, 1},
                        {2, 1, 2, 1, 1, 2, 1}
                }), 1),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {2, 0, 0, 0, 0, 0, 0},
                        {1, 0, 2, 0, 1, 0, 0}
                }), 1),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 2, 0},
                        {0, 0, 0, 0, 0, 1, 0},
                        {0, 0, 0, 0, 0, 2, 0},
                        {1, 1, 1, 2, 0, 2, 1}
                }), 2),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 1, 0, 0, 0},
                        {0, 1, 0, 1, 0, 0, 1},
                        {2, 1, 0, 2, 0, 0, 2},
                        {1, 1, 2, 2, 1, 1, 1},
                        {2, 2, 2, 1, 2, 1, 2},
                        {1, 1, 2, 2, 1, 2, 2}
                }), 2),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 0, 0},
                        {0, 2, 0, 0, 0, 0, 0},
                        {0, 2, 0, 2, 1, 0, 0},
                        {1, 2, 2, 2, 1, 1, 1}
                }), 2),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 1, 0, 0},
                        {0, 0, 2, 2, 1, 0, 0},
                        {0, 0, 1, 1, 2, 0, 2},
                        {0, 1, 1, 2, 2, 1, 1},
                        {2, 2, 2, 1, 1, 2, 2},
                        {2, 1, 1, 1, 2, 1, 2}
                }), 1),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 1, 0, 1}
                }), 1),
                new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 1, 2, 0, 0},
                        {2, 0, 0, 1, 2, 0, 0},
                        {1, 0, 1, 2, 2, 0, 0},
                        {1, 0, 1, 2, 1, 1, 0},
                        {1, 0, 1, 2, 2, 2, 0},
                        {2, 1, 2, 1, 1, 2, 2}
                }), 1)
        );
    }
}
