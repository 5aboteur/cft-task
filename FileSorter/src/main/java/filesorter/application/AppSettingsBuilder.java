package filesorter.application;

import java.nio.file.Path;

import filesorter.util.content.ContentType;
import filesorter.sort.SortMode;

/**
 * Because application settings are immutable, we need this auxiliary class to
 * get all the parameters alternately, before we build the AppSettings instance.
 */
public class AppSettingsBuilder
{
    private Path path;
    private String outPrefix;
    private ContentType contentType;
    private SortMode sortMode;
    private int numOfWorkers;

    public Path getPath() { return path; }
    public String getOutPrefix() { return outPrefix; }
    public ContentType getContentType() { return contentType; }
    public SortMode getSortMode() { return sortMode; }
    public int getNumOfWorkers() { return numOfWorkers; }

    public void setPath(Path path) { this.path = path; }
    public void setOutPrefix(String pfx) { this.outPrefix = pfx; }
    public void setContentType(ContentType ct) { this.contentType = ct; }
    public void setSortMode(SortMode sm) { this.sortMode = sm; }
    public void setNumOfWorkers(int wrk) { this.numOfWorkers = wrk; }

    public AppSettings build() {
        return new AppSettings(path, outPrefix, contentType, sortMode, numOfWorkers);
    }
}