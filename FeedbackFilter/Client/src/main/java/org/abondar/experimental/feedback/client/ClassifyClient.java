package org.abondar.experimental.feedback.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.model.DocumentClass;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import static org.abondar.experimental.feedback.common.util.Constant.RESULT_BUCKET;
import static org.abondar.experimental.feedback.common.util.Error.WRONG_INPUT;

public class ClassifyClient {

    private final HttpClient client;

    private final S3AsyncClient s3Client;

    private final String url;

    public ClassifyClient(String url) {
        this.url = url;
        this.client = HttpClient.newHttpClient();
        this.s3Client = buildS3Client();
    }

    private S3AsyncClient buildS3Client() {
        return S3AsyncClient.builder()
                .region(Region.EU_WEST_1)
                .httpClientBuilder(NettyNioAsyncHttpClient.builder())
                .build();
    }

    public void uploadFeedback(String feedbackFile) throws IOException,InterruptedException {
        if (!feedbackFile.endsWith(".json")){
            System.err.println(WRONG_INPUT);
        }

        var file = new File(feedbackFile);
        var body =  Files.readString(file.toPath());

        var req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        var resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        var respBody = resp.body();

        System.out.println(respBody);
    }

    public void getClassifyResults(String outputPath){

        try {
            var res = s3Client.listObjectsV2(builder -> builder.bucket(RESULT_BUCKET)
                    .maxKeys(10)
                    .build()).get();

            var latest = res.contents().stream().max(Comparator.comparing(S3Object::eTag));
            if (latest.isPresent()){
                var obj =latest.get();
                if (!obj.key().isEmpty()){
                    s3Client.getObject(builder -> builder.bucket(RESULT_BUCKET)
                            .key(obj.key())
                            .build(), Path.of(outputPath));
                } else {
                    System.out.println("empty key");
                }

            }

        } catch (InterruptedException | ExecutionException ex){
            System.err.println(ex.getMessage());
        }


    }
}
