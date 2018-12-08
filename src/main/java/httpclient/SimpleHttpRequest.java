package httpclient;

import java.util.Map;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Nov 21, 2018 18:23
 */
public class SimpleHttpRequest {
    private String url;

    private String method;
    private Map<String, String> formData;
    private Map<String, String> header;


    public SimpleHttpRequest(String url, Map<String, String> header, String method, Map<String,String> formData) {
        this.url = url;
        this.header = header;
        this.method = method;
        this.formData = formData;

    }

    public static SimpleHttpRequest build(String url, Map<String, String> header, String method, Map<String,String> formData) {
        return new SimpleHttpRequest(url, header, method, formData);
    }

    public static SimpleHttpRequest build(String url, String method) {
        return new SimpleHttpRequest(url, null, method, null);
    }

    public static SimpleHttpRequest build(String url, String method, Map<String,String> formData) {
        return new SimpleHttpRequest(url, null, method, formData);
    }

    public static SimpleHttpRequest build(String url) {
        return new SimpleHttpRequest(url, null, "GET", null);
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getFormData() {
        return formData;
    }

    @Override
    public String toString() {
        return "Url: " + url + " Method: " + method;
    }
}
