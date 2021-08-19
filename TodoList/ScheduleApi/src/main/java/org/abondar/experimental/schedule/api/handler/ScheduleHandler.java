package org.abondar.experimental.schedule.api.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ScheduleHandler {
    private static final Map<String, String> HEADERS = Map.of("Access-Control-Allow-Headers", "Content-Type",
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Methods", "OPTIONS,POST,PUT,GET,DELETE",
            "Access-Control-Allow-Credentials", "true",
            "Content-Type", "application/json"
    );

    protected static final String BUCKET = "td-str";

    protected ObjectMapper mapper;

    protected APIGatewayProxyResponseEvent buildResponse(int code, String body) {
        var resp = new APIGatewayProxyResponseEvent();
        resp.setBody(body);
        resp.setStatusCode(code);
        resp.setHeaders(HEADERS);
        resp.setIsBase64Encoded(true);

        return resp;
    }
}
