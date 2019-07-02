package filesorter.util.content;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import filesorter.util.auxiliary.DataQueue;
import filesorter.util.auxiliary.FileContentPair;

/**
 * Loads content from files.
 */
public class ContentLoader
{
    private final DataQueue queue;

    public ContentLoader(DataQueue queue)
    {
        this.queue = queue;
    }

    /**
     * Loads all unsorted data from files in a given path and posts it into the
     * input queue with [FILE <=> CONTENT] relation.
     */
    public int loadFrom(Path path) throws IOException
    {
        int loadedFiles = 0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry: stream) {
                String filename = entry.toFile().getName();
                List<String> lines = Files.readAllLines(entry, StandardCharsets.UTF_8);
                FileContentPair pair =
                    new FileContentPair(filename, lines.toArray(new String[lines.size()]));
                
                // Send for processing
                queue.push(pair);
                loadedFiles++;
            }
        }
        return loadedFiles;
    }
}