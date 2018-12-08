package httpclient;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Nov 26, 2018 13:55
 */
public class ProxyInfo {

    private String address;
    private int port;
    private String type;
    private String username;
    private String password;

    public ProxyInfo(String address, int port, String type, String username, String password) {
        this.address = address;
        this.port = port;
        this.type = type;
        this.username = username;
        this.password = password;
    }

    public ProxyInfo(String address, int port, String type) {
        this.address = address;
        this.port = port;
        this.type = type;
        this.username = null;
        this.password = null;
    }

    public static ProxyInfo build(String address, int port, String type, String username, String password) {
        return new ProxyInfo(address, port, type, username, password);
    }

    public static ProxyInfo build(String address, int port, String type) {
        return new ProxyInfo(address, port, type);
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getType() {
        return type.toLowerCase();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Type: " + type + " Address: " + address + " Port: " + port;
    }
}
