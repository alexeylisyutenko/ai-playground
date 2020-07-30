package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class SortUtils {
    private SortUtils() {
    }

    /**
     * Find k smallest elements in the collection.
     * <br>
     * Resulting list is not sorted.
     *
     * @param collection collection where we're looking smallest elements at
     * @param comparator comparator for collection's element type
     * @param k number of smallest elements
     * @param <T> type of elements
     * @return list of k smallest elements
     */
    public static <T> List<T> kSmallest(Collection<T> collection, Comparator<T> comparator, int k) {
        Objects.requireNonNull(collection, "collection cannot be null");
        Objects.requireNonNull(comparator, "comparator cannot be null");
        if (k < 1 || k > collection.size()) {
            throw new IllegalArgumentException("Illegal k value");
        }

        QuickSelector<T> quickSelector = new QuickSelector<>(collection, comparator);
        return quickSelector.kSmallest(k);
    }

    /**
     * Find k smallest elements in the collection.
     * <br>
     * Resulting list is not sorted.
     *
     * @param collection collection where we're looking smallest elements at
     * @param k number of smallest elements
     * @param <T> type of elements
     * @return list of k smallest elements
     */
    public static <T extends Comparable<? super T>> List<T> kSmallest(Collection<T> collection, int k) {
        return kSmallest(collection, Comparator.comparing(Function.identity()), k);
    }
}
