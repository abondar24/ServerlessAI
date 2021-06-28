package org.abondar.experimental.imagerec.crawler;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.abondar.experimental.imagerec.crawler.Constants.ACC_ID;
import static org.abondar.experimental.imagerec.crawler.Constants.ANALYZE_ACTION;
import static org.abondar.experimental.imagerec.crawler.Constants.ANL_QUEUE;
import static org.abondar.experimental.imagerec.crawler.Constants.BUCKET_KEY;
import static org.abondar.experimental.imagerec.crawler.Constants.BUCKET_NAME;
import static org.abondar.experimental.imagerec.crawler.Constants.DOWNLOAD_ACTION;

public class Handler implements RequestHandler<SQSEvent, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String handleRequest(SQSEvent input, Context context) {
        var logger = context.getLogger();

        var recs = input.getRecords();
        if (recs != null) {
            recs.forEach(rec -> {
                var body = rec.getBody();
                logger.log(body);

                try {
                    var event = mapper.readValue(body, Event.class);

                    if (event.getAction().equals(DOWNLOAD_ACTION) && event.getMsg() != null) {
                        var url = new URL(event.getMsg().getUrl());
                        crawlImage(url, context);
                        sendToAnalysis(url);
                    }
                } catch (Exception ex) {
                    logger.log(String.format("Error reading message body: %s", ex.getMessage()));
                }

            });
        }

        return null;
    }


    private void crawlImage(URL url, Context context) throws IOException {
        context.getLogger().log(String.format("crawling %s", url.getHost()));

        var doc = Jsoup.connect(url.toString()).post();
        var elems = doc.getElementsByClass("block");

        for (org.jsoup.nodes.Element blockElem : elems) {
            var img = blockElem.getElementsByTag("img");
            var imgUrl = new URL(img.attr("src"));
            saveImage(new File(imgUrl.getFile()));
        }
    }


    private void saveImage(File file) {
        var s3 = S3Client.builder()
                .region(Region.EU_WEST_1)
                .build();

        s3.putObject(buildPutRequest(), RequestBody.fromFile(file));
    }

    private PutObjectRequest buildPutRequest(){
        return PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(BUCKET_KEY)
                .build();
    }

    private void sendToAnalysis(URL url) throws IOException {
        var queueUrl = String.format("https://sqs.%s.amazonaws.com/%s/%s",
                Region.EU_WEST_1.toString(), ACC_ID, ANL_QUEUE);

        var msgBody = buildBody(url.getHost());

        var sqs = SqsClient.builder()
                .region(Region.EU_WEST_1)
                .build();

        sqs.sendMessage(buildRequest(queueUrl,msgBody));

    }

    private String buildBody(String domain) throws IOException{
        var event = new Event();
        var msg = new EventMsg();

        msg.setUrl(domain);
        event.setAction(ANALYZE_ACTION);
        event.setMsg(msg);

        return mapper.writeValueAsString(event);
    }

    private SendMessageRequest buildRequest(String queueUrl,String msgBody){
       return SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(msgBody)
                .delaySeconds(10)
                .build();
    }
}
