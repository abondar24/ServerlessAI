package org.abondar.experimental.identity.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.identity.data.UploadResponse;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class IdentityClient {

    private final HttpClient client;
    private final ObjectMapper mapper;


    private final String url;

    public IdentityClient(String url) {
        this.url = url;
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public String uploadImage(String filePath) throws IOException, InterruptedException {
        var file = new File(filePath);

        var body = Files.readAllBytes(file.toPath());
        var encodedBody = new String(Base64.encodeBase64(body), StandardCharsets.UTF_8);

        var req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(encodedBody))
                .build();

        var resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        var respBody = resp.body();
        try {
            var res = mapper.readValue(respBody, UploadResponse.class);
            return res.getKey();
        } catch (IOException ex){
            System.err.println(respBody);
            System.exit(2);
        }

        return null;
    }

    public String analyseResults(String id) throws IOException, InterruptedException {
        var req = HttpRequest.newBuilder()
                .uri(URI.create(url + "/" + id))
                .GET()
                .build();

        var resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        return resp.body();

    }
}
