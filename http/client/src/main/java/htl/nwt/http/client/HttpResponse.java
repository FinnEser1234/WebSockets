package htl.nwt.http.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    private int getContentLength(String[] headers) {
        for (String header : headers) {
            if (header.toLowerCase().startsWith("content-length:")) {
                return Integer.parseInt(header.substring(15).strip());
            }
        }
        return 0;
    }

    public void recieve(InputStream in) throws IOException {
        responseLine = recieveLine(in);
        List<String> headerList = new ArrayList<>();
        String header;
        while (!(header = recieveLine(in)).isEmpty()) {
            headerList.add(header);
        }

        headers = headerList.toArray(new String[0]);
        int contentLength = getContentLength(headers);

        body = readBody(in, contentLength);
    }


    private byte[] readBody(InputStream in, int contentLength) throws IOException {
        byte[] bodyData = new byte[contentLength];
        int totalBytesRead = 0;

        while (totalBytesRead < contentLength) {
            int bytesRead = in.read(bodyData, totalBytesRead, contentLength - totalBytesRead);
            if (bytesRead == -1) {
                throw new IOException("Stream closed before reading full content length");
            }
            totalBytesRead += bytesRead;
        }

        return bodyData;
    }


    public String recieveLine(InputStream is) throws IOException {
        boolean crFound = false;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int pos = 0;

        int b = is.read();
        while (b >= 0) {
            if (b == 0xd) {
                crFound = true;
            } else {
                if (crFound && b == 0xa) {
                    return new String(buffer.toByteArray(), 0, pos - 1, StandardCharsets.UTF_8);
                }
            }
            buffer.write((byte) b);

            pos++;
            b = is.read();
        }
        throw new IOException("Invalid state");
    }
}
