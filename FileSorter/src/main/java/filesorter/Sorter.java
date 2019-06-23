package filesorter;

import java.util.List;
import java.util.Map;

/**
 * Represents a worker who sort things.
 */
public class Sorter implements Runnable {
    private String workerName;

    Sorter(String name) {
        workerName = name;
    }

    public void run() {
        System.out.println("'" + workerName + "' awakened");

        Map.Entry<String, List<String>> content;

        while (!App.isReleaseWorkers()) {
            try {
                // Wait until content loader post unsorted data to the queue
                if ((content = App.inputQueuePop()) != null) {
                    System.out.printf("'%s' takes [%s]\n", workerName, content.getKey());

                    // Sort content & post result to the output queue
                    sort(content.getValue());
                    App.outputQueuePush(content);
                }
            } catch (Exception e) {
                System.err.printf("An error in '%s', print stack trace...\n", workerName);
                e.printStackTrace();
            }
        }

        System.out.println("'" + workerName + "' stopped");
    }

    /**
     * An implementation of insertion sort.
     */
    public void sort(List<String> content) {
        for (int i = 1; i < content.size(); i++) {
            String key = content.get(i);
            int j = i - 1;

            while (j >= 0 && cmp(content.get(j), key)) {
                content.set(j + 1, content.get(j));
                j--;
            }

            content.set(j + 1, key);
        }
    }

    /**
     * Compares objects. Our application keeps data in string representation,
     * but if we want to sort the other types (e.g.: int), we should cast data
     * to appropriate one.
     */
    public boolean cmp(String obj1, String obj2) {
        int diff = 0;
        switch (App.getAppSettings().getContentType()) {
            case App.ContentType.INTEGER_CT:
                Integer i1 = Integer.parseInt(obj1);
                Integer i2 = Integer.parseInt(obj2);
                diff = i1.compareTo(i2);
                break;
            case App.ContentType.STRING_CT:  
                diff = obj1.compareTo(obj2);
                break;            
            default: // should never happen
                break;
        }
        return App.getAppSettings().getSortMode() == App.SortMode.DESCENDING_SM ? diff < 0 : diff > 0;
    }
}