package org.abondar.experimental.identity.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.abondar.experimental.identity.data.UploadResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class IdentityClient {

    private final OkHttpClient client;
    private final ObjectMapper mapper;


    private final String url;

    public IdentityClient(String url){
        this.url = url;
        this.client = new OkHttpClient();
        this.mapper = new ObjectMapper();
    }

    public String uploadImage(String filePath) throws IOException {
        var file = new File(filePath);
        var content = Files.readAllBytes(file.toPath());

        var requestBody = RequestBody.create(content);
        var request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            var json = response.body().string();
            var resp = mapper.readValue(json, UploadResponse.class);
            return resp.getKey();
        }

    }

    public String analyseResults(String id) throws IOException{
        var request = new Request.Builder()
                .url(url +"/"+id)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
