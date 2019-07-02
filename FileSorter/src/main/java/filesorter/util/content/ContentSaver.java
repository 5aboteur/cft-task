package filesorter.util.content;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import filesorter.util.auxiliary.DataQueue;
import filesorter.util.auxiliary.FileContentPair;

/**
 * Saves processed content to files.
 */
public class ContentSaver
{
    // Default timeout before next attempt to get some data from the queue
    private final int WRK_TIMEOUT = 200;
    private final DataQueue queue;

    public ContentSaver(DataQueue queue)
    {
        this.queue = queue;
    }

    /**
     * Gets all sorted data from the output queue and write it into the brand new
     * files with given 'out-prefix' value for their names.
     */
    public void saveToWithPrefix(Path path, String prefix, int filesToSave)
        throws Exception
    {
        FileContentPair pair;
        for (int i = 0; i < filesToSave; i++) {
            // Wait until workers post some data to the queue
            int attempts = 0;
            while ((pair = queue.pop()) == null) {
                if (attempts >= 5) {
                    System.err.println("Something wrong with the threadpool");
                    throw new Exception();
                }
                System.out.println("No data in the output queue.");
                Thread.sleep(WRK_TIMEOUT);
                attempts++;
            }

            String fileName = path + File.separator + prefix + pair.getFileName();
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));

            try {
                for (Object s: pair.getRawContent()) {
                    bw.write(s.toString());
                    bw.newLine();
                }    
            } catch (IOException e) {
                System.err.printf("Error writing to '%s'\n", fileName);
                e.printStackTrace();
            } finally {
                bw.close();
            }

            System.out.printf("%d/%d files processed\n", i + 1, filesToSave);
        }
    }
}
