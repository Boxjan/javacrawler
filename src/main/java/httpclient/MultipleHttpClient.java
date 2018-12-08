package httpclient;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Nov 20, 2018 16:20
 */
public class MultipleHttpClient {

    protected HttpClient delegate;

    private String Ua;

    public static MultipleHttpClient instance() {
        return new MultipleHttpClient();
    }

    MultipleHttpClient() {
        try {
            InputStream inStream = HttpClientPool.class.getResourceAsStream("../remote.properties");
            Properties properties = new Properties();
            properties.load(inStream);
            Ua = properties.getProperty("ua");
        } catch (Exception e) {
            Ua = "Just a Java Bot, build for the Java course, Sorry for trouble caused.";
        }
    }

    public static boolean client(HttpClientInfo info) throws Exception {
        SimpleHttpResponse response = null;
        SimpleHttpRequest request ;
        String method;

        try {
            request = info.getRequest();
            method = request.getMethod().toLowerCase();
        } catch (NullPointerException e) {
            throw new Exception("No request");
        }


        if (method.equals("get")) {
             response = instance().get(info.getRequest());
        } else if (method.equals("post")) {
            response = instance().post(info.getRequest());
        } else if(method.equals("head")) {
            response = instance().head(info.getRequest());
        } else {
            return false;
        }

        if (response != null) {
            info.setResponse(response);
            return true;
        }
        return false;
    }

    private  SimpleHttpResponse get(SimpleHttpRequest info) {
        String url = info.getUrl();
        Map<String, String> formMap = info.getFormData();

        if (formMap != null && !formMap.isEmpty()) {
            url += "?";
            for (String key: formMap.keySet()) {
                url += key + "=" + formMap.get(key) + "&";
            }
        }
        HttpGet request = new HttpGet(url);

        return execute(request, info.getHeader());
    }

    private  SimpleHttpResponse post(SimpleHttpRequest info) {
        String url = info.getUrl();
        Map<String, String> formMap = info.getFormData();

        HttpPost request = new HttpPost(url);

        if(formMap != null && !formMap.isEmpty()) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (String key: formMap.keySet()) {
                params.add(new BasicNameValuePair(key, formMap.get(key)));
            }
            try {
                request.setEntity(new UrlEncodedFormEntity(params));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return execute(request, info.getHeader());
    }

    private  SimpleHttpResponse head(SimpleHttpRequest info) {
        String url = info.getUrl();
        Map<String, String> formMap = info.getFormData();

        if (formMap != null && !formMap.isEmpty()) {
            url += "?";
            for (String key: formMap.keySet()) {
                url += key + "=" + formMap.get(key) + "&";
            }
        }
        HttpHead request = new HttpHead(url);

        return execute(request, info.getHeader());

    }

    private SimpleHttpResponse execute(final HttpUriRequest request, Map<String, String> headerMap) {

        if (headerMap != null && !headerMap.isEmpty()) {
            for (String key: headerMap.keySet())
                request.setHeader(key, headerMap.get(key));
        }

        if (headerMap == null || !headerMap.containsKey("User-Agent") ) {
            request.setHeader("User-Agent", Ua);
        }

        try {
            return SimpleHttpResponse.build(HttpClientPool.getInstance().get().execute(request));
        } catch (Exception e) {
            return null;
        }

    }

}



