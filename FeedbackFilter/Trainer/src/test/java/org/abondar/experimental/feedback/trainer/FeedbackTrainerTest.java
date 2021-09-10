package org.abondar.experimental.feedback.trainer;

import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import org.abondar.experimental.feedback.common.data.Message;
import org.abondar.experimental.feedback.common.test.TestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import software.amazon.awssdk.services.comprehend.ComprehendAsyncClient;
import software.amazon.awssdk.services.comprehend.model.CreateDocumentClassifierRequest;
import software.amazon.awssdk.services.comprehend.model.CreateDocumentClassifierResponse;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FeedbackTrainerTest {

    private ComprehendAsyncClient comprehendClient;
    private FeedbackTrainer trainer;

    @BeforeEach
    public void init() throws Exception {
        trainer = new FeedbackTrainer();
        comprehendClient = mock(ComprehendAsyncClient.class);
        FieldSetter.setField(trainer, trainer.getClass()
                .getDeclaredField("client"), comprehendClient);
    }

    @Test
    public void createDocumentClassifierTest() {
        var docArn = "someArn";
        var ct = CompletableFuture.completedFuture(CreateDocumentClassifierResponse
                .builder()
                .documentClassifierArn(docArn)
                .build());

        when(comprehendClient.createDocumentClassifier(any(Consumer.class)))
                .thenReturn(ct);

        var arn = trainer.createDocumentClassifier();
        assertEquals(docArn,arn);

    }

}
