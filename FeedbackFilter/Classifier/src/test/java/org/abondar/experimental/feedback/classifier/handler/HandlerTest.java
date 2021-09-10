package org.abondar.experimental.feedback.classifier.handler;

import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.feedback.common.data.FeedbackClass;
import org.abondar.experimental.feedback.common.data.Message;
import org.abondar.experimental.feedback.common.test.TestContext;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.comprehend.ComprehendAsyncClient;
import software.amazon.awssdk.services.comprehend.model.ClassifyDocumentResponse;
import software.amazon.awssdk.services.comprehend.model.DocumentClass;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HandlerTest {


    @Test
    public void determineClassTest() {

        var handler = new ClassifierHandler();
        var docClass = DocumentClass.builder()
                .score(0.56f)
                .name("auto")
                .build();

        var docClass1 = DocumentClass.builder()
                .score(0.87f)
                .name("pet")
                .build();

        var res = handler.determineClass(List.of(docClass1, docClass));
        assertEquals(FeedbackClass.PET.name(), res);
    }


    @Test
    public void determineClassUnclassifiedTest() {

        var handler = new ClassifierHandler();
        var docClass = DocumentClass.builder()
                .score(0.56f)
                .name("auto")
                .build();

        var docClass1 = DocumentClass.builder()
                .score(0.45f)
                .name("pet")
                .build();

        var res = handler.determineClass(List.of(docClass1, docClass));
        assertEquals(FeedbackClass.UNCLASSIFIED.name(), res);
    }

    @Test
    public void determineClassSameTest() {

        var handler = new ClassifierHandler();
        var docClass = DocumentClass.builder()
                .score(0.56f)
                .name("pet")
                .build();

        var docClass1 = DocumentClass.builder()
                .score(0.95f)
                .name("pet")
                .build();

        var res = handler.determineClass(List.of(docClass1, docClass));
        assertEquals(FeedbackClass.PET.name(), res);
    }

    @Test
    public void classifierHandlerTest() throws Exception {
        var msg = new Message();
        msg.setText("text");
        var mapper = new ObjectMapper();
        var recordData = mapper.writeValueAsString(msg).getBytes();

        var kinesis = new KinesisEvent.Record();
        kinesis.setData(ByteBuffer.wrap(recordData));
        var record = new KinesisEvent.KinesisEventRecord();
        record.setKinesis(kinesis);

        var event = new KinesisEvent();
        event.setRecords(List.of(record));

        var context = new TestContext();
        var handler = new ClassifierHandler();

        var comprehendClient = mock(ComprehendAsyncClient.class);
        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("comprehendClient"), comprehendClient);

        var s3Client = mock(S3AsyncClient.class);
        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("s3Client"), s3Client);

        var docClass = DocumentClass.builder()
                .score(0.56f)
                .name("auto")
                .build();

        var docClass1 = DocumentClass.builder()
                .score(0.85f)
                .name("pet")
                .build();

        var ct = CompletableFuture.completedFuture(
                ClassifyDocumentResponse.builder()
                        .classes(List.of(docClass, docClass1))
                        .build());

        when(comprehendClient.classifyDocument(any(Consumer.class)))
                .thenReturn(ct);

        var res = handler.handleRequest(event, context);
        verify(s3Client).putObject(any(Consumer.class), any(AsyncRequestBody.class));

        assertNull(res);
    }
}
