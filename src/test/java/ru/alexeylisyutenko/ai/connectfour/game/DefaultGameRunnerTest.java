package ru.alexeylisyutenko.ai.connectfour.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.constructBoardArray;
import static ru.alexeylisyutenko.ai.connectfour.game.GameState.*;

@ExtendWith(MockitoExtension.class)
class DefaultGameRunnerTest {
    @Mock
    Player player1;

    @Mock
    Player player2;

    @Mock
    GameEventListener gameEventListener;

    @Test
    void normalGameScenario() {
        // -------- Initialize environment and variables required for a test  --------
        int[] boardArray = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 0, 0, 0}
        });
        Board initialBoard = new DefaultBoard(boardArray, 1);
        GameRunner gameRunner = new DefaultGameRunner(player1, player2, 10, initialBoard, gameEventListener);
        InOrder inOrder = inOrder(player1, player2, gameEventListener);
        ArgumentCaptor<GameContext> gameContextArgumentCaptor;
        GameContext gameContext;

        //  -------- Check gameRunner's fields before start of a main  --------
        assertSame(player1, gameRunner.getPlayer1());
        assertSame(player2, gameRunner.getPlayer2());
        assertEquals(10, gameRunner.getTimeLimit());
        assertEquals(STOPPED, gameRunner.getGameState());

        //  -------- Start the main and verify events called  --------
        gameRunner.startGame();

        inOrder.verify(player1).gameStarted(1);
        inOrder.verify(player2).gameStarted(2);
        inOrder.verify(gameEventListener).gameStarted(same(gameRunner), eq(initialBoard));

        gameContextArgumentCaptor = ArgumentCaptor.forClass(GameContext.class);
        inOrder.verify(player1).requestMove(gameContextArgumentCaptor.capture());
        gameContext = gameContextArgumentCaptor.getValue();
        assertEquals(10, gameContext.getTimeout());
        assertEquals(new DefaultBoard(constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 0, 0, 0}
        }), 1), gameContext.getBoard());
        assertEquals(1, gameContext.getBoardHistory().size());
        inOrder.verify(gameEventListener).moveRequested(
                same(gameRunner),
                eq(1),
                eq(new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 2, 2, 0, 0, 0}
                }), 1))
        );
        assertEquals(WAITING_FOR_PLAYER1_MOVE, gameRunner.getGameState());

        // -------- Make player 1 move and verify events called  --------
        gameContext.makeMove(0);

        inOrder.verify(gameEventListener).moveMade(
                same(gameRunner),
                eq(1),
                eq(0),
                eq(new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 2, 2, 0, 0, 0}
                }), 2))
        );
        gameContextArgumentCaptor = ArgumentCaptor.forClass(GameContext.class);
        inOrder.verify(player2).requestMove(gameContextArgumentCaptor.capture());
        gameContext = gameContextArgumentCaptor.getValue();
        assertEquals(10, gameContext.getTimeout());
        assertEquals(new DefaultBoard(constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 0, 0, 0}
        }), 2), gameContext.getBoard());
        assertEquals(2, gameContext.getBoardHistory().size());
        inOrder.verify(gameEventListener).moveRequested(
                same(gameRunner),
                eq(2),
                eq(new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 2, 2, 0, 0, 0}
                }), 2))
        );
        assertEquals(WAITING_FOR_PLAYER2_MOVE, gameRunner.getGameState());

        // -------- Make player 2 move and verify events called  --------
        gameContext.makeMove(4);

        inOrder.verify(gameEventListener).moveMade(
                same(gameRunner),
                eq(2),
                eq(4),
                eq(new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 2, 2, 2, 0, 0}
                }), 1))
        );
        gameContextArgumentCaptor = ArgumentCaptor.forClass(GameContext.class);
        inOrder.verify(player1).requestMove(gameContextArgumentCaptor.capture());
        gameContext = gameContextArgumentCaptor.getValue();
        assertEquals(10, gameContext.getTimeout());
        assertEquals(new DefaultBoard(constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 2, 0, 0}
        }), 1), gameContext.getBoard());
        assertEquals(3, gameContext.getBoardHistory().size());
        inOrder.verify(gameEventListener).moveRequested(
                same(gameRunner),
                eq(1),
                eq(new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 2, 2, 2, 0, 0}
                }), 1))
        );
        assertEquals(WAITING_FOR_PLAYER1_MOVE, gameRunner.getGameState());

        // -------- Make player 1 move, which finishes the main, and verify events called  --------
        gameContext.makeMove(0);

        inOrder.verify(gameEventListener).moveMade(
                same(gameRunner),
                eq(1),
                eq(0),
                eq(new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 2, 2, 2, 0, 0}
                }), 2))
        );
        inOrder.verify(player1).gameFinished(eq(GameResult.normalVictory(1, 2)));
        inOrder.verify(player2).gameFinished(eq(GameResult.normalVictory(1, 2)));
        inOrder.verify(gameEventListener).gameFinished(
                same(gameRunner),
                eq(GameResult.normalVictory(1, 2))
        );
        assertEquals(STOPPED, gameRunner.getGameState());
        assertEquals(4, gameRunner.getBoardHistory().size());
        assertEquals(new DefaultBoard(constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 0, 0, 0}
        }), 1), gameRunner.getBoardHistory().get(0));
        assertEquals(new DefaultBoard(constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 0, 0, 0}
        }), 2), gameRunner.getBoardHistory().get(1));
        assertEquals(new DefaultBoard(constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 2, 0, 0}
        }), 1), gameRunner.getBoardHistory().get(2));
        assertEquals(new DefaultBoard(constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 2, 0, 0}
        }), 2), gameRunner.getBoardHistory().get(3));

        verifyNoMoreInteractions(player1, player2, gameEventListener);
    }

    @Test
    void tieGameScenario() {
        // -------- Initialize environment and variables required for a test  --------
        int[] boardArray = constructBoardArray(new int[][]{
                {0, 1, 2, 2, 2, 1, 2},
                {2, 2, 1, 1, 2, 1, 1},
                {1, 1, 1, 2, 1, 2, 1},
                {1, 1, 2, 2, 1, 1, 1},
                {2, 1, 2, 1, 2, 1, 2},
                {1, 2, 1, 2, 1, 2, 1}
        });
        Board initialBoard = new DefaultBoard(boardArray, 1);
        GameRunner gameRunner = new DefaultGameRunner(player1, player2, 10, initialBoard, gameEventListener);
        InOrder inOrder = inOrder(player1, player2, gameEventListener);
        ArgumentCaptor<GameContext> gameContextArgumentCaptor;
        GameContext gameContext;

        //  -------- Check gameRunner's fields before start of a main  --------
        assertSame(player1, gameRunner.getPlayer1());
        assertSame(player2, gameRunner.getPlayer2());
        assertEquals(10, gameRunner.getTimeLimit());
        assertEquals(STOPPED, gameRunner.getGameState());

        //  -------- Start the main and verify events called  --------
        gameRunner.startGame();

        inOrder.verify(player1).gameStarted(1);
        inOrder.verify(player2).gameStarted(2);
        inOrder.verify(gameEventListener).gameStarted(same(gameRunner), eq(initialBoard));

        gameContextArgumentCaptor = ArgumentCaptor.forClass(GameContext.class);
        inOrder.verify(player1).requestMove(gameContextArgumentCaptor.capture());
        gameContext = gameContextArgumentCaptor.getValue();
        assertEquals(10, gameContext.getTimeout());
        assertEquals(new DefaultBoard(constructBoardArray(new int[][]{
                {0, 1, 2, 2, 2, 1, 2},
                {2, 2, 1, 1, 2, 1, 1},
                {1, 1, 1, 2, 1, 2, 1},
                {1, 1, 2, 2, 1, 1, 1},
                {2, 1, 2, 1, 2, 1, 2},
                {1, 2, 1, 2, 1, 2, 1}
        }), 1), gameContext.getBoard());
        inOrder.verify(gameEventListener).moveRequested(
                same(gameRunner),
                eq(1),
                eq(new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 1, 2, 2, 2, 1, 2},
                        {2, 2, 1, 1, 2, 1, 1},
                        {1, 1, 1, 2, 1, 2, 1},
                        {1, 1, 2, 2, 1, 1, 1},
                        {2, 1, 2, 1, 2, 1, 2},
                        {1, 2, 1, 2, 1, 2, 1}
                }), 1))
        );
        assertEquals(WAITING_FOR_PLAYER1_MOVE, gameRunner.getGameState());

        // -------- Make player 1 move, which finishes the main, and verify events called  --------
        gameContext.makeMove(0);

        inOrder.verify(gameEventListener).moveMade(
                same(gameRunner),
                eq(1),
                eq(0),
                eq(new DefaultBoard(constructBoardArray(new int[][]{
                        {1, 1, 2, 2, 2, 1, 2},
                        {2, 2, 1, 1, 2, 1, 1},
                        {1, 1, 1, 2, 1, 2, 1},
                        {1, 1, 2, 2, 1, 1, 1},
                        {2, 1, 2, 1, 2, 1, 2},
                        {1, 2, 1, 2, 1, 2, 1}
                }), 2))
        );
        inOrder.verify(player1).gameFinished(eq(GameResult.tie()));
        inOrder.verify(player2).gameFinished(eq(GameResult.tie()));
        inOrder.verify(gameEventListener).gameFinished(
                same(gameRunner),
                eq(GameResult.tie())
        );
        assertEquals(STOPPED, gameRunner.getGameState());

        verifyNoMoreInteractions(player1, player2, gameEventListener);
    }

    @Test
    void gameStopScenario() {
        // -------- Initialize environment and variables required for a test  --------
        int[] boardArray = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 0, 0, 0}
        });
        Board initialBoard = new DefaultBoard(boardArray, 1);
        GameRunner gameRunner = new DefaultGameRunner(player1, player2, 10, initialBoard, gameEventListener);
        InOrder inOrder = inOrder(player1, player2, gameEventListener);
        ArgumentCaptor<GameContext> gameContextArgumentCaptor;
        GameContext gameContext;

        //  -------- Check gameRunner's fields before start of a main  --------
        assertSame(player1, gameRunner.getPlayer1());
        assertSame(player2, gameRunner.getPlayer2());
        assertEquals(10, gameRunner.getTimeLimit());
        assertEquals(STOPPED, gameRunner.getGameState());

        //  -------- Start the main and verify events called  --------
        gameRunner.startGame();

        inOrder.verify(player1).gameStarted(1);
        inOrder.verify(player2).gameStarted(2);
        inOrder.verify(gameEventListener).gameStarted(same(gameRunner), eq(initialBoard));

        gameContextArgumentCaptor = ArgumentCaptor.forClass(GameContext.class);
        inOrder.verify(player1).requestMove(gameContextArgumentCaptor.capture());
        gameContext = gameContextArgumentCaptor.getValue();
        assertEquals(10, gameContext.getTimeout());
        assertEquals(new DefaultBoard(constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 0, 0, 0}
        }), 1), gameContext.getBoard());
        inOrder.verify(gameEventListener).moveRequested(
                same(gameRunner),
                eq(1),
                eq(new DefaultBoard(constructBoardArray(new int[][]{
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 2, 2, 0, 0, 0}
                }), 1))
        );
        assertEquals(WAITING_FOR_PLAYER1_MOVE, gameRunner.getGameState());

        //  -------- Stop the main and verify events called  --------
        gameRunner.stopGame();

        inOrder.verify(player1).gameFinished(eq(GameResult.stoppedGame()));
        inOrder.verify(player2).gameFinished(eq(GameResult.stoppedGame()));
        inOrder.verify(gameEventListener).gameFinished(
                same(gameRunner),
                eq(GameResult.stoppedGame())
        );
        assertEquals(STOPPED, gameRunner.getGameState());

        verifyNoMoreInteractions(player1, player2, gameEventListener);
    }

    @Test
    void ifPlayerMakesIllegalMoveTheyShouldBeRequestedForMoveAgain() {
        // -------- Initialize environment and variables required for a test  --------
        int[] boardArray = constructBoardArray(new int[][]{
                {2, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 0, 0, 0}
        });
        Board initialBoard = new DefaultBoard(boardArray, 1);
        GameRunner gameRunner = new DefaultGameRunner(player1, player2, 10, initialBoard, gameEventListener);
        InOrder inOrder = inOrder(player1, player2, gameEventListener);
        ArgumentCaptor<GameContext> gameContextArgumentCaptor;
        GameContext gameContext;

        //  -------- Check gameRunner's fields before start of a main  --------
        assertSame(player1, gameRunner.getPlayer1());
        assertSame(player2, gameRunner.getPlayer2());
        assertEquals(10, gameRunner.getTimeLimit());
        assertEquals(STOPPED, gameRunner.getGameState());

        //  -------- Start the main and verify events called  --------
        gameRunner.startGame();

        inOrder.verify(player1).gameStarted(1);
        inOrder.verify(player2).gameStarted(2);
        inOrder.verify(gameEventListener).gameStarted(same(gameRunner), eq(initialBoard));

        gameContextArgumentCaptor = ArgumentCaptor.forClass(GameContext.class);
        inOrder.verify(player1).requestMove(gameContextArgumentCaptor.capture());
        gameContext = gameContextArgumentCaptor.getValue();
        assertEquals(10, gameContext.getTimeout());
        assertEquals(new DefaultBoard(constructBoardArray(new int[][]{
                {2, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 0, 0, 0}
        }), 1), gameContext.getBoard());
        inOrder.verify(gameEventListener).moveRequested(
                same(gameRunner),
                eq(1),
                eq(new DefaultBoard(constructBoardArray(new int[][]{
                        {2, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {2, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 2, 2, 0, 0, 0}
                }), 1))
        );
        assertEquals(WAITING_FOR_PLAYER1_MOVE, gameRunner.getGameState());

        //  -------- Make illegal move and verify events called  --------
        gameContext.makeMove(0);

        inOrder.verify(gameEventListener).illegalMoveAttempted(
                same(gameRunner),
                eq(1),
                eq(0),
                eq(new DefaultBoard(constructBoardArray(new int[][]{
                        {2, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {2, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 2, 2, 0, 0, 0}
                }), 1))
        );
        gameContextArgumentCaptor = ArgumentCaptor.forClass(GameContext.class);
        inOrder.verify(player1).requestMove(gameContextArgumentCaptor.capture());
        gameContext = gameContextArgumentCaptor.getValue();
        assertEquals(10, gameContext.getTimeout());
        assertEquals(new DefaultBoard(constructBoardArray(new int[][]{
                {2, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 0, 0, 0}
        }), 1), gameContext.getBoard());
        inOrder.verify(gameEventListener).moveRequested(
                same(gameRunner),
                eq(1),
                eq(new DefaultBoard(constructBoardArray(new int[][]{
                        {2, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {2, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 2, 2, 0, 0, 0}
                }), 1))
        );
        assertEquals(WAITING_FOR_PLAYER1_MOVE, gameRunner.getGameState());

        verifyNoMoreInteractions(player1, player2, gameEventListener);
    }

    @Test
    void impossibleToStartGameIfItIsNotStopped() {
        // -------- Initialize environment and variables required for a test  --------
        int[] boardArray = constructBoardArray(new int[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 0, 2, 2, 0, 0, 0}
        });
        Board initialBoard = new DefaultBoard(boardArray, 1);
        GameRunner gameRunner = new DefaultGameRunner(player1, player2, 10, initialBoard, gameEventListener);

        // -------- Start a main  --------
        gameRunner.startGame();

        // -------- Try to start the main again an verify  --------
        IllegalStateException exception = assertThrows(IllegalStateException.class, gameRunner::startGame);
        assertEquals("Game is already started. You should stop current main first.", exception.getMessage());
    }

}