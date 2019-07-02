package filesorter.util;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import filesorter.util.auxiliary.FileContentPair;

/**
 * Unit tests for FileContentPair.
 */
public class FileContentPairTest 
{
    @Test
    public void testTryGetContentInIntegerReprBad()
    {
        FileContentPair pair =
            new FileContentPair("test.txt", new String[] { "d33z", "ni99az", "ea71n", "b34nz" });

        try {
            pair.getContentInIntegerRepr();
        } catch(NumberFormatException e) {

        }
    }

    @Test
    public void testTryGetContentInIntegerReprGood()
    {
        Integer[] integers;
        Integer[] expected = new Integer[] { 33, 99, 71, 34 };
        FileContentPair pair =
            new FileContentPair("test.txt", new String[] { "33", "99", "71", "34" });

        try {
            integers = pair.getContentInIntegerRepr();
            assertArrayEquals(expected, integers);
        } catch(NumberFormatException e) {

        }
    }
}