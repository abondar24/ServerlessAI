package org.abondar.experimental.schedule.api.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.abondar.experimental.schedule.service.DynamoService;
import org.abondar.experimental.schedule.service.PollyService;
import org.abondar.experimental.schedule.service.impl.DynamoServiceImpl;
import org.abondar.experimental.schedule.service.impl.PollyServiceImpl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DayHandler extends ScheduleHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final DynamoService dynamoService;

    private final PollyService pollyService;

    public DayHandler(){
        this.dynamoService = new DynamoServiceImpl();
        this.pollyService = new PollyServiceImpl();
    }



    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var date = getDate("yyyy-mm-dd hh:mm:ss");
        var speakDate = getDate("dddd, MMMM Do YYYY");

        var scheduleSpeech = dynamoService.buildSchedule(date,speakDate);
        var taskResult = pollyService.startTask(scheduleSpeech);

        try {
            return buildResponse(taskResult);
        } catch (IOException ex){
            return  buildErrorResponse(500,ex.getMessage());
        }

    }

    private String getDate(String format) {
        var date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}
