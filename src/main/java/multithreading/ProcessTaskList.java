package multithreading;

import httpclient.HttpClientInfo;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Nov 21, 2018 18:31
 */
public class ProcessTaskList {
    private Queue<HttpClientInfo> queue;

    private static ProcessTaskList ourInstance = null;

    public static ProcessTaskList getInstance() {
        if (ourInstance == null) {
            synchronized(ProcessTaskList.class) {
                ourInstance = new ProcessTaskList();
            }
        }
        return ourInstance;
    }

    private ProcessTaskList() {
        queue = new LinkedList<HttpClientInfo>();
    }

    public HttpClientInfo pop() {
        synchronized(ProcessTaskList.class) {
            return queue.poll();
        }
    }

    public boolean pull(HttpClientInfo a) {
        synchronized(ProcessTaskList.class) {
            return queue.add(a);
        }
    }

    public int getCount() {
        synchronized (ProcessTaskList.class) {
            return queue.size();
        }
    }
}
