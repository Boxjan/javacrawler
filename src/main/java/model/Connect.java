package model;

import io.github.biezhi.anima.Anima;

import java.io.InputStream;
import java.util.Properties;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Dec 03, 2018 16:54
 */
public class Connect {

    private static Connect connect = new Connect();
    private static Anima anima;
    private static boolean isconnect = false;

    private Connect() {
        synchronized (Connect.class) {
            if(isconnect) return;
            try {
                InputStream inStream = Connect.class.getResourceAsStream("../config.properties");
                Properties properties = new Properties();
                properties.load(inStream);
                String jdbcUrl = properties.getProperty("jdbc-url");
                String username = properties.getProperty("jdbc-username");
                String password = properties.getProperty("jdbc-password");
                anima = Anima.open(jdbcUrl, username, password);
            } catch (Exception e) {
                System.err.println("Can not connect to Database");
                System.exit(1);
            }
        }
    }

    public static Anima getConnect() {
        return anima;
    }
}
