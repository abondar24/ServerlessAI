package org.abondar.experimental.todo.api.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.todo.api.constant.Constants;
import org.abondar.experimental.todo.api.service.DynamoService;
import org.abondar.experimental.todo.api.service.DynamoServiceImpl;

import java.io.IOException;

import static org.abondar.experimental.todo.api.constant.Constants.ID_PARAM;

public class BaseHandler {

    protected DynamoService service = new DynamoServiceImpl();

    protected ObjectMapper mapper = new ObjectMapper();

    protected APIGatewayProxyResponseEvent buildResponse(int code,String body){
        var resp = new APIGatewayProxyResponseEvent();
        resp.setBody(body);
        resp.setStatusCode(code);
        resp.setHeaders(Constants.HEADERS);

        return resp;
    }

    protected <T>APIGatewayProxyResponseEvent buildResponse(int code,T body) throws IOException {
        var resp = new APIGatewayProxyResponseEvent();
        var respBody = mapper.writeValueAsString(body);

        resp.setBody(respBody);
        resp.setStatusCode(code);
        resp.setHeaders(Constants.HEADERS);

        return resp;
    }

    protected String getId(APIGatewayV2HTTPEvent input){
        var params = input.getPathParameters();
        return params.get(ID_PARAM);
    }
}
