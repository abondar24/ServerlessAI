package org.abondar.experimental.todo.api.handler;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import org.abondar.experimental.todo.api.data.TodoItem;
import org.abondar.experimental.todo.api.service.DynamoService;

import java.io.IOException;


import static org.abondar.experimental.todo.api.constant.Constants.ID_PARAM;
import static org.abondar.experimental.todo.api.constant.Errors.AWS_NOT_AVAILABLE;
import static org.abondar.experimental.todo.api.constant.Errors.ITEM_NOT_FOUND;
import static org.abondar.experimental.todo.api.constant.Errors.MALFORMED_BODY_ERROR;
import static org.abondar.experimental.todo.api.constant.Errors.TABLE_NOT_FOUND;

public class ReadHandler extends BaseHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public ReadHandler() {
        super();
    }

    public ReadHandler(DynamoService service) {
        super(service);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var logger = context.getLogger();

        var id = getId(input);
        logger.log(String.format("Looking for value with id %s",id));

        try {
            var item = service.readItem(id);
            if (item.isEmpty()){
                logger.log(String.format("No items found with id %s",id));
                return buildResponse(404, ITEM_NOT_FOUND);
            }

            var resp = buildResponse(200, item.get());
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
