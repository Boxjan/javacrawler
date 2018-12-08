package crawler;

import httpclient.HttpClientInfo;
import multithreading.ClientThread;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Nov 27, 2018 11:52
 */
public abstract class Crawler {

    protected void addToClient(HttpClientInfo info) {
        ClientThread.getInstance().add(info);
    }

    abstract public void processResponse(HttpClientInfo info);

}
