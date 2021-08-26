package org.abondar.experimental.note.api;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.abondar.experimental.note.api.handler.PollHandler;
import org.abondar.experimental.note.api.handler.TranscribeHandler;
import org.mockito.Spy;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import software.amazon.awssdk.services.transcribe.model.GetTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.GetTranscriptionJobResponse;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobResponse;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJob;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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
        assertEquals("\"Transcribe Job Started\"", resp);

    }


    @Test
    public void pollHandlerTest() throws Exception {
        var requestEvent = new APIGatewayProxyRequestEvent();
        var fakeId = "someId";
        requestEvent.setPathParameters(Map.of("id",fakeId));;

        var handler = new PollHandler();
        FieldSetter.setField(handler, handler.getClass()
                .getSuperclass()
                .getDeclaredField("client"), client);

        var req = GetTranscriptionJobRequest.builder()
                .transcriptionJobName(fakeId)
                .build();
        var jobResp = GetTranscriptionJobResponse.builder()
                .transcriptionJob(TranscriptionJob.builder().build())
                .build();

        var spy = spy(handler);

        var jobStatus = "{\"jobName\":\"b499d1a4-a170-472e-a8f0-4e955856bb18\",\"accountId\":\"203212890819\",\"results\":{\"transcripts\":[{\"transcript\":\"\"}],\"items\":[]},\"status\":\"COMPLETED\"}";

        when(client.getTranscriptionJob(req)).thenReturn(jobResp);
        doReturn(jobStatus).when(spy).getTranscriptResult(fakeId);

        var result = spy.handleRequest(requestEvent, context);

        var resp = result.getBody();

        assertEquals(200, result.getStatusCode());
        assertEquals(5, result.getHeaders().size());
        assertTrue(result.getHeaders().containsKey("Access-Control-Allow-Origin"));
        assertEquals("*", result.getHeaders().get("Access-Control-Allow-Origin"));
        assertEquals(jobStatus, resp);

    }
}
