package httpclient;

import com.google.gson.Gson;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Nov 26, 2018 13:27
 */

public class HttpClientPool {
    private List<HttpClient> clientPool;
    private ProxyInfo[] proxyList;
    private int last;

    private static HttpClientPool instance = new HttpClientPool();

    public static HttpClientPool getInstance() {
        return instance;
    }

    private HttpClientPool() {
        synchronized (HttpClientPool.class) {
            last = 0;
            clientPool = new ArrayList<HttpClient>();
            try {
                HttpClientBuilder directBuilder = getBuilder();
                clientPool.add(directBuilder.build());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            try {
                InputStream inStream = HttpClientPool.class.getResourceAsStream("../remote.properties");
                Properties properties = new Properties();
                properties.load(inStream);
                proxyList = new Gson().fromJson(properties.getProperty("proxy"), ProxyInfo[].class);

                for (ProxyInfo aProxy : proxyList) {

                    if (aProxy.getType().equals("socks")) {

                        HttpClientBuilder socksProxyBuilder = getBuilder(new InetSocketAddress(aProxy.getAddress(), aProxy.getPort()));
                        HttpClient httpClient = socksProxyBuilder.build();
                        if (httpClientTest(httpClient)) {
                            clientPool.add(httpClient);
                        }

                    } else if (aProxy.getType().equals("http")) {

                        HttpClientBuilder httpProxyBuilder = getBuilder();

                        HttpHost proxy = new HttpHost(aProxy.getAddress(), aProxy.getPort());

                        if (aProxy.getUsername() != null || aProxy.getPassword() != null) {
                            String username = aProxy.getUsername();
                            String password = aProxy.getPassword();
                            CredentialsProvider credsProvider = new BasicCredentialsProvider();
                            credsProvider.setCredentials(
                                    new AuthScope(aProxy.getAddress(), aProxy.getPort()),
                                    new UsernamePasswordCredentials(username, password));
                            httpProxyBuilder.setDefaultCredentialsProvider(credsProvider);
                        }
                        httpProxyBuilder.setProxy(proxy);
                        HttpClient httpClient = httpProxyBuilder.build();
                        if (httpClientTest(httpClient)) {
                            clientPool.add(httpClient);
                        }

                    } else {
                        System.err.println("Not support this :" + aProxy.toString());
                    }

                }

            } catch (FileNotFoundException e) {
                System.out.println("remote.properties not exist, There is no proxy in client pool");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Proxy config error, There is no proxy in client pool");
            }

        }

        if (count() == 0) {
            System.err.println("Can not Create Client POOL");
            System.exit(1);
        }
    }

    public HttpClient getRand() {
        return get( (int) (Math.random() * (count() + 1)));
    }

    public HttpClient get() {
        last += 1;
        last %= count();
        return get(last);
    }

    public HttpClient get(int index) {
        index = index % count();
        return  clientPool.get(index);
    }

    public int count() {
        return clientPool.size();
    }

    private HttpClientBuilder getBuilder(InetSocketAddress socksProxyAddress) {

        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("https", getSSLSocketFactory(socksProxyAddress))
                        .register("http", getHttpSocketFactory(socksProxyAddress))
                        .build();
        
        PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        return getBaseBuilder().setConnectionManager(clientConnectionManager);

    }

    private HttpClientBuilder getBuilder() {
        ConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(getSSLContext());

        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("https", sslsf)
                        .register("http", getHttpSocketFactory(null))
                        .build();

        PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        return getBaseBuilder().setConnectionManager(clientConnectionManager);

    }

    private HttpClientBuilder getBaseBuilder() {
        RequestConfig config = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(3000).build();
        return HttpClients.custom().setMaxConnTotal(8).
                setMaxConnPerRoute(4).setDefaultRequestConfig(config).setRetryHandler(new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount > 3) {
                    return false;
                }
                return true;
            }
        });
    }


    private ConnectionSocketFactory getHttpSocketFactory(final InetSocketAddress socksProxyAddress) {

        if (socksProxyAddress == null) return PlainConnectionSocketFactory.getSocketFactory();

        return new ConnectionSocketFactory() {
            @Override
            public Socket createSocket(HttpContext context) throws IOException {
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksProxyAddress);
                return new Socket(proxy);
            }

            @Override
            public Socket connectSocket(int connectTimeout, Socket sock, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {
                final Socket socket = sock != null ? sock : createSocket(context);
                if (localAddress != null) {
                    socket.bind(localAddress);
                }
                try {
                    socket.connect(remoteAddress, connectTimeout);
                } catch (final IOException ex) {

                    try {
                        socket.close();
                    } catch (final IOException ignore) {
                    }
                    throw ex;
                }
                return socket;
            }
        };
    }

    private SSLConnectionSocketFactory getSSLSocketFactory(final InetSocketAddress socksProxyAddress) {


        return new SSLConnectionSocketFactory(getSSLContext()) {
            @Override
            public Socket createSocket(HttpContext context) throws IOException {
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksProxyAddress);
                return new Socket(proxy);
            }
            @Override
            public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress,
                                        InetSocketAddress localAddress, HttpContext context) throws IOException {
                remoteAddress = InetSocketAddress.createUnresolved(host.getHostName(), host.getPort());
                return super.connectSocket(connectTimeout, socket, host, remoteAddress, localAddress, context);
            }
        };

    }

    private SSLContext getSSLContext() {
        SSLContext sslContext = null;

        try {
            sslContext = SSLContext.getDefault();
        } catch (NoSuchAlgorithmException e) {
        }

        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        };
        try {
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new java.security.SecureRandom());
        } catch (KeyManagementException e) {

        }

        return sslContext;
    }

    private boolean httpClientTest(HttpClient httpClient) {
        try {
            HttpResponse response = httpClient.execute(new HttpHead("https://www.baidu.com/"));
            if (response.getStatusLine().getStatusCode() == 200) return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
