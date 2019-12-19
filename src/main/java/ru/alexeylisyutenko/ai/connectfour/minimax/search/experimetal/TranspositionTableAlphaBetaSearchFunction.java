package ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;

import java.util.*;
import java.util.stream.Collectors;

import static ru.alexeylisyutenko.ai.connectfour.util.Constants.NEGATIVE_INFINITY;
import static ru.alexeylisyutenko.ai.connectfour.util.Constants.POSITIVE_INFINITY;

public class TranspositionTableAlphaBetaSearchFunction implements SearchFunction {

    private final Map<Board, TranspositionTableEntry> transpositionTable = new HashMap<>();

    @Override
    public Move search(Board board, int depth, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            throw new IllegalStateException("Search function was called on a terminal node");
        }

        List<Pair<Integer, Board>> nextMoves = MinimaxHelper.getAllNextMoves(board);
        int bestColumn = NEGATIVE_INFINITY;
        int bestScore = NEGATIVE_INFINITY;

        for (Pair<Integer, Board> nextMove : nextMoves) {
            int score = -1 * findAlphaBetaBoardValue(nextMove.getRight(), depth - 1, -POSITIVE_INFINITY, -bestScore, evaluationFunction);
            if (score > bestScore) {
                bestScore = score;
                bestColumn = nextMove.getLeft();
            }
        }

        return new Move(bestColumn, bestScore);
    }

    private int findAlphaBetaBoardValue(Board board, int depth, int alpha, int beta, EvaluationFunction evaluationFunction) {
        if (MinimaxHelper.isTerminal(depth, board)) {
            return evaluationFunction.evaluate(board);
        }

        int originalAlpha = alpha;

        TranspositionTableEntry transpositionTableEntry = transpositionTable.get(board);
        if (transpositionTableEntry != null && transpositionTableEntry.getDepth() >= depth) {
            switch (transpositionTableEntry.getType()) {
                case EXACT_VALUE:
                    return transpositionTableEntry.getValue();
                case UPPER_BOUND:
                    beta = Math.min(beta, transpositionTableEntry.getValue());
                    break;
                case LOWER_BOUND:
                    alpha = Math.max(alpha, transpositionTableEntry.getValue());
                    break;
            }
        }

        Iterator<Pair<Integer, Board>> nextMovesIterator;
//        if (transpositionTableEntry != null && transpositionTableEntry.getBestMove().isPresent()) {
//            nextMovesIterator = MinimaxHelper.getAllNextMovesIterator(board, transpositionTableEntry.getBestMove().get());
//        } else {
//            nextMovesIterator = MinimaxHelper.getAllNextMovesIterator(board);
//        }

        nextMovesIterator = MinimaxHelper.getAllNextMovesIterator(board);

        int bestMove = 0;
        int value = NEGATIVE_INFINITY;
        while (nextMovesIterator.hasNext()) {
            Pair<Integer, Board> nextMove = nextMovesIterator.next();

            int score = -1 * findAlphaBetaBoardValue(nextMove.getRight(), depth - 1, -beta, -alpha, evaluationFunction);
            if (score > value) {
                value = score;
                bestMove = nextMove.getLeft();
            }

            alpha = Math.max(alpha, value);
            if (alpha >= beta) {
                break;
            }
        }

        // Save entry in the transposition table.
        TranspositionTableEntry entry;
        if (value <= originalAlpha) {
            entry = new TranspositionTableEntry(depth, TranspositionTableEntryType.UPPER_BOUND, value, null);
        } else if (value >= beta) {
            entry = new TranspositionTableEntry(depth, TranspositionTableEntryType.LOWER_BOUND, value, null);
        } else {
            entry = new TranspositionTableEntry(depth, TranspositionTableEntryType.EXACT_VALUE, value, bestMove);
        }
        transpositionTable.put(board, entry);

        // Add records wisely.
//        transpositionTable.merge(board, entry, (entry1, entry2) -> entry1.getDepth() > entry2.getDepth() ? entry1 : entry2);

        /*
    ttEntry.value := value
    if value ≤ alphaOrig then
        ttEntry.flag := UPPERBOUND
    else if value ≥ β then
        ttEntry.flag := LOWERBOUND
    else
        ttEntry.flag := EXACT

     // Think about why it's the case!
         */

        return alpha;
    }

    private enum TranspositionTableEntryType {
        EXACT_VALUE, UPPER_BOUND, LOWER_BOUND;
    }

    private static class TranspositionTableEntry {
        private final int depth;
        private final TranspositionTableEntryType type;
        private final int value;
        private final Integer bestMove;

        public TranspositionTableEntry(int depth, TranspositionTableEntryType type, int value, Integer bestMove) {
            this.depth = depth;
            this.type = type;
            this.value = value;
            this.bestMove = bestMove;
        }

        public int getDepth() {
            return depth;
        }

        public TranspositionTableEntryType getType() {
            return type;
        }

        public int getValue() {
            return value;
        }

        public Optional<Integer> getBestMove() {
            return Optional.ofNullable(bestMove);
        }
    }
}

//    /**
//     * Reccursively score connect 4 position using negamax variant of alpha-beta algorithm.
//     * @param: alpha < beta, a score window within which we are evaluating the position.
//     *
//     * @return the exact score, an upper or lower bound score depending of the case:
//     * - if actual score of position <= alpha then actual score <= return value <= alpha
//     * - if actual score of position >= beta then beta <= return value <= actual score
//     * - if alpha <= actual score <= beta then return value = actual score
//     */
//    int negamax(const Position &P, int alpha, int beta) {
//        assert(alpha < beta);
//        nodeCount++; // increment counter of explored nodes
//
//        if(P.nbMoves() == Position::WIDTH*Position::HEIGHT) // check for draw game
//            return 0;
//
//        for(int x = 0; x < Position::WIDTH; x++) // check if current player can win next move
//            if(P.canPlay(x) && P.isWinningMove(x))
//                return (Position::WIDTH*Position::HEIGHT+1 - P.nbMoves())/2;
//
//        int max = (Position::WIDTH*Position::HEIGHT-1 - P.nbMoves())/2;	// upper bound of our score as we cannot win immediately
//        if(int val = transTable.get(P.key()))
//        max = val + Position::MIN_SCORE - 1;
//
//        if(beta > max) {
//            beta = max;                     // there is no need to keep beta above our max possible score.
//            if(alpha >= beta) return beta;  // prune the exploration if the [alpha;beta] window is empty.
//        }
//
//        for(int x = 0; x < Position::WIDTH; x++) // compute the score of all possible next move and keep the best one
//            if(P.canPlay(columnOrder[x])) {
//                Position P2(P);
//                P2.play(columnOrder[x]);               // It's opponent turn in P2 position after current player plays x column.
//                int score = -negamax(P2, -beta, -alpha); // explore opponent's score within [-beta;-alpha] windows:
//                // no need to have good precision for score better than beta (opponent's score worse than -beta)
//                // no need to check for score worse than alpha (opponent's score worse better than -alpha)
//
//                if(score >= beta) return score;  // prune the exploration if we find a possible move better than what we were looking for.
//                if(score > alpha) alpha = score; // reduce the [alpha;beta] window for next exploration, as we only
//                // need to search for a position that is better than the best so far.
//            }
//
//        transTable.put(P.key(), alpha - Position::MIN_SCORE + 1); // save the upper bound of the position
//        return alpha;
//    }
