package ru.alexeylisyutenko.ai.connectfour.dataset.defaultdataset;

import ru.alexeylisyutenko.ai.connectfour.dataset.model.BoardWithMove;
import ru.alexeylisyutenko.ai.connectfour.dataset.serializer.DefaultBoardWithMoveSerializer;
import ru.alexeylisyutenko.ai.connectfour.exception.ConnectFourDatasetException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static ru.alexeylisyutenko.ai.connectfour.dataset.serializer.DefaultBoardWithMoveSerializer.RECORD_SIZE;

public class ConnectFourDatasetLoader {
    public Set<BoardWithMove> load(String fileName) {
        Set<BoardWithMove> dataset = new HashSet<>();
        DefaultBoardWithMoveSerializer boardWithMoveSerializer = new DefaultBoardWithMoveSerializer();
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            while (true) {
                byte[] bytes = fileInputStream.readNBytes(RECORD_SIZE);
                if (bytes.length == 0) {
                    break;
                }
                BoardWithMove boardWithMove = boardWithMoveSerializer.deserialize(bytes);
                dataset.add(boardWithMove);
            }
        } catch (IOException e) {
            throw new ConnectFourDatasetException(String.format("Failed to load dataset from file '%s': %s", fileName, e.getMessage()));
        }
        return dataset;
    }
}
