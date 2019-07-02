package filesorter.util.auxiliary;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Shared queue wrapper.
 */
public class DataQueue
{
    private ConcurrentLinkedQueue<FileContentPair> queue;

    public DataQueue()
    {
        queue = new ConcurrentLinkedQueue<FileContentPair>();
    }

    public void push(FileContentPair val) { queue.add(val); }
    public FileContentPair pop() { return queue.poll(); }
}