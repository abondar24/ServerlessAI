package org.abondar.experimental.note.api;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.abondar.experimental.note.api.handler.TranscribeHandler;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class HandlerTest {

    private static TranscribeClient client;
    private TestContext context;

    @BeforeAll
    public static void init() {
        client = mock(TranscribeClient.class);
    }


    @BeforeEach
    public void initContext() {
        this.context = new TestContext();
    }

    @Test
    public void transcribeHandlerTest() throws Exception {
        var testNote = "{\n" +
                "    \"noteLang\": \"en-US\",\n" +
                "    \"noteUri\": \"https://s3-eu-west-1.amazonaws.com/td-str/public/b499d1a4-a170-472e-a8f0-4e955856bb18.wav\",\n" +
                "    \"noteFormat\": \"wav\",\n" +
                "    \"noteName\": \"b499d1a4-a170-472e-a8f0-4e955856bb18\",\n" +
                "    \"noteSampleRate\": 48000\n" +
                "}";

        var requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody(testNote);
        var handler = new TranscribeHandler();

        FieldSetter.setField(handler, handler.getClass()
                .getSuperclass().getDeclaredField("client"), client);
        when(client.startTranscriptionJob(any(StartTranscriptionJobRequest.class)))
                .thenReturn(any(StartTranscriptionJobResponse.class));

        var result = handler.handleRequest(requestEvent, context);
        var resp = result.getBody();

        assertEquals(200, result.getStatusCode());
        assertEquals(5, result.getHeaders().size());
        assertTrue(result.getHeaders().containsKey("Access-Control-Allow-Origin"));
        assertEquals("*", result.getHeaders().get("Access-Control-Allow-Origin"));
        assertEquals("Transcribe Job Started", resp);

    }

}
