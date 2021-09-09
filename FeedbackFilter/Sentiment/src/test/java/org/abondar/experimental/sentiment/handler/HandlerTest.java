package org.abondar.experimental.sentiment.handler;

import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.feedback.common.data.Message;
import org.abondar.experimental.feedback.common.test.TestContext;
import org.abondar.experimental.feedback.sentiment.handler.SentimentHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import software.amazon.awssdk.services.comprehend.ComprehendAsyncClient;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentRequest;
import software.amazon.awssdk.services.comprehend.model.DetectSentimentResponse;
import software.amazon.awssdk.services.comprehend.model.SentimentType;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HandlerTest {

    private KinesisEvent event;

    private TestContext context;

    private SentimentHandler handler;

    private ComprehendAsyncClient comprehendClient;

    private KinesisAsyncClient kinesisClient;

    @BeforeEach
    public void init() throws Exception {
        var msg = new Message();
        msg.setText("text");
        var mapper = new ObjectMapper();
        var recordData = mapper.writeValueAsString(msg).getBytes();

        var kinesis = new KinesisEvent.Record();
        kinesis.setData(ByteBuffer.wrap(recordData));
        var record = new KinesisEvent.KinesisEventRecord();
        record.setKinesis(kinesis);

        event = new KinesisEvent();
        event.setRecords(List.of(record));

        context = new TestContext();
        handler = new SentimentHandler();

        comprehendClient = mock(ComprehendAsyncClient.class);
        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("comprehendClient"), comprehendClient);

        kinesisClient = mock(KinesisAsyncClient.class);
        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("kinesisClient"), kinesisClient);
    }

    @Test
    public void sentimentHandlerTest() {
        var ct = CompletableFuture.completedFuture(
                DetectSentimentResponse.builder().sentiment(SentimentType.NEGATIVE)
                .sentimentScore(builder -> {
                    builder.negative(0.95f);
                    builder.positive(0.97f);
                })
                .build());

        when(comprehendClient.detectSentiment(any(DetectSentimentRequest.class)))
                .thenReturn(ct);

        var ct1 = CompletableFuture
                .completedFuture(PutRecordResponse.builder().build());

        when(kinesisClient.putRecord(any(PutRecordRequest.class)))
                .thenReturn(ct1);

        var res = handler.handleRequest(event,context);
        assertNull(res);
    }

    @Test
    public void sentimentHandlerPositiveTest() {
        var ct = CompletableFuture.completedFuture(
                DetectSentimentResponse.builder().sentiment(SentimentType.POSITIVE)
                        .sentimentScore(builder -> {
                            builder.positive(0.97f);
                        })
                        .build());

        when(comprehendClient.detectSentiment(any(DetectSentimentRequest.class)))
                .thenReturn(ct);


        var res = handler.handleRequest(event,context);
        assertNull(res);
    }
}
