package multithreading;

import crawler.*;
import httpclient.HttpClientInfo;
import process.CallMapper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Nov 27, 2018 12:04
 */
public class ProcessThread {

    private ExecutorService threadPool;
    private static ProcessThread instance = null;

    public static ProcessThread getInstance() {

        if (instance == null) {
            synchronized (ProcessThread.class) {
                instance = new ProcessThread();
            }
        }
        return instance;
    }

    private ProcessThread() {
        synchronized (ProcessThread.class) {
            threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2 );
        }
    }

    public void shutdown() {
        while (true) {
            if (ProcessTaskList.getInstance().getCount() == 0 && ClientTaskList.getInstance().getCount() == 0) {
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

    public void add(HttpClientInfo clientInfo) {
        ProcessTaskList.getInstance().pull(clientInfo);
        run();
    }

    private void run() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                if (ProcessTaskList.getInstance().getCount() > 0) {
                    HttpClientInfo task = ProcessTaskList.getInstance().pop();

                    String className = CallMapper.getInstance().getCall(task.getUuid());
                    Class c;
                    Crawler process;
                    try {
                        c = Class.forName(className);
                        process = (Crawler) c.newInstance();
                        process.processResponse(task);
                    } catch (ClassNotFoundException classNotFound) {
                        System.err.println("No class can be get");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        });
    }

}
