package org.abondar.experimental.schedule.api.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.schedule.api.data.TaskResult;

import java.io.IOException;
import java.util.Map;

import static org.abondar.experimental.schedule.util.Constants.HEADERS;

public class ScheduleHandler {


    protected APIGatewayProxyResponseEvent buildResponse(TaskResult body) throws IOException {
        var resp = new APIGatewayProxyResponseEvent();
        var mapper = new ObjectMapper();
        resp.setBody(mapper.writeValueAsString(body));
        resp.setStatusCode(200);
        resp.setHeaders(HEADERS);
        resp.setIsBase64Encoded(true);

        return resp;
    }

    protected APIGatewayProxyResponseEvent buildErrorResponse(int code, String msg)  {
        var resp = new APIGatewayProxyResponseEvent();
        resp.setBody(msg);
        resp.setStatusCode(code);
        resp.setHeaders(HEADERS);
        resp.setIsBase64Encoded(true);

        return resp;
    }
}
