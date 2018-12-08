import crawler.BilibiliUserInfoCrawler;
import multithreading.ClientThread;
import multithreading.ProcessThread;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Nov 01, 2018 20:44
 */

public class main {

    public static void main(String args[]) {


        for (int i = 1; i <= 100 ; i++ ) { //400000000
            try {
                BilibiliUserInfoCrawler.get(i);
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }

        ClientThread.getInstance().shutdown();
        ProcessThread.getInstance().shutdown();

    }

}

