package org.abondar.experimental.feedback.translate.handler;

import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.translate.AmazonTranslateAsync;
import com.amazonaws.services.translate.AmazonTranslateAsyncClient;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.feedback.common.data.Message;
import org.abondar.experimental.feedback.common.test.TestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import software.amazon.awssdk.services.comprehend.ComprehendAsyncClient;
import software.amazon.awssdk.services.comprehend.model.DetectDominantLanguageRequest;
import software.amazon.awssdk.services.comprehend.model.DetectDominantLanguageResponse;
import software.amazon.awssdk.services.comprehend.model.DominantLanguage;
import software.amazon.awssdk.services.comprehend.model.LanguageCode;
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

    private TranslateHandler handler;


    @BeforeEach
    public void init() throws Exception{
        var msg = new Message("text","twitter","@myTwitter");
        var mapper = new ObjectMapper();
        var recordData = mapper.writeValueAsString(msg).getBytes();

        var kinesis = new KinesisEvent.Record();
        kinesis.setData(ByteBuffer.wrap(recordData));
        var record = new KinesisEvent.KinesisEventRecord();
        record.setKinesis(kinesis);

        event = new KinesisEvent();
        event.setRecords(List.of(record));

        context = new TestContext();
        handler = new TranslateHandler();

    }

    @Test
    public void translateHandlerTest() throws Exception{
        var kinesisClient = mock(KinesisAsyncClient.class);
        var translateClient = mock(AmazonTranslateAsync.class);
        var comprehendClient = mock(ComprehendAsyncClient.class);


        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("kinesisClient"), kinesisClient);

        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("translateClient"), translateClient);

        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("comprehendClient"), comprehendClient);

        var ct = CompletableFuture
                .completedFuture(PutRecordResponse.builder().build());

        when(kinesisClient.putRecord(any(PutRecordRequest.class)))
                .thenReturn(ct);


        var lang = DominantLanguage.builder()
                .languageCode(LanguageCode.DE.toString())
                .build();
        var ct1 = CompletableFuture
                .completedFuture(DetectDominantLanguageResponse.builder()
                        .languages(List.of(lang))
                        .build());

        when(comprehendClient.detectDominantLanguage(any(DetectDominantLanguageRequest.class)))
                .thenReturn(ct1);

        var trRes = new TranslateTextResult();
        trRes.setTranslatedText("Some text");

        var ct2 = CompletableFuture
                .completedFuture(trRes);
        when(translateClient.translateTextAsync(any(TranslateTextRequest.class)))
                .thenReturn(ct2);

        var res = handler.handleRequest(event,context);
        assertNull(res);
    }


    @Test
    public void translateHandlerEmptyLangTest() throws Exception{
        var comprehendClient = mock(ComprehendAsyncClient.class);
      FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("comprehendClient"), comprehendClient);


         var ct = CompletableFuture
                .completedFuture(DetectDominantLanguageResponse.builder().build());

        when(comprehendClient.detectDominantLanguage(any(DetectDominantLanguageRequest.class)))
                .thenReturn(ct);

        var res = handler.handleRequest(event,context);
        assertNull(res);
    }


    @Test
    public void translateHandlerEnglishTest() throws Exception{
        var kinesisClient = mock(KinesisAsyncClient.class);
        var translateClient = mock(AmazonTranslateAsync.class);
        var comprehendClient = mock(ComprehendAsyncClient.class);


        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("kinesisClient"), kinesisClient);

        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("translateClient"), translateClient);

        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("comprehendClient"), comprehendClient);

        var ct = CompletableFuture
                .completedFuture(PutRecordResponse.builder().build());

        when(kinesisClient.putRecord(any(PutRecordRequest.class)))
                .thenReturn(ct);


        var lang = DominantLanguage.builder()
                .languageCode(LanguageCode.EN.toString())
                .build();
        var ct1 = CompletableFuture
                .completedFuture(DetectDominantLanguageResponse.builder()
                        .languages(List.of(lang))
                        .build());

        when(comprehendClient.detectDominantLanguage(any(DetectDominantLanguageRequest.class)))
                .thenReturn(ct1);


        var res = handler.handleRequest(event,context);
        assertNull(res);
    }
}
