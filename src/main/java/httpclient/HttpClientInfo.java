package httpclient;

import java.util.UUID;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Nov 19, 2018 18:17
 */
public class HttpClientInfo {
    private UUID uuid;
    private SimpleHttpRequest request;
    private SimpleHttpResponse response;

    HttpClientInfo(SimpleHttpRequest request, SimpleHttpResponse response) throws Exception {
        uuid = UUID.randomUUID();
        if (request != null)
            this.request = request;
        else throw new Exception("request is null");
        this.response = response;
    }

    public UUID getUuid() {
        return uuid;
    }


    public static HttpClientInfo build(SimpleHttpRequest request, SimpleHttpResponse response) throws Exception {
        return new HttpClientInfo(request, response);
    }

    public static HttpClientInfo build(SimpleHttpRequest request) throws Exception {
        return new HttpClientInfo(request, null);
    }

    public SimpleHttpRequest getRequest() {
        return request;
    }

    public SimpleHttpResponse getResponse() {
        return response;
    }

    public void setResponse(SimpleHttpResponse response) {
        this.response = response;
    }
}
