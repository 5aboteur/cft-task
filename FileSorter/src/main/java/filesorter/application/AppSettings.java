package filesorter.application;

import java.nio.file.Path;

import filesorter.util.content.ContentType;
import filesorter.sort.SortMode;

/**
 * Storage class for application parameters taken from the command-line.
 */
public class AppSettings
{
    // Default number of worker threads
    private final int DEFAULT_WORKERS_NUMBER = 2;

    private final Path path;
    private final String outPrefix;
    private final ContentType contentType;
    private final SortMode sortMode;
    private final int numOfWorkers;

    public AppSettings(Path path, String pfx, ContentType ct, SortMode sm, int wrk)
    {
        if (path == null || pfx == null || ct == null || sm == null)
        {
            System.err.println("Required parameters must not be null");
            throw new IllegalArgumentException();
        }

        this.path = path;
        this.outPrefix = pfx;
        this.contentType = ct;
        this.sortMode = sm;
        this.numOfWorkers = (wrk > 0 ? wrk : DEFAULT_WORKERS_NUMBER);
    }

    public int getDefaultWorkersNum() { return DEFAULT_WORKERS_NUMBER; }
    public Path getPath() { return path; }
    public String getOutPrefix() { return outPrefix; }
    public ContentType getContentType() { return contentType; }
    public SortMode getSortMode() { return sortMode; }
    public int getNumOfWorkers() { return numOfWorkers; }

    public String toString()
    {
        String ct = (
            contentType == ContentType.INTEGER ? "INT" :
            contentType == ContentType.STRING ? "STR" : "UNKNOWN"
        );
        String sm = (
            sortMode == SortMode.ASCENDING ? "ASC" :
            sortMode == SortMode.DESCENDING ? "DESC" : "UNKNOWN"
        );
        return getClass().getName() + " {\n"
            + " Path: " + path.toString() + "\n"
            + " Out prefix: " + outPrefix + "\n"
            + " Content type: " + ct + "\n"
            + " Sort mode: " + sm + "\n"
            + " Number of workers: " + numOfWorkers + "\n"
            + "}\n";
    }
}