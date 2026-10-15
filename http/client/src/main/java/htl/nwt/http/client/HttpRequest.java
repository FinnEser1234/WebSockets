package htl.nwt.http.client;

import java.io.IOException;
import java.io.OutputStream;

public class HttpRequest {
    static private final byte[] DELIMETER = { 0x0d, 0x0a };

    private String requestLine;
    private String[] headers;
    private byte[] body;

    public HttpRequest(String requestLine, String[] headers, byte[] body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    public HttpRequest(String requestLine, String... headers) {
        this(requestLine, headers, null);
    }

    public void send(OutputStream os) throws IOException {
        os.write(requestLine.getBytes());
        os.write(DELIMETER);

        for (String header : headers) {
            os.write(header.getBytes());
            os.write(DELIMETER);
        }

        if (body != null) {
            os.write(("Content-Length: " + body.length).getBytes());
            os.write(DELIMETER);
        }

        os.write(DELIMETER);
        os.flush();

        if (body != null) {
            os.write(body);
        }
        
        os.flush();
    }
}
