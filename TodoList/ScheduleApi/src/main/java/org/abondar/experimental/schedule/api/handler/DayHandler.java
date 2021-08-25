package org.abondar.experimental.schedule.api.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.abondar.experimental.schedule.service.DynamoService;
import org.abondar.experimental.schedule.service.PollyService;

public class DayHandler extends ScheduleHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private DynamoService dynamoService;

    private PollyService pollyService;

    public DayHandler(){}

    public DayHandler(DynamoService dynamoService, PollyService pollyService) {
        this.dynamoService = dynamoService;
        this.pollyService = pollyService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        return null;
    }
}
