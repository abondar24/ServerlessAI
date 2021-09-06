package org.abondar.experimental.schedule.api.handler;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.io.IOException;

import static org.abondar.experimental.schedule.util.Constants.AWS_NOT_AVAILABLE;
import static org.abondar.experimental.schedule.util.Constants.MSG_FORMAT;

public class PollHandler extends ScheduleHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var id = getId(input);
        var taskResult = pollyService.pollTask(id);

        try {
            return buildResponse(taskResult);
        } catch (IOException ex){
            return  buildErrorResponse(500,ex.getMessage());
        } catch (AmazonServiceException ex) {
            return buildErrorResponse(502, String.format(MSG_FORMAT,AWS_NOT_AVAILABLE));
        }
    }

    private String getId(APIGatewayProxyRequestEvent input) {
        var params = input.getPathParameters();
        return params.get("id");
    }
}
