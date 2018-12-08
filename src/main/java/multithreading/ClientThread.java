package multithreading;

import httpclient.HttpClientInfo;
import httpclient.HttpClientPool;
import httpclient.MultipleHttpClient;
import process.CallMapper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Nov 21, 2018 18:30
 */
public class ClientThread {

    private ExecutorService threadPool;
    private static ClientThread instance = null;

    public static ClientThread getInstance() {
        if (instance == null) {
            synchronized (ClientThread.class) {
                instance = new ClientThread();
            }
        }
        return instance;
    }

    private ClientThread() {
        synchronized (ClientThread.class) {
            threadPool = Executors.newFixedThreadPool(HttpClientPool.getInstance().count() + 1);
        }
    }

    public void shutdown() {
        while (true) {
            if (ClientTaskList.getInstance().getCount() == 0) {
                threadPool.shutdown();
                instance = null;
                break;
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void add(HttpClientInfo client) {
        CallMapper.getInstance().addCall(client.getUuid(), new Exception().getStackTrace()[2].getClassName());
        ClientTaskList.getInstance().pull(client);
        run();
    }

    private void run() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                if (ClientTaskList.getInstance().getCount() > 0) {
                    HttpClientInfo task = ClientTaskList.getInstance().pop();

                    try {
                        Thread.sleep(250 + (int) (Math.random() * 250));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (MultipleHttpClient.client(task)) {
                            ProcessThread.getInstance().add(task);
                        } else {
                            System.err.println("Client to " + task.getRequest().getUrl() + " Fail");
                        }
                    } catch (Exception e) {
                        System.err.println("No request");
                    }

                }
            }
        });
    }



}
