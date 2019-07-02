package filesorter.sort;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import filesorter.sort.comparators.AscendingSortComparator;
import filesorter.sort.comparators.DescendingSortComparator;
import filesorter.sort.sorters.InsertionSorter;
import filesorter.sort.sorters.Sorter;

/**
 * Unit tests for InsertionSorter.
 */
public class InsertionSorterTest
{
    @Test
    public void testSortIntegersAscending()
    {
        Sorter sorter = new InsertionSorter();
        Integer[] array = { 8, 8, 4, 1 };

        sorter.sort(array, new AscendingSortComparator());

        assertArrayEquals(new Integer[] { 1, 4, 8, 8 }, array);
    }

    @Test
    public void testSortIntegersDescending()
    {
        Sorter sorter = new InsertionSorter();
        Integer[] array = { 1, 3, 3, 7};

        sorter.sort(array, new DescendingSortComparator());

        assertArrayEquals(new Integer[] { 7, 3, 3, 1 }, array);
    }

    @Test
    public void testSortStringsAscending()
    {
        Sorter sorter = new InsertionSorter();
        String[] array = { "80085", "091", "455", "7", "1", "109131011391", "80711", "1" };

        sorter.sort(array, new AscendingSortComparator());

        assertArrayEquals(new String[] { "091", "1", "1", "109131011391", "455", "7", "80085", "80711" }, array);
    }

    @Test
    public void testSortStringsDescending()
    {
        Sorter sorter = new InsertionSorter();
        String[] array = { "c'mon", "just", "TesT", "meOut", "TesTer", ">:(" };

        sorter.sort(array, new DescendingSortComparator());

        assertArrayEquals(new String[] { "meOut", "just", "c'mon", "TesTer", "TesT", ">:(" }, array);
    }
}