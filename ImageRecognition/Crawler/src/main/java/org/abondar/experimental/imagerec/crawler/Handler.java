package org.abondar.experimental.imagerec.crawler;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.abondar.experimental.imagerec.constants.Constants;
import org.abondar.experimental.imagerec.data.Event;
import org.abondar.experimental.imagerec.data.EventMsg;
import org.jsoup.Jsoup;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;


public class Handler implements RequestHandler<SQSEvent, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String handleRequest(SQSEvent input, Context context) {
        var logger = context.getLogger();

        var recs = input.getRecords();
        if (recs != null) {
            recs.forEach(rec -> {
                var body = rec.getBody();

                try {
                    var event = mapper.readValue(body, Event.class);

                    if (event.getAction().equals(Constants.DOWNLOAD_ACTION) && event.getMsg() != null) {
                        var url = new URL(event.getMsg().getUrl());
                        crawlImage(event.getMsg().getUrl(), logger);
                        sendToAnalysis(url, logger);
                    }
                } catch (Exception ex) {
                    logger.log(ex.getMessage());
                }

            });
            return "Crawling complete";
        }

        return null;
    }


    private void crawlImage(String url, LambdaLogger logger) throws IOException {
        logger.log(String.format("crawling %s\n", url));

        var doc = Jsoup.connect(url).get();
        var elems = doc.getElementsByTag("img");

        for (org.jsoup.nodes.Element imgElem : elems) {
            var imgUrl = new URL(url + "/" + imgElem.attr("src"));
            logger.log(String.format("Downloading image from %s\n", imgUrl));

            var fileKey = imgUrl.toString().replace("http://","");
            saveImage(getImage(imgUrl),fileKey);
        }
    }

    private byte[] getImage(URL url) throws IOException {
        var bos = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int bytesRead;

        var is = url.openStream();
        while ((bytesRead = is.read(chunk)) > 0) {
            bos.write(chunk, 0, bytesRead);
        }

        return bos.toByteArray();

    }

    private void saveImage(byte[] file,String fileKey) {
        var s3 = S3Client.builder()
                .region(Region.EU_WEST_1)
                .build();


        s3.putObject(buildPutRequest(fileKey), RequestBody.fromBytes(file));
    }

    private PutObjectRequest buildPutRequest(String fileName) {
        return PutObjectRequest.builder()
                .bucket(Constants.BUCKET_NAME)
                .key(fileName)
                .build();
    }

    private void sendToAnalysis(URL url, LambdaLogger logger) throws IOException {
        var queueUrl = String.format("https://sqs.%s.amazonaws.com/%s/%s",
                Region.EU_WEST_1.toString(), Constants.ACC_ID, Constants.ANL_QUEUE);

        var msgBody = buildBody(url.getHost());

        var sqs = SqsClient.builder()
                .region(Region.EU_WEST_1)
                .build();

        logger.log(String.format("Sending for analysis %s\n", msgBody));
        sqs.sendMessage(buildRequest(queueUrl, msgBody));

    }

    private String buildBody(String domain) throws IOException {
        var msg = new EventMsg();
        msg.setUrl(domain);

        var event = new Event(Constants.ANALYZE_ACTION,msg);

        return mapper.writeValueAsString(event);
    }

    private SendMessageRequest buildRequest(String queueUrl, String msgBody) {
        return SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(msgBody)
                .delaySeconds(10)
                .build();
    }
}
