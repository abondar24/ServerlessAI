package org.abondar.experimental.feedback.api.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import static org.abondar.experimental.feedback.common.util.Constant.HEADERS;
import static org.abondar.experimental.feedback.common.util.Constant.TRANSLATE_STREAM;
import static org.abondar.experimental.feedback.common.util.Error.AWS_NOT_AVAILABLE;
import static org.abondar.experimental.feedback.common.util.Error.ERR_MSG_FORMAT;
import static org.abondar.experimental.feedback.common.util.Error.KINESIS_RESP_FAILED;

public class FeedbackApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final KinesisAsyncClient kinesisClient;

    public FeedbackApiHandler(){
        this.kinesisClient = KinesisAsyncClient.builder()
                .build();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var logger = context.getLogger();
        try {
            var req = buildRecordRequest(input.getBody());
            logger.log(String.format("Putting record %s",req.toString()));
            var resp = kinesisClient.putRecord(req).get();

            return buildResponse(200,resp.encryptionTypeAsString());
        } catch (AwsServiceException ex) {
            logger.log(ex.getMessage());
            return buildResponse(502, String.format(ERR_MSG_FORMAT, AWS_NOT_AVAILABLE));
        } catch (InterruptedException | ExecutionException ex){
            logger.log(ex.getMessage());
            return buildResponse(500, String.format(ERR_MSG_FORMAT, KINESIS_RESP_FAILED));
        }

    }


    private PutRecordRequest buildRecordRequest(String eventBody) {
        return PutRecordRequest.builder()
                .data(SdkBytes.fromString(eventBody, StandardCharsets.UTF_8))
                .partitionKey("1")
                .streamName(TRANSLATE_STREAM)
                .build();
    }


    protected APIGatewayProxyResponseEvent buildResponse(int code, String msg) {
        var resp = new APIGatewayProxyResponseEvent();
        resp.setBody(msg);
        resp.setStatusCode(code);
        resp.setHeaders(HEADERS);
        resp.setIsBase64Encoded(true);

        return resp;
    }
}
