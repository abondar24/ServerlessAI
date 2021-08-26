package org.abondar.experimental.schedule.api.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.abondar.experimental.schedule.service.PollyService;
import org.abondar.experimental.schedule.service.impl.PollyServiceImpl;

import java.io.IOException;

public class PollHandler extends ScheduleHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final PollyService pollyService;

    public PollHandler(){
        this.pollyService = new PollyServiceImpl();
    }

    public PollHandler(PollyService pollyService) {
        this.pollyService = pollyService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var id = getId(input);
        var taskResult = pollyService.pollTask(id);

        try {
            return buildResponse(taskResult);
        } catch (IOException ex){
            return  buildErrorResponse(500,ex.getMessage());
        }
    }

    private String getId(APIGatewayProxyRequestEvent input) {
        var params = input.getPathParameters();
        return params.get("id");
    }
}
