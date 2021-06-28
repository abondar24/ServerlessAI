package org.abondar.experimental.imagerec.crawler;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CrawlerTest {

    @Test
    public void jsonParseTest() throws Exception{
        var mapper = new ObjectMapper();

        var eventBody = "{\n" +
                "\"action\": \"download\",\n" +
                "\"msg\": {\n" +
                "\"url\": \"http://s3-website-eu-west-1.amazonaws.com\"\n" +
                "}" + "}";

        var event = mapper.readValue(eventBody,Event.class);

        assertEquals("download",event.getAction());
        assertEquals("http://s3-website-eu-west-1.amazonaws.com",event.getMsg().getUrl());
    }

    @Test
    public void testHandler(){
        var context = new TestContext();
        var handler = new Handler();

        var eventBody = "{\n" +
                "\"action\": \"download\",\n" +
                "\"msg\": {\n" +
                "\"url\": \"http://s3-website-eu-west-1.amazonaws.com\"\n" +
                "}" + "}";

        var event = new SQSEvent();
        var msg = new SQSEvent.SQSMessage();
        msg.setBody(eventBody);
        event.setRecords(List.of(msg));

        var res = handler.handleRequest(event,context);
        assertTrue(res.contains("OK"));
    }
}
