package filesorter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

/**
 * Manager of files and their internal content.
 */
public class ContentManager {
    private int filesToManageCounter;

    ContentManager() {
        filesToManageCounter = 0;
    }

    private String getAbsoluteFileName(String relativeFileName) {
        return Paths.get(App.getAppSettings().getPath() + File.separator + relativeFileName).toString();
    }

    /**
     * Loads all data from given path into 'inputQ' with [FILE_NAME] <=> [CONTENT] relation.
     */
    public void load() throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(App.getAppSettings().getPath())) {
            for (Path entry: stream) {
                String filename = entry.toFile().getName();
                List<String> lines = Files.readAllLines(entry, StandardCharsets.UTF_8);
                
                Map.Entry<String, List<String>> fileContent =
                    new AbstractMap.SimpleEntry<String, List<String>>(filename, lines);
                
                // Send for processing
                App.inputQueuePush(fileContent);
                filesToManageCounter++;
            }
        }
    }

    /**
     * Saves all sorted data from 'outputQ' into new files.
     */
    public void save() throws Exception {
        Map.Entry<String, List<String>> fileContent;
        for (int i = 0; i < filesToManageCounter; i++) {
            // Wait until workers post some data to the queue
            int attempts = 0;
            while ((fileContent = App.outputQueuePop()) == null) {
                if (attempts >= 5) {
                    System.err.println("Something wrong with the threadpool");
                    throw new Exception();
                }
                Thread.sleep(200);
                attempts++;
            }

            String sortedFileName = App.getAppSettings().getOutPrefix() + fileContent.getKey();
            String fileName = getAbsoluteFileName(sortedFileName);
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));

            try {
                for (Object s: fileContent.getValue()) {
                    bw.write(s.toString());
                    bw.newLine();
                }    
            } catch (IOException e) {
                System.err.printf("Error writing to '%s'\n", fileName);
                e.printStackTrace();
            } finally {
                bw.close();
            }

            System.out.printf("%d/%d files processed\n", i + 1, filesToManageCounter);
        }
    }
}
