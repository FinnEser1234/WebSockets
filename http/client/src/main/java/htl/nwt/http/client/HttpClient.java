package htl.nwt.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class HttpClient {
    private final static Logger log = Logger.getAnonymousLogger();
    public static void main(String[] args) {
        try (Socket socket = new Socket("orf.at", 80);) {
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();


            HttpRequest request = new HttpRequest("GET / HTTP/1.1", "Host: orf.at");
            request.send(os);

            HttpResponse response = new HttpResponse();
            response.recieve(is);
            log.fine(response.getBody().toString());
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }
}
