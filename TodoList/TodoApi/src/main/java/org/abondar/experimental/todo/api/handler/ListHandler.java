package org.abondar.experimental.todo.api.handler;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;

import java.io.IOException;

import static org.abondar.experimental.todo.api.constant.Constants.ID_PARAM;
import static org.abondar.experimental.todo.api.constant.Errors.AWS_NOT_AVAILABLE;
import static org.abondar.experimental.todo.api.constant.Errors.ITEM_NOT_FOUND;
import static org.abondar.experimental.todo.api.constant.Errors.MALFORMED_BODY_ERROR;
import static org.abondar.experimental.todo.api.constant.Errors.TABLE_NOT_FOUND;

public class ListHandler extends BaseHandler
        implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayV2HTTPEvent input, Context context) {
        var logger = context.getLogger();
        try {
            var items = service.listItems();
            var resp = buildResponse(200, items);
            logger.log(resp.toString());

            return resp;
        } catch (IOException ex) {
            logger.log(ex.getMessage());
            return buildResponse(500, MALFORMED_BODY_ERROR);
        } catch (ResourceNotFoundException ex) {
            logger.log(ex.getMessage());
            return buildResponse(404, TABLE_NOT_FOUND);
        } catch (AmazonServiceException ex) {
            logger.log(ex.getMessage());
            return buildResponse(502, AWS_NOT_AVAILABLE);
        }
    }
}
