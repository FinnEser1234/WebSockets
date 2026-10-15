package htl.nwt.http.client;

import java.io.IOException;
import java.io.InputStream;

public class HttpResponse {
    private String responseLine;
    private String[] headers;
    private byte[] body;


    public String getResponseLine() {
        return responseLine;
    }

    public String[] getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void recieve(InputStream is) throws IOException {
        boolean foundCr = false;
        byte[] buf = new byte[4096];
        int pos = 0;

        int b = is.read();
        while (b >= 0) {
            if (b == 0xd) {
                foundCr = true;
            } else if (b == 0xa && foundCr) {
                // TODO
                foundCr = false;
            } else if (b == 0xa) {
                buf[pos] = 0xd;
                buf[pos] = (byte) b;
            } else {
                buf[pos] = (byte) b;
            }

            pos++;
            b = is.read();
        }
    }
}
