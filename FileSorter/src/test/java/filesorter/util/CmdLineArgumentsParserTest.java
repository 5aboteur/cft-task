package filesorter.util;

import org.junit.Test;

import filesorter.util.cmdline.CmdLineArgumentsParser;

/**
 * Unit tests for CmdLineArgumentsParser.
 */
public class CmdLineArgumentsParserTest 
{
    @Test
    public void testNoArgumentsPassed()
    {
        String[] args = new String[0];

        try {
            CmdLineArgumentsParser.parse(args);
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testNotAllArgumentsPassed()
    {
        String[] args = {
            "/test/things/up",
            "--out-prefix=test"
        };

        try {
            CmdLineArgumentsParser.parse(args);
        } catch (IllegalArgumentException e) {
            
        }
    }

    @Test
    public void testIncorrectPath()
    {
        String[] args = {
            "%_&incorrect/path?",
            "--out-prefix=sorted_",
            "--content-type=i",
            "--sort-mode=a"
        };

        try {
            CmdLineArgumentsParser.parse(args);
        } catch (IllegalArgumentException e) {
            
        }
    }

    @Test
    public void testInvalidOption()
    {
        String[] args = {
            ".",
            "--",
            "--out-prefix=sorted_",
            "--content-type=i",
            "--sort-mode=a"
        };

        try {
            CmdLineArgumentsParser.parse(args);
        } catch (IllegalArgumentException e) {
            
        }
    }

    @Test
    public void testUnknownOption()
    {
        String[] args = {
            ".",
            "--super-option=true",
            "--out-prefix=sorted_",
            "--content-type=i",
            "--sort-mode=a"
        };

        try {
            CmdLineArgumentsParser.parse(args);
        } catch (IllegalArgumentException e) {
            
        }
    }

    @Test
    public void testNoOptionValue()
    {
        String[] args = {
            ".",
            "--out-prefix=sorted_",
            "--content-type=",
            "--sort-mode=a"
        };

        try {
            CmdLineArgumentsParser.parse(args);
        } catch (IllegalArgumentException e) {
            
        }
    }

    @Test
    public void testUnknownContentType()
    {
        String[] args = {
            ".",
            "--out-prefix=sorted_",
            "--content-type=666",
            "--sort-mode=a"
        };

        try {
            CmdLineArgumentsParser.parse(args);
        } catch (IllegalArgumentException e) {
            
        }
    }

    @Test
    public void testUnknownSortMode()
    {
        String[] args = {
            ".",
            "--out-prefix=sorted_",
            "--content-type=i",
            "--sort-mode=aaaaa"
        };

        try {
            CmdLineArgumentsParser.parse(args);
        } catch (IllegalArgumentException e) {
            
        }
    }

    @Test
    public void testIncorrectWorkersNumber()
    {
        String[] args = {
            ".",
            "--out-prefix=sorted_",
            "--content-type=i",
            "--sort-mode=a",
            "--workers=435345353453534535"
        };

        try {
            CmdLineArgumentsParser.parse(args);
        } catch (IllegalArgumentException e) {
            
        }
    }
}
