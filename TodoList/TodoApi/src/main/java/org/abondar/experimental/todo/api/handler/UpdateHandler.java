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

import static org.abondar.experimental.todo.api.constant.Errors.AWS_NOT_AVAILABLE;
import static org.abondar.experimental.todo.api.constant.Errors.MALFORMED_BODY_ERROR;
import static org.abondar.experimental.todo.api.constant.Errors.TABLE_NOT_FOUND;


public class UpdateHandler extends BaseHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public UpdateHandler(DynamoService service) {
        super(service);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var logger = context.getLogger();

        var body = input.getBody();
        logger.log(body);

        try {
            var item = mapper.readValue(body, TodoItem.class);
            service.updateItem(item);

            var resp = buildResponse(200, body);
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
