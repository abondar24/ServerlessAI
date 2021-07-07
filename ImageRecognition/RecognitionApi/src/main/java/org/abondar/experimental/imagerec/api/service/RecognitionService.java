package org.abondar.experimental.imagerec.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.imagerec.constants.Constants;
import org.abondar.experimental.imagerec.data.Event;
import org.abondar.experimental.imagerec.data.EventMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.io.IOException;

import static org.abondar.experimental.imagerec.constants.Constants.ACC_ID;
import static org.abondar.experimental.imagerec.constants.Constants.ANALYSIS_FILE;
import static org.abondar.experimental.imagerec.constants.Constants.BUCKET_NAME;
import static org.abondar.experimental.imagerec.constants.Constants.REC_QUEUE;

@Service
public class RecognitionService {

    private final Logger logger = LoggerFactory.getLogger(RecognitionService.class);

    public void triggerAnalysis(EventMsg msg) throws IOException {
       logger.info("Triggering SQS service");
       var event = new Event(Constants.DOWNLOAD_ACTION,msg);
       var request = buildMessageRequest(event);

       var sqsClient = buildSqsClient();
       sqsClient.sendMessage(request);
    }



    public String getResults(String domain) throws IOException{
        logger.info("Retrieving results");
        var anlFile = domain + "/" + ANALYSIS_FILE;

        var s3 = buildS3Client();
        var objectRequest = buildObjectRequest(anlFile);
        var resp = s3.getObject(objectRequest).readAllBytes();
        return new String(resp);
    }


    private SqsClient buildSqsClient(){
        return SqsClient.builder()
                .region(Region.EU_WEST_1)
                .build();
    }

    private S3Client buildS3Client() {
        return S3Client.builder()
                .region(Region.EU_WEST_1)
                .build();
    }

    private SendMessageRequest buildMessageRequest(Event event) throws IOException {
        var mapper = new ObjectMapper();
        var msgBody = mapper.writeValueAsString(event);

        var queueUrl = String.format("https://sqs.%s.amazonaws.com/%s/%s",
                Region.EU_WEST_1.toString(), ACC_ID, REC_QUEUE);

        return SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(msgBody)
                .delaySeconds(10)
                .build();
    }

    private GetObjectRequest buildObjectRequest(String anlFile) {
        return GetObjectRequest.builder()
                .key(anlFile)
                .bucket(BUCKET_NAME)
                .build();
    }


}
