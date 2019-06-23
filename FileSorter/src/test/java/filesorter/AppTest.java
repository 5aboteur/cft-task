package filesorter;

import java.nio.file.NotDirectoryException;

import org.junit.Test;

/**
 * Unit tests for App.
 */
public class AppTest 
{
    @Test
    public void testNoArgumentsPassed()
    {
        App.AppSettings settings = new App.AppSettings();
        String[] args = new String[0];

        try {
            App.cmdLineArgsParse(args, settings);
        } catch (IllegalArgumentException | NotDirectoryException e) {

        }
    }

    @Test
    public void testNotAllArgumentsPassed()
    {
        App.AppSettings settings = new App.AppSettings();
        String[] args = {
            "/test/things/up",
            "--out-prefix=test"
        };

        try {
            App.cmdLineArgsParse(args, settings);
        } catch (IllegalArgumentException | NotDirectoryException e) {
            
        }
    }

    @Test
    public void testIncorrectPath()
    {
        App.AppSettings settings = new App.AppSettings();
        String[] args = {
            "%_&incorrect/path?",
            "--out-prefix=sorted_",
            "--content-type=i",
            "--sort-mode=a"
        };

        try {
            App.cmdLineArgsParse(args, settings);
        } catch (IllegalArgumentException | NotDirectoryException e) {
            
        }
    }

    @Test
    public void testInvalidOption()
    {
        App.AppSettings settings = new App.AppSettings();
        String[] args = {
            ".",
            "--",
            "--out-prefix=sorted_",
            "--content-type=i",
            "--sort-mode=a"
        };

        try {
            App.cmdLineArgsParse(args, settings);
        } catch (IllegalArgumentException | NotDirectoryException e) {
            
        }
    }

    @Test
    public void testUnknownOption()
    {
        App.AppSettings settings = new App.AppSettings();
        String[] args = {
            ".",
            "--super-option=true",
            "--out-prefix=sorted_",
            "--content-type=i",
            "--sort-mode=a"
        };

        try {
            App.cmdLineArgsParse(args, settings);
        } catch (IllegalArgumentException | NotDirectoryException e) {
            
        }
    }

    @Test
    public void testNoOptionValue()
    {
        App.AppSettings settings = new App.AppSettings();
        String[] args = {
            ".",
            "--out-prefix=sorted_",
            "--content-type=",
            "--sort-mode=a"
        };

        try {
            App.cmdLineArgsParse(args, settings);
        } catch (IllegalArgumentException | NotDirectoryException e) {
            
        }
    }

    @Test
    public void testUnknownContentType()
    {
        App.AppSettings settings = new App.AppSettings();
        String[] args = {
            ".",
            "--out-prefix=sorted_",
            "--content-type=666",
            "--sort-mode=a"
        };

        try {
            App.cmdLineArgsParse(args, settings);
        } catch (IllegalArgumentException | NotDirectoryException e) {
            
        }
    }

    @Test
    public void testUnknownSortMode()
    {
        App.AppSettings settings = new App.AppSettings();
        String[] args = {
            ".",
            "--out-prefix=sorted_",
            "--content-type=i",
            "--sort-mode=aaaaa"
        };

        try {
            App.cmdLineArgsParse(args, settings);
        } catch (IllegalArgumentException | NotDirectoryException e) {
            
        }
    }
}
