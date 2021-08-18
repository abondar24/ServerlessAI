package org.abondar.experimental.note.api.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.transcribe.TranscribeClient;

import java.util.Map;

public class NoteHandler {
    private static final Map<String, String> HEADERS = Map.of("Access-Control-Allow-Headers", "Content-Type",
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Methods", "OPTIONS,POST,PUT,GET,DELETE",
            "Access-Control-Allow-Credentials", "true",
            "Content-Type", "application/json"
    );
    private static final String ID_PARAM = "id";

    protected static final String BUCKET = "td-str";

    protected ObjectMapper mapper;
    protected TranscribeClient client;


    public NoteHandler() {
        this.mapper = new ObjectMapper();
        this.client = TranscribeClient.create();
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
