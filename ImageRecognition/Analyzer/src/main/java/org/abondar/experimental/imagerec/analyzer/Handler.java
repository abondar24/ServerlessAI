package org.abondar.experimental.imagerec.analyzer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

import java.util.ArrayList;
import java.util.List;

import static org.abondar.experimental.imagerec.analyzer.Constants.ANALYSYS_FILE;
import static org.abondar.experimental.imagerec.analyzer.Constants.ANALYZE_ACTION;
import static org.abondar.experimental.imagerec.analyzer.Constants.BUCKET_NAME;

public class Handler implements RequestHandler<SQSEvent, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String handleRequest(SQSEvent input, Context context) {
        var logger = context.getLogger();

        var recs = input.getRecords();

        if (recs != null) {
            var s3 = buildS3Client();
            recs.forEach(rec -> {
                var body = rec.getBody();

                try {
                    var event = mapper.readValue(body, Event.class);

                    if (event.getAction().equals(ANALYZE_ACTION) && event.getMsg() != null) {
                        iterateBucket(event.getMsg().getUrl(), s3, logger);
                    }
                } catch (Exception ex) {
                    logger.log(ex.getMessage());
                }

            });
        }
        return null;
    }

    private void iterateBucket(String url, S3Client s3, LambdaLogger logger) {
        logger.log("Iterating bucket");
        var resp = s3.listObjectsV2(buildListObjectRequest(url));
        var contents = resp.contents();
        if (contents.isEmpty()){
           logger.log("Empty data");
           return;
        }

        List<DetectLabelsResponse> labels = new ArrayList<>();
        contents
                .stream()
                .filter(img-> !img.key().equals(url+"/"+ANALYSYS_FILE))
                .forEach(img -> labels.add(analyzeLabels(img.key())));

        //TODO: write results to json and save to bucket
    }

    private DetectLabelsResponse analyzeLabels(String imgKey) {
           var rekognitionClient = buildRekognitionClient();
           return rekognitionClient.detectLabels(buildLabelRequest(imgKey));
    }

    private S3Client buildS3Client() {
        return S3Client.builder()
                .region(Region.EU_WEST_1)
                .build();
    }

    private ListObjectsV2Request buildListObjectRequest(String prefix) {
        return ListObjectsV2Request.builder()
                .bucket(BUCKET_NAME)
                .maxKeys(1000)
                .prefix(prefix)
                .build();
    }

    private RekognitionClient buildRekognitionClient(){
        return RekognitionClient.builder()
                .region(Region.EU_WEST_1)
                .build();
    }

    private DetectLabelsRequest buildLabelRequest(String imgKey) {
        return DetectLabelsRequest.builder()
                .image(buildImage(imgKey))
                .maxLabels(10)
                .minConfidence(80.0F)
                .build();
    }

    private Image buildImage(String imgKey) {
        return Image.builder()
                .s3Object(buildS3Object(imgKey))
                .build();
    }

    private S3Object buildS3Object(String imgKey) {
        return S3Object.builder()
                .bucket(BUCKET_NAME)
                .name(imgKey)
                .build();
    }

}
