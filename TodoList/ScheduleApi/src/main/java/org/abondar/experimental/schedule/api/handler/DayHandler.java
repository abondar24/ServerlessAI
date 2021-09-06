package org.abondar.experimental.schedule.api.handler;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.abondar.experimental.schedule.service.DynamoService;
import org.abondar.experimental.schedule.service.impl.DynamoServiceImpl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.abondar.experimental.schedule.util.Constants.AWS_NOT_AVAILABLE;
import static org.abondar.experimental.schedule.util.Constants.MSG_FORMAT;
import static org.abondar.experimental.schedule.util.Constants.TABLE_NOT_FOUND;

public class DayHandler extends ScheduleHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final DynamoService dynamoService;

    public DayHandler() {
        super();
        this.dynamoService = new DynamoServiceImpl();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var date = getDate("MM/dd/yyyy");
        var speakDate = getDate("EEEE, MMMM dd YYYY");


        try {
            var scheduleSpeech = dynamoService.buildSchedule(date, speakDate);
            var taskResult = pollyService.startTask(scheduleSpeech);
            return buildResponse(taskResult);
        } catch (IOException ex) {
            return buildErrorResponse(500, ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            return buildErrorResponse(404, String.format(MSG_FORMAT, TABLE_NOT_FOUND));
        } catch (AmazonServiceException ex) {
            return buildErrorResponse(502, String.format(MSG_FORMAT, AWS_NOT_AVAILABLE));
        }

    }

    private String getDate(String format) {
        var date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}
