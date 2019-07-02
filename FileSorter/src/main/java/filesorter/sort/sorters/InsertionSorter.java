package filesorter.sort.sorters;

import filesorter.sort.comparators.SortComparator;

/**
 * The following sorter sorts data with Insertion Sort algorithm.
 */
public class InsertionSorter implements Sorter
{
    @Override
    public <T extends Comparable<T>> void sort(T[] data, SortComparator cmp)
    {
        if (data == null || data.length <= 1) {
            System.out.println("Nothing to sort");
            return;
        }

        for (int i = 1; i < data.length; i++) {
            T key = data[i];
            int j = i - 1;

            while (j >= 0 && cmp.compare(data[j], key) > 0) {
                data[j + 1] = data[j];
                j--;
            }

            data[j + 1] = key;
        }
    }
}