package org.abondar.experimental.analysis.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.abondar.experimental.analysis.data.UploadResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.Instant;
import java.util.UUID;

public class UploadHandler extends IdentityAnalysisHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    private final S3Client s3 = S3Client.builder()
            .region(Region.EU_WEST_1)
            .build();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var logger = context.getLogger();

        var file = input.getBody().getBytes();
        var uuid = UUID.randomUUID().toString();
        var key = getObjectKey(uuid);

        logger.log(String.format("Generating image url for %s", key));
        var body = RequestBody.fromBytes(file);
        s3.putObject(buildObjectRequest(key), body);

        var uploadResponse = new UploadResponse(uuid);

        return buildResponse(uploadResponse);
    }


    private PutObjectRequest buildObjectRequest(String key) {
        return PutObjectRequest.builder()
                .key(key)
                .expires(Instant.MAX)
                .bucket(BUCKET)
                .build();
    }

}
