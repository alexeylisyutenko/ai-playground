package ru.alexeylisyutenko.ai.connectfour.minimax.search.experimetal;

import org.apache.commons.lang3.tuple.Pair;
import ru.alexeylisyutenko.ai.connectfour.game.Board;
import ru.alexeylisyutenko.ai.connectfour.minimax.EvaluationFunction;
import ru.alexeylisyutenko.ai.connectfour.minimax.MinimaxHelper;
import ru.alexeylisyutenko.ai.connectfour.minimax.Move;
import ru.alexeylisyutenko.ai.connectfour.minimax.SearchFunction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static ru.alexeylisyutenko.ai.connectfour.util.Constants.NEGATIVE_INFINITY;
import static ru.alexeylisyutenko.ai.connectfour.util.Constants.POSITIVE_INFINITY;

public class TranspositionTableAlphaBetaSearchFunction implements SearchFunction {

    private final Map<Board, Pair<Integer, Integer>> transpositionTable = new HashMap<>();

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

        // Upper bound of our score.
//        int max = transpositionTable.getOrDefault(board, POSITIVE_INFINITY);
//
//
//        if (beta > max) {
//            beta = max;                     // there is no need to keep beta above our max possible score.
//            if (alpha >= beta) {
//                return beta;  // prune the exploration if the [alpha;beta] window is empty.
//            }
//        }

        Pair<Integer, Integer> transpositionTableRecord = transpositionTable.get(board);
        if (transpositionTableRecord != null) {
            int value = transpositionTableRecord.getLeft();
            int tableDepth = transpositionTableRecord.getRight();

            // We know value of this node already. What to do with it?
            System.out.println(String.format("We already know a score for a node! Score: '%d' at depth '%d'. Current node's depth: '%d'", value, tableDepth, depth));

            // Can we just return it? I don't think so! Because this saved value depends on beta's value when it was calculated.
//            if (depth == tableDepth) {
//                 But we return not an actual score here, but a value which can be bigger than current alpha.
//                return value;
//            }

            // How can we use it?

            // Can we prune?
            if (beta > value) {
                // ???
            }
        }

        Iterator<Pair<Integer, Board>> nextMovesIterator = MinimaxHelper.getAllNextMovesIterator(board);
        while (nextMovesIterator.hasNext()) {
            Pair<Integer, Board> nextMove = nextMovesIterator.next();

            int score = -1 * findAlphaBetaBoardValue(nextMove.getRight(), depth - 1, -beta, -alpha, evaluationFunction);
            if (score >= beta) {
                return score;  // prune the exploration if we find a possible move better than what we were looking for.
            }
            if (score > alpha) {
                alpha = score;
            }
        }

        // We safe this value only if we found an exact value within some depth.
        // Alpha is not just a score of this node, the actual score <= alpha
        // How can we use this information? Do we need to store depth here?
        transpositionTable.put(board, Pair.of(alpha, depth));

        return alpha;
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
