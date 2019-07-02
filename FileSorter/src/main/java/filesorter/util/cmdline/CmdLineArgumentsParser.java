package filesorter.util.cmdline;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;

import filesorter.application.AppSettings;
import filesorter.application.AppSettingsBuilder;
import filesorter.sort.SortMode;
import filesorter.util.content.ContentType;

/**
 * Represents command-line arguments parser.
 */
public class CmdLineArgumentsParser
{
    public static AppSettings parse(String[] args)
        throws IllegalArgumentException
    {
        if (args.length == 0) {
            printUsage();
            throw new IllegalArgumentException();
        }

        AppSettingsBuilder bAppSettings = new AppSettingsBuilder(); 

        try {
            bAppSettings.setPath(getPathFromCmdLine(args[0]));
        } catch (NotDirectoryException e) {
            throw new IllegalArgumentException();
        }

        for (String arg: args) {
            if (arg.charAt(0) == '-' && arg.charAt(1) == '-') {
                if (arg.length() < 3) {
                    System.err.printf("Option for arg '%s' is missing\n", arg);
                    throw new IllegalArgumentException();
                }

                String[] arg_content = arg.substring(2).split("=");

                if (arg_content.length == 1) {
                    System.err.printf("Value for '%s' option is missing\n", arg_content[0]);
                    throw new IllegalArgumentException();
                } 

                switch (arg_content[0]) {
                    case CmdLineOptions.CT:
                        bAppSettings.setContentType(getContentTypeFromCmdLine(arg_content[1]));
                        break;
                    case CmdLineOptions.SM:
                        bAppSettings.setSortMode(getSortModeFromCmdLine(arg_content[1]));
                        break;
                    case CmdLineOptions.PFX:
                        bAppSettings.setOutPrefix(arg_content[1]);
                        break;
                    case CmdLineOptions.WRK:
                        try {
                            bAppSettings.setNumOfWorkers(Integer.parseInt(arg_content[1]));
                        } catch (NumberFormatException e) {
                            System.err.printf("The value '%s' not in the Integer range value\n",
                                arg_content[1]);
                            throw new IllegalArgumentException();
                        }
                        break;
                    default:
                        System.err.printf("Unknown option '%s'\n", arg_content[0]);
                        throw new IllegalArgumentException();
                }
            }
        }

        return bAppSettings.build();
    }

    private static Path getPathFromCmdLine(String arg)
        throws NotDirectoryException
    {
        Path path = FileSystems.getDefault().getPath(arg);

        if (Files.notExists(path)) {
            System.err.println("Incorrect path specified");
            throw new NotDirectoryException(path.toString());
        }

        return path;
    }

    private static ContentType getContentTypeFromCmdLine(String arg)
        throws IllegalArgumentException
    {
        ContentType ct;

        switch (arg) {
            case CmdLineOptionValues.INT:
                ct = ContentType.INTEGER;
                break;
            case CmdLineOptionValues.STR:
                ct = ContentType.STRING;
                break;
            default:
                System.err.printf("Unknown content type '%s'\n", arg);
                throw new IllegalArgumentException();
        }

        return ct;
    }

    private static SortMode getSortModeFromCmdLine(String arg)
        throws IllegalArgumentException
    {
        SortMode sm;

        switch (arg) {
            case CmdLineOptionValues.ASC:
                sm = SortMode.ASCENDING;
                break;
            case CmdLineOptionValues.DESC:
                sm = SortMode.DESCENDING;
                break;
            default:
                System.err.printf("Unknown sort mode '%s'\n", arg);
                throw new IllegalArgumentException();
        }

        return sm;
    }

    private static void printUsage()
    {
        System.err.println("You need to specify all required parameters; [R] - required, [O] - optional");
        System.err.println(" [R]  <path>                -> path to files to be sorted");
        System.err.println(" [R]  --out-prefix=<pfx>    -> output file name prefix");
        System.err.println(" [R]  --content-type=<type> -> 'i' for INTs or 's' for STRs");
        System.err.println(" [R]  --sort-mode=<mode>    -> 'a' for ASC or 'd' for DESC");
        System.err.println(" [O]  --workers=<n>         -> number of workers (default=2)");
    }
}