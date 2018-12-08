package multithreading;

import httpclient.HttpClientInfo;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Nov 19, 2018 18:13
 */
public class ClientTaskList {
    private Queue<HttpClientInfo> queue;

    private static ClientTaskList ourInstance = null;

    public static ClientTaskList getInstance() {
        if (ourInstance == null) {
            synchronized(ClientTaskList.class) {
                ourInstance = new ClientTaskList();
            }
        }
        return ourInstance;
    }

    private ClientTaskList() {
        queue = new LinkedList<HttpClientInfo>();
    }

    public HttpClientInfo pop() {
         synchronized(ClientTaskList.class) {
             return queue.poll();
         }
    }

    public boolean pull(HttpClientInfo a) {
        synchronized(ClientTaskList.class) {
            return queue.add(a);
        }
    }

    public int getCount() {
        synchronized (ClientTaskList.class) {
            return queue.size();
        }
    }
}
