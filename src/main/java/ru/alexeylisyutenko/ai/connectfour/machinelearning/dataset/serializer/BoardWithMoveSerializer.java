package ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.serializer;

import ru.alexeylisyutenko.ai.connectfour.machinelearning.dataset.model.BoardWithMove;

public interface BoardWithMoveSerializer {
    byte[] serialize(BoardWithMove boardWithMove);

    BoardWithMove deserialize(byte[] bytes);
}
