package httpclient;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;


/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Nov 20, 2018 17:32
 */
public class SimpleHttpResponse {

    private String body;
    private int statusCode;
    private HttpResponse rawResponse;

    public String getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public HttpResponse getRawResponse() {
        return rawResponse;
    }

    public SimpleHttpResponse(String body, int statusCode, HttpResponse rawResponse) {
        this.body = body;
        this.statusCode = statusCode;
        this.rawResponse = rawResponse;
    }

    public static SimpleHttpResponse build(HttpResponse response, String charset) throws ParseException, IOException {
        String content = EntityUtils.toString(response.getEntity(), charset);
        int statusCode = response.getStatusLine().getStatusCode();
        return new SimpleHttpResponse(content, statusCode, response);
    }

    public static SimpleHttpResponse build(HttpResponse response) throws ParseException, IOException {
        String charset = ContentType.getOrDefault(response.getEntity()).getCharset().toString();
        if (charset == null) charset = "UTF-8";
        String content = EntityUtils.toString(response.getEntity(), charset);
        int statusCode = response.getStatusLine().getStatusCode();
        return new SimpleHttpResponse(content, statusCode, response);
    }

}