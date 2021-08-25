package org.abondar.experimental.schedule.api.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.abondar.experimental.schedule.util.Constants.HEADERS;

public class ScheduleHandler {

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
