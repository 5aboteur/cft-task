package filesorter.sort.comparators;

public abstract interface SortComparator
{
    public abstract <T extends Comparable<T>> int compare(T arg0, T arg1);
}