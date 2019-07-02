package filesorter.sort.sorters;

import filesorter.sort.comparators.SortComparator;

/**
 * Represents sorter objects.
 */
public abstract interface Sorter
{
    public abstract <T extends Comparable<T>> void sort(T[] data, SortComparator cmp);
}