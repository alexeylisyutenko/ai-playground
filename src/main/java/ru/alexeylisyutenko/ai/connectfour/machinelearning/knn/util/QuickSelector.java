package ru.alexeylisyutenko.ai.connectfour.machinelearning.knn.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// TODO: Implement and test.
public class QuickSelector<T> {
    private final ArrayList<T> arrayList;
    private final Comparator<T> comparator;

    public QuickSelector(List<T> list, Comparator<T> comparator)  {
        this.arrayList = new ArrayList<>(list);
        this.comparator = comparator;
    }

    public List<T> kSmallest(int k) {
        partialSort(0, arrayList.size() - 1, k);
        return arrayList.subList(0, k);
    }

    private void partialSort(int lo, int hi, int index) {
        while (true) {
            if (lo == hi) {
                return;
            }
            int q = partition(lo, hi);
            int k = q - lo + 1;
            if (k == index) {
                return;
            }
            if (index < k) {
                hi = q - 1;
            } else {
                lo = q + 1;
                index -= k;
            }
        }
    }

    private int partition(int lo, int hi) {
        T pivot = arrayList.get(hi);
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (comparator.compare(arrayList.get(j), pivot) <= 0) {
                i++;
                exchange(i, j);
            }
        }
        exchange(i + 1, hi);
        return i + 1;
    }

    private void exchange(int firstIndex, int secondIndex) {
        T temp = arrayList.get(firstIndex);
        arrayList.set(firstIndex, arrayList.get(secondIndex));
        arrayList.set(secondIndex, temp);
    }
}
