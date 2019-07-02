package filesorter.application;

import filesorter.sort.comparators.AscendingSortComparator;
import filesorter.sort.comparators.DescendingSortComparator;
import filesorter.sort.comparators.SortComparator;
import filesorter.sort.sorters.InsertionSorter;
import filesorter.sort.sorters.Sorter;
import filesorter.util.auxiliary.DataQueue;
import filesorter.util.auxiliary.FileContentPair;
import filesorter.util.cmdline.CmdLineArgumentsParser;
import filesorter.util.content.ContentLoader;
import filesorter.util.content.ContentSaver;
import filesorter.util.content.ContentType;

/**
 * Notes:

 *  This application was made just 4 lulz.. and for job competition too :)
 *  OOP wannabe coder since 22.06.2019 *thinking*
 * 
 * 
 * Description:

 *  The application workflow is divided into three main stages:
 *   1) First, the ContentLoader loads all the data from files in a given path
 *      and posts raw unsorted data into the input queue.
 *   2) Then workers (a.k.a. sorters) sort data in parallel and post result into
 *      the output queue.
 *   3) And finally, ContentSaver grabs all sorted data from output queue and
 *      puts it into new created files.
 *
 *  The following diagram shows the process described:
 * 
 *            inp  out
 *          ->+-+  +-+
 *   (1)   /  | |  | | \   (3)
 *  +-----+   | |  | |  ->+-----+
 *  | C L |   | |  | |    | C S |
 *  +-----+   | |  | |    +-----+
 *    ^ ^     | |  | |      | |
 *    | |     | |  | |      v v
 *   {raw}    +-+  +-+   {sorted}
 *               \/ 
 *          s o r t e r s
 *               (2)
 * 
 * 
 * @author  5aboteur
 * @version  2.0
 */
public class App
{
    private static final ContentLoader contentLoader;
    private static final ContentSaver contentSaver;

    private static final DataQueue inputQ;
    private static final DataQueue outputQ;

    // A flag that signals sorters to stop their job immediately
    private volatile static Boolean shutdown = false;
    public static Boolean isShutdown() { return shutdown; }

    static {
        inputQ = new DataQueue();
        outputQ = new DataQueue();
        contentLoader = new ContentLoader(inputQ);
        contentSaver = new ContentSaver(outputQ);
    }

    public static void main(String[] args)
    {
        System.out.println("\n\t*** CFT TASK ***\n");
        try {
            // Parse command-line arguments and fill the application settings
            AppSettings settings = CmdLineArgumentsParser.parse(args);
            System.out.println(settings.toString());

            SortComparator comparator;

            switch (settings.getSortMode())
            {
                case DESCENDING:
                    comparator = new DescendingSortComparator();
                    break;
                case ASCENDING:
                default:
                    comparator = new AscendingSortComparator();
                    break;
            }

            for (int i = 0; i < settings.getNumOfWorkers(); i++) {
                new Thread(() -> {
                    String tName = Thread.currentThread().getName();
                    System.out.printf("'%s' awakened\n", tName);

                    FileContentPair pair;
                    Sorter sorter = new InsertionSorter();

                    while (!isShutdown()) {
                        try {
                            // Wait until some unsorted data will appear in the queue
                            if ((pair = inputQ.pop()) != null) {
                                System.out.printf("'%s' takes [%s]\n", tName, pair.getFileName());

                                // Determine the type of data
                                if (settings.getContentType() == ContentType.INTEGER) {
                                    Integer[] content = pair.getContentInIntegerRepr();
                                    sorter.sort(content, comparator);
                                    pair.setContentFromIntegerRepr(content);
                                } else {
                                    sorter.sort(pair.getRawContent(), comparator);
                                }

                                outputQ.push(pair);
                            }
                        } catch (Exception e) {
                            System.err.printf("An error in '%s', print stack trace...\n", tName);
                            e.printStackTrace();
                        }
                    }
                    System.out.printf("'%s' stopped\n", tName);
                }).start();
            }

            // Process files
            int filesToProcess = contentLoader.loadFrom(settings.getPath());
            contentSaver.saveToWithPrefix(settings.getPath(), settings.getOutPrefix(), filesToProcess);

        } catch (Exception e) {
            System.err.println("An error occurred, stop the execution");
        } finally {
            // Stop multithreaded environment
            shutdown = true;
        }
    }
}
