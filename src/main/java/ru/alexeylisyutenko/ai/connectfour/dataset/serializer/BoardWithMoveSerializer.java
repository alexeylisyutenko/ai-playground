package ru.alexeylisyutenko.ai.connectfour.dataset.serializer;

import ru.alexeylisyutenko.ai.connectfour.dataset.model.BoardWithMove;

public interface BoardWithMoveSerializer {
    byte[] serialize(BoardWithMove boardWithMove);
}
