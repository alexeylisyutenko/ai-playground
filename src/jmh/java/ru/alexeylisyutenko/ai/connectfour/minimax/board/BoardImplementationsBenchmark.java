package ru.alexeylisyutenko.ai.connectfour.minimax.board;

import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import ru.alexeylisyutenko.ai.connectfour.game.BitBoard;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.game.DefaultBoard;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.helper.BoardArrays;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_HEIGHT;
import static ru.alexeylisyutenko.ai.connectfour.game.Constants.BOARD_WIDTH;
import static ru.alexeylisyutenko.ai.connectfour.helper.BoardHelpers.constructBoardArray;

@State(Scope.Thread)
public class BoardImplementationsBenchmark {

    List<DefaultBoard> defaultBoards;
    List<BitBoard> bitBoards;
    List<DefaultBoard> nonFinishedDefaultBoards;
    List<BitBoard> nonFinishedBitBoards;
    List<Integer> moves;

    @Setup
    public void setup() {
        List<Pair<int[][], Integer>> boardArrays = BoardArrays.getBoardArrays();
        defaultBoards = boardArrays.stream()
                .map(boardArrayMovePair -> new DefaultBoard(constructBoardArray(boardArrayMovePair.getLeft()), boardArrayMovePair.getRight()))
                .collect(Collectors.toList());
        bitBoards = boardArrays.stream()
                .map(boardArrayMovePair -> new BitBoard(boardArrayMovePair.getLeft(), boardArrayMovePair.getRight()))
                .collect(Collectors.toList());

        List<Pair<int[][], Integer>> nonFinishedBoardArrays = BoardArrays.getNonFinishedBoardArrays();
        nonFinishedDefaultBoards = nonFinishedBoardArrays.stream()
                .map(boardArrayMovePair -> new DefaultBoard(constructBoardArray(boardArrayMovePair.getLeft()), boardArrayMovePair.getRight()))
                .collect(Collectors.toList());
        nonFinishedBitBoards = nonFinishedBoardArrays.stream()
                .map(boardArrayMovePair -> new BitBoard(boardArrayMovePair.getLeft(), boardArrayMovePair.getRight()))
                .collect(Collectors.toList());

        moves = nonFinishedDefaultBoards.stream()
                .map(board -> {
                    List<Pair<Integer, Board>> allNextMoves = MinimaxHelper.getAllNextMoves(board);
                    Collections.shuffle(allNextMoves);
                    return allNextMoves.get(0).getLeft();
                })
                .collect(Collectors.toList());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void isGameOverDefaultBoard(Blackhole bh) {
        for (DefaultBoard defaultBoard : defaultBoards) {
            bh.consume(defaultBoard.isGameOver());
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void isGameOverBitBoard(Blackhole bh) {
        for (BitBoard bitBoard : bitBoards) {
            bh.consume(bitBoard.isGameOver());
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void makeMoveDefaultBoard(Blackhole bh) {
        for (int i = 0; i < moves.size(); i++) {
            DefaultBoard defaultBoard = nonFinishedDefaultBoards.get(i);
            int move = moves.get(i);
            bh.consume(defaultBoard.makeMove(move));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void makeMoveBitBoard(Blackhole bh) {
        for (int i = 0; i < moves.size(); i++) {
            BitBoard bitBoard = nonFinishedBitBoards.get(i);
            int move = moves.get(i);
            bh.consume(bitBoard.makeMove(move));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void getCellPlayerIdDefaultBoard(Blackhole bh) {
        for (DefaultBoard defaultBoard : defaultBoards) {
            for (int row = 0; row < BOARD_HEIGHT; row++) {
                for (int column = 0; column < BOARD_WIDTH; column++) {
                    bh.consume(defaultBoard.getCellPlayerId(row, column));
                }
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void getCellPlayerIdBitBoard(Blackhole bh) {
        for (BitBoard bitBoard : bitBoards) {
            for (int row = 0; row < BOARD_HEIGHT; row++) {
                for (int column = 0; column < BOARD_WIDTH; column++) {
                    bh.consume(bitBoard.getCellPlayerId(row, column));
                }
            }
        }
    }
}
