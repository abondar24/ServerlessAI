package org.abondar.experimental.note.api.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.transcribe.model.GetTranscriptionJobRequest;

import java.io.IOException;

import static org.abondar.experimental.note.api.util.Errors.JOB_NOT_FOUND;
import static org.abondar.experimental.note.api.util.Errors.MSG_FORMAT;

public class PollHandler extends NoteHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var logger = context.getLogger();

        var id = getId(input);
        logger.log(String.format("Polling job with id %s", id));

        var response = client.getTranscriptionJob(getJobRequest(id));
        if (response != null) {
            var job = response.transcriptionJob();
            if (job != null) {
                try {
                    var transcriptResult = getTranscriptResult(id);

                    return buildResponse(200, transcriptResult);
                } catch (IOException ex) {
                    logger.log(ex.getMessage());
                    return buildResponse(501, ex.getMessage());
                }
            } else {
                logger.log(String.format("Job with id %s not found", id+".json"));
                return buildResponse(404, String.format(MSG_FORMAT, JOB_NOT_FOUND));
            }
        }

        return null;
    }


    private GetTranscriptionJobRequest getJobRequest(String id) {
        return GetTranscriptionJobRequest.builder()
                .transcriptionJobName(id)
                .build();
    }

    public String getTranscriptResult(String transcriptUri) throws IOException {
        var s3 = buildS3Client();
        var objectRequest = buildObjectRequest(transcriptUri);
        var resp = s3.getObject(objectRequest).readAllBytes();
        return new String(resp);
    }

    private S3Client buildS3Client() {
        return S3Client.builder()
                .region(Region.EU_WEST_1)
                .build();
    }

    private GetObjectRequest buildObjectRequest(String transcriptUri) {
        return GetObjectRequest.builder()
                .key(transcriptUri)
                .bucket(BUCKET)
                .build();
    }


}
