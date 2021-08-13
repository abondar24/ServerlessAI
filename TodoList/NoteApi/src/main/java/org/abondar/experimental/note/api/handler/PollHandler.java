package org.abondar.experimental.note.api.handler;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest;

import java.io.IOException;

import static org.abondar.experimental.note.api.util.Errors.AWS_NOT_AVAILABLE;
import static org.abondar.experimental.note.api.util.Errors.JOB_NOT_FOUND;
import static org.abondar.experimental.note.api.util.Errors.MALFORMED_BODY_ERROR;
import static org.abondar.experimental.note.api.util.Errors.MSG_FORMAT;

public class PollHandler  extends NoteHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var logger = context.getLogger();

        var id = getId(input);

        logger.log(String.format("Polling job with id %s",id));

        var res = client.getTranscriptionJob(getJobRequest(id));

            if (res!=null){
                var job = res.getTranscriptionJob();
                if (job!=null){
                    if (job.getTranscriptionJobStatus().equals("COMPLETED")){
                        logger.log(String.format("Job Status %s",job.getTranscriptionJobStatus()));
                        //TODO make response body with json format below
                        //TODO {transcribeStatus: 'status', transcript: [transcripts]  }
                        return buildResponse(200,job.getTranscriptionJobStatus());
                    }
                } else {
                    logger.log(String.format("Job with id %s not found",id));
                    return buildResponse(404, String.format(MSG_FORMAT, JOB_NOT_FOUND));
                }
            }

        return null;
    }


    private GetTranscriptionJobRequest getJobRequest(String id){
        var req = new GetTranscriptionJobRequest();
        req.setTranscriptionJobName(id);

        return req;
    }



}
