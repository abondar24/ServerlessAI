package org.abondar.experimental.feedback.api.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.abondar.experimental.feedback.common.test.TestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.awssdk.services.kinesis.model.PutRecordResponse;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HandlerTest {


    @Test
    public void feedbackApiHandlerTest() throws Exception{
        var kinesisAsyncClient = mock(KinesisAsyncClient.class);
        var httpClient = mock(SdkAsyncHttpClient.class);
        var context = new TestContext();

        var request = new APIGatewayProxyRequestEvent();
        request.setBody("Some event body");

        var handler = new FeedbackApiHandler();

        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("httpClient"),httpClient);

        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("kinesisClient"),kinesisAsyncClient);

        var ct = CompletableFuture
                .completedFuture(PutRecordResponse.builder().build());
        //when(ct.get()).thenReturn(any(PutRecordResponse.class));

        when(kinesisAsyncClient.putRecord(any(PutRecordRequest.class)))
                .thenReturn(ct);



        var res = handler.handleRequest(request,context);

        assertEquals(200,res.getStatusCode());
        assertEquals(5, res.getHeaders().size());
        assertTrue(res.getHeaders().containsKey("Access-Control-Allow-Origin"));
        assertEquals("*",res.getHeaders().get("Access-Control-Allow-Origin"));
        assertNull(res.getBody());
    }
}
