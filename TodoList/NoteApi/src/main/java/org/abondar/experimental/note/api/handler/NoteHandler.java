package org.abondar.experimental.note.api.handler;

import com.amazonaws.endpointdiscovery.Constants;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class NoteHandler {
    private static final Map<String, String> HEADERS = Map.of("Access-Control-Allow-Headers", "Content-Type",
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Methods", "OPTIONS,POST,PUT,GET,DELETE",
            "Access-Control-Allow-Credentials", "true",
            "Content-Type", "application/json"
    );
    private static final String ID_PARAM = "id";
    protected ObjectMapper mapper;
    protected AmazonTranscribe client;

    public NoteHandler() {
        this.mapper = new ObjectMapper();
        this.client = AmazonTranscribeClientBuilder.defaultClient();
    }


    protected APIGatewayProxyResponseEvent buildResponse(int code, String body) {
        var resp = new APIGatewayProxyResponseEvent();
        resp.setBody(body);
        resp.setStatusCode(code);
        resp.setHeaders(HEADERS);
        resp.setIsBase64Encoded(true);

        return resp;
    }

    protected String getId(APIGatewayProxyRequestEvent input) {
        var params = input.getPathParameters();
        return params.get(ID_PARAM);
    }

}
