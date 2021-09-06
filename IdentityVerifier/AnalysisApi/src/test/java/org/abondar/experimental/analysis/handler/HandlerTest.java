package org.abondar.experimental.analysis.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.identity.analysis.handler.AnalyseHandler;
import org.abondar.experimental.identity.analysis.handler.UploadHandler;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.AnalyzeDocumentRequest;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HandlerTest {

    private static S3Client s3;

    private static TextractClient txt;

    private static ObjectMapper mapper;
    private TestContext context;

    @BeforeAll
    public static void init() {
        s3 = mock(S3Client.class);
        txt = mock(TextractClient.class);
        mapper = new ObjectMapper();
    }

    @BeforeEach
    public void initContext() {
        this.context = new TestContext();
    }

    @Test
    public void uploadHandlerTest() throws Exception {
        var requestEvent = new APIGatewayProxyRequestEvent();

        var is = HandlerTest.class.getResourceAsStream("/passport.jpg");
        var body = is.readAllBytes();
        requestEvent.setBody(new String(Base64.encodeBase64(body), StandardCharsets.UTF_8));

        var handler = new UploadHandler();
        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("s3"), s3);


        var response = handler.handleRequest(requestEvent, context);
        verify(s3).putObject(any(PutObjectRequest.class), any(RequestBody.class));

        var res = response.getBody();
        assertNotNull(res);
    }


    @Test
    public void analyseHandlerTest() throws Exception {
        var requestEvent = new APIGatewayProxyRequestEvent();
        var uuid = UUID.randomUUID().toString();

        requestEvent.setPathParameters( Map.of("id",uuid));


        var handler = new AnalyseHandler();
        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("txt"), txt);


        var response = handler.handleRequest(requestEvent, context);
        verify(txt).analyzeDocument(any(AnalyzeDocumentRequest.class));

        var res = response.getBody();
        var resp = mapper.readValue(res, String.class);

        assertEquals("analysis failed",resp);
    }

}
