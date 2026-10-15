package htl.nwt.http.client;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

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
        os.write(requestLine.getBytes(StandardCharsets.UTF_8));
        os.write(DELIMETER);

        for (String header : headers) {
            os.write(header.getBytes(StandardCharsets.UTF_8));
            os.write(DELIMETER);
        }

        if (body != null && body.length > 0) {
            String contentLengthHeader = "Content-Length: " + body.length;
            os.write(contentLengthHeader.getBytes(StandardCharsets.UTF_8));
            os.write(DELIMETER);
        }

        os.write(DELIMETER);
        os.flush();

        if (body != null && body.length > 0) {
            os.write(body);
            os.flush();
        }
    }

}
