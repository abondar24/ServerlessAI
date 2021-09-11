package org.abondar.experimental.feedback.sentiment.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.feedback.common.data.Message;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.comprehend.ComprehendAsyncClient;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentRequest;
import software.amazon.awssdk.services.comprehend.model.LanguageCode;
import software.amazon.awssdk.services.comprehend.model.SentimentType;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import static org.abondar.experimental.feedback.common.util.Constant.CLASSIFY_STREAM;


public class SentimentHandler implements RequestHandler<KinesisEvent, Void> {


    private final KinesisAsyncClient kinesisClient;

    private final ComprehendAsyncClient comprehendClient;

    private final ObjectMapper mapper;

    public SentimentHandler(){

        this.kinesisClient = buildKinesisClient();
        this.comprehendClient = buildComprehendClient();
        this.mapper = new ObjectMapper();

    }

    private KinesisAsyncClient buildKinesisClient(){
        return KinesisAsyncClient.builder()
                .build();
    }

    private ComprehendAsyncClient buildComprehendClient(){
        return ComprehendAsyncClient.builder()
                .build();
    }

    @Override
    public Void handleRequest(KinesisEvent input, Context context) {
        var logger = context.getLogger();
        var records = input.getRecords();

        records.forEach(r->{
            try {
                var payload = r.getKinesis().getData().array();
                var msg = mapper.readValue(payload, Message.class);

                var detectRequest = buildDetectRequest(msg);
                var detectResponse =
                        comprehendClient.detectSentiment(detectRequest).get();

                if (detectResponse!=null){
                    var sentiment = detectResponse.sentiment();
                    msg.setSentiment(detectResponse.sentimentAsString());

                    var sentimentScore = detectResponse.sentimentScore();
                    msg.setSentimentScore(sentimentScore.toString());

                    if ( sentiment == SentimentType.NEUTRAL   || sentiment == SentimentType.MIXED ||
                            sentiment==SentimentType.NEGATIVE || sentimentScore.positive()<0.85){
                        logger.log("saving negative");
                        pushToSentimentStream(msg);
                    }

                }

            } catch (IOException | InterruptedException | ExecutionException ex) {
                logger.log(ex.getMessage());
            }
        });

        return null;
    }

    private DetectSentimentRequest buildDetectRequest(Message msg) {

        return DetectSentimentRequest.builder()
                .languageCode(LanguageCode.EN)
                .text(msg.getText())
                .build();
    }


    private void pushToSentimentStream(Message message) throws IOException,InterruptedException,ExecutionException {
        var body = mapper.writeValueAsString(message);
        var recordRequest = buildRecordRequest(body);

        var res= kinesisClient.putRecord(recordRequest);
        System.out.println(res.get().toString());
    }

    private PutRecordRequest buildRecordRequest(String eventBody) {
        return PutRecordRequest.builder()
                .data(SdkBytes.fromString(eventBody, StandardCharsets.UTF_8))
                .partitionKey("1")
                .streamName(CLASSIFY_STREAM)
                .build();
    }
}
