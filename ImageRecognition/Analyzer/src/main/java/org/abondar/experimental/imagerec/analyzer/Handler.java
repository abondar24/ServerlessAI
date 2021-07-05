package org.abondar.experimental.imagerec.analyzer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.imagerec.analyzer.data.AnalysisResults;
import org.abondar.experimental.imagerec.analyzer.data.Event;
import org.abondar.experimental.imagerec.analyzer.data.ImageLabel;
import org.abondar.experimental.imagerec.analyzer.data.ImageResult;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.S3Object;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return "Analysis complete";
    }

    private void iterateBucket(String url, S3Client s3, LambdaLogger logger) throws IOException {
        logger.log(String.format("Iterating bucket with key %s",url));
        var resp = s3.listObjectsV2(buildListObjectRequest(url+"/"));
        var contents = resp.contents();
        if (contents.isEmpty()) {
            logger.log("Empty data");
            return;
        }

        Map<String, DetectLabelsResponse> labels = new HashMap<>();
        var anlFile = url + "/" + ANALYSYS_FILE;
        contents
                .stream()
                .filter(img -> !img.key().equals(anlFile))
                .forEach(img -> {
                    var imgFile = img.key();
                    var labelsResponse = analyzeLabels(imgFile);
                    labels.put(imgFile, labelsResponse);
                });

        writeResults(labels, anlFile, logger);
    }

    private void writeResults(Map<String, DetectLabelsResponse> labels, String anlFile,LambdaLogger logger) throws IOException {
        logger.log("Saving analysis results");
        var s3 = buildS3Client();
        var req = buildPutRequest(anlFile);

        s3.putObject(req, RequestBody.fromBytes(getAnalysisContent(labels)));
    }

    private byte[] getAnalysisContent(Map<String, DetectLabelsResponse> imageLabels) throws IOException {

        var analysisResults = new AnalysisResults();

        Map<String, Integer> wordCloud = new HashMap<>();
        List<ImageResult> imageResults = new ArrayList<>();
        imageLabels.forEach((i, lr) -> {
            var labels = lr.labels();
            fillWordCount(wordCloud, labels);

            imageResults.add(fillImageResult(i, labels));
        });

        analysisResults.setWordCloud(wordCloud);
        analysisResults.setResults(imageResults);
        var json = mapper.writeValueAsString(analysisResults);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    private void fillWordCount(Map<String, Integer> wordCloud, List<Label> labels) {
        labels.forEach(l -> {
            var labelName = l.name();
            if (wordCloud.containsKey(labelName)) {
                var lCount = wordCloud.get(labelName);
                wordCloud.put(labelName, lCount + 1);
            } else {
                wordCloud.put(labelName, 1);
            }
        });
    }

    private ImageResult fillImageResult(String image, List<Label> labels) {
        var imageResult = new ImageResult(image);

        List<ImageLabel> imageLabels = new ArrayList<>();
        labels.forEach(l -> {
            var imageLabel = new ImageLabel(l.name(), l.confidence());
            imageLabels.add(imageLabel);
        });
        imageResult.setLabels(imageLabels);

        return imageResult;
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

    private RekognitionClient buildRekognitionClient() {
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

    private PutObjectRequest buildPutRequest(String fileKey) {
        return PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileKey)
                .build();
    }
}
