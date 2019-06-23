package filesorter;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This application was made just 4 lulz.. and for job competition too :)
 * OOP wannabe coder since 22.06.2019 *thinking*
 * 
 * @author  5aboteur
 */
public class App {
    /**
     * Inner class, that keeps all application parameters from the command-line.
     */
    static class AppSettings {
        private final int numberOfWorkers = 8;
        private Integer contentType;
        private Integer sortMode;
        private String outPrefix;
        private Path path;

        public int getNumberOfWorkers() { return numberOfWorkers; }
        public Integer getContentType() { return contentType; }
        public Integer getSortMode() { return sortMode; }
        public String getOutPrefix() { return outPrefix; }
        public Path getPath() { return path; }

        public void printSettings() {
            System.out.println("Number of workers: " + numberOfWorkers);
            System.out.printf("Content type: %s\n",
                contentType == ContentType.INTEGER_CT ? "INT" :
                contentType == ContentType.STRING_CT ? "STR" : "UNKNOWN");
            System.out.printf("Sort mode: %s\n",
                sortMode == SortMode.ASCENDING_SM ? "ASC" :
                sortMode == SortMode.DESCENDING_SM ? "DESC" : "UNKNOWN");
            System.out.println("Out prefix: " + outPrefix);
            System.out.println("Path: " + path.toString());
        }
    }

    public static class ContentType {
        public static final int INTEGER_CT = 0;
        public static final int STRING_CT = 1;
        public static final int UNKNOWN_CT = -1;
    }

    public static class SortMode {
        public static final int ASCENDING_SM = 0;
        public static final int DESCENDING_SM = 1;
        public static final int UNKNOWN_SM = -1;
    }

    private static ExecutorService executor;

    private static Boolean releaseWorkers = false;
    public static Boolean isReleaseWorkers() { return releaseWorkers; }

    /*
     * In this application shared queues are used in a pipeline-like way. When we put 
     * raw unsorted data from files into the 'inputQ' queue, workers start sorting
     * things. After completion, they insert sorted data into the 'outputQ' and
     * after that all sorted data will be put into resulting files.
     */
    private static ConcurrentLinkedQueue<Map.Entry<String, List<String>>> inputQ;
    private static ConcurrentLinkedQueue<Map.Entry<String, List<String>>> outputQ;

    public static void inputQueuePush(Map.Entry<String, List<String>> val) { inputQ.add(val); }
    public static void outputQueuePush(Map.Entry<String, List<String>> val) { outputQ.add(val); }
    public static Map.Entry<String, List<String>> inputQueuePop() { return inputQ.poll(); }
    public static Map.Entry<String, List<String>> outputQueuePop() { return outputQ.poll(); }

    private static AppSettings settings;
    public static AppSettings getAppSettings() { return settings; }

    public static void main(String[] args) throws Exception {
        System.out.println("\n\t*** CFT TASK ***\n");
        try {
            settings = new AppSettings();

            // Parsing command-line arguments
            cmdLineArgsParse(args, settings);

            settings.printSettings();

            // Init shared queues & threadpool
            inputQ = new ConcurrentLinkedQueue<Map.Entry<String, List<String>>>();
            outputQ = new ConcurrentLinkedQueue<Map.Entry<String, List<String>>>();
            executor = Executors.newFixedThreadPool(settings.numberOfWorkers);

            for (int i = 0; i < settings.numberOfWorkers; i++) {
                Sorter sorter = new Sorter("worker #" + String.valueOf(i + 1));
                executor.execute(sorter);
            }

            // Process files
            ContentManager contentManager = new ContentManager();
            contentManager.load();
            contentManager.save();

        } catch (Exception e) {
            System.err.println("An error occurred, stop the execution");
        } finally {
            // Stop multithreaded environment
            releaseWorkers = true;
            if (executor != null) {
                executor.shutdown();
            }
        }
    }

    /** 
     * Parses the command-line arguments and fills the application settings.
     */
    public static void cmdLineArgsParse(String[] args, AppSettings settings)
        throws IllegalArgumentException, NotDirectoryException {
        if (args.length < 4) {
            System.err.printf("You have to specify at least 4 parameters:\n");
            System.err.printf("\t<path>\n\t--out-prefix=<pfx>\n\t--content-type=<type>\n\t--sort-mode=<mode>\n");
            throw new IllegalArgumentException();
        }

        settings.path = FileSystems.getDefault().getPath(args[0]);
        if (Files.notExists(settings.path)) {
            System.err.println("Incorrect path specified");
            throw new NotDirectoryException(settings.path.toString());
        }

        for (int i = 1; i < args.length; i++) {
            String arg = args[i];

            if (arg.charAt(0) == '-' && arg.charAt(1) == '-') {
                if (arg.length() < 3) {
                    System.err.printf("Option for #%d arg is missing\n", i, arg);
                    throw new IllegalArgumentException();
                }

                String[] arg_content = arg.substring(2).split("=");

                if (arg_content.length == 1) {
                    System.err.printf("Value for '%s' option is missing\n", arg_content[0]);
                    throw new IllegalArgumentException();
                } 

                switch (arg_content[0]) {
                    case "content-type":
                        switch (arg_content[1]) {
                            case "i": settings.contentType = ContentType.INTEGER_CT; break;
                            case "s": settings.contentType = ContentType.STRING_CT; break;
                            default:
                                System.err.printf("Unknown content type '%s'\n", arg_content[1]);
                                throw new IllegalArgumentException();
                        }
                        break;
                    case "sort-mode":
                        switch (arg_content[1]) {
                            case "a": settings.sortMode = SortMode.ASCENDING_SM; break;
                            case "d": settings.sortMode = SortMode.DESCENDING_SM; break;
                            default:
                                System.err.printf("Unknown sort mode '%s'\n", arg_content[1]);
                                throw new IllegalArgumentException();
                        }
                        break;
                    case "out-prefix":
                        settings.outPrefix = arg_content[1];
                        break;
                    default:
                        System.err.printf("Unknown option '%s'\n", arg_content[0]);
                        throw new IllegalArgumentException();
                }
            }
        }
    }
}
