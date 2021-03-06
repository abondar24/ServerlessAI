package org.abondar.experimental.feedback.classifier.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.feedback.common.data.FeedbackClass;
import org.abondar.experimental.feedback.common.data.Message;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendAsyncClient;
import software.amazon.awssdk.services.comprehend.model.DocumentClass;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.abondar.experimental.feedback.common.util.Constant.CLASSIFIER_ENDPOINT_ARN;
import static org.abondar.experimental.feedback.common.util.Constant.CLASS_SCORE;
import static org.abondar.experimental.feedback.common.util.Constant.RESULT_BUCKET;

public class ClassifierHandler implements RequestHandler<KinesisEvent, Void> {

    private final S3AsyncClient s3Client;

    private final ComprehendAsyncClient comprehendClient;

    private final ObjectMapper mapper;

    public ClassifierHandler() {
        this.s3Client = buildS3Client();
        this.comprehendClient = buildComprehendClient();
        this.mapper = new ObjectMapper();
    }

    private S3AsyncClient buildS3Client() {
        return S3AsyncClient.builder()
                .region(Region.EU_WEST_1)
                .httpClientBuilder(NettyNioAsyncHttpClient.builder())
                .build();
    }

    private ComprehendAsyncClient buildComprehendClient() {
        return ComprehendAsyncClient.builder()
                .build();
    }


    @Override
    public Void handleRequest(KinesisEvent input, Context context) {
        var logger = context.getLogger();
        var records = input.getRecords();

        records.forEach(r -> {
            try {
                var payload = r.getKinesis().getData().array();
                var msg = mapper.readValue(payload, Message.class);

                var res = comprehendClient.classifyDocument(builder -> builder.endpointArn(CLASSIFIER_ENDPOINT_ARN)
                        .text(msg.getText())
                        .build()).get();

                if (!res.classes().isEmpty()) {
                    var cls = determineClass(res.classes());
                    saveResultToBucket(cls, msg);
                }

            } catch (IOException | InterruptedException | ExecutionException ex) {
                logger.log(ex.getMessage());
            }
        });

        return null;
    }

    public String determineClass(List<DocumentClass> resultClasses) {
        var cls = FeedbackClass.UNCLASSIFIED;

        var maxDoc = resultClasses.stream().max(Comparator.comparing(DocumentClass::score));

        if (maxDoc.isPresent()) {
            var doc = maxDoc.get();
            if (doc.score() > CLASS_SCORE) {
                cls = FeedbackClass.valueOf(doc.name().toUpperCase());
                System.out.println(cls);
            }
        }
        System.out.println(cls);
        return cls.toString();
    }

    private void saveResultToBucket(String cls, Message message) throws IOException,InterruptedException,ExecutionException {
        var uuid = UUID.randomUUID().toString();
        var body = mapper.writeValueAsString(message);

        var res= s3Client.putObject(builder -> {
            builder.bucket(RESULT_BUCKET)
                    .key(cls + "/" + uuid + ".json")
                    .build();
        }, AsyncRequestBody.fromString(body));

        System.out.println(res.get());
    }
}
