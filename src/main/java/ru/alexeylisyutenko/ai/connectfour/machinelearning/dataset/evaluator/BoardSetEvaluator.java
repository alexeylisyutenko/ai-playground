package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.evaluator;

import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.game.Board;

import java.util.List;
import java.util.Set;

public interface BoardSetEvaluator {
    List<BoardWithMove> evaluate(Set<Board> boards, String boardSetName);
}
