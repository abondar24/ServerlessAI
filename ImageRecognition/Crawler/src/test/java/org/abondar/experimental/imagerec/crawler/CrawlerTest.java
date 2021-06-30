package org.abondar.experimental.imagerec.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({MockitoExtension.class})
public class CrawlerTest {


    @Test
    public void jsonParseTest() throws Exception {
        var mapper = new ObjectMapper();

        var eventBody = "{\n" +
                "\"action\": \"download\",\n" +
                "\"msg\": {\n" +
                "\"url\": \"http://s3-website-eu-west-1.amazonaws.com\"\n" +
                "}" + "}";

        var event = mapper.readValue(eventBody, Event.class);

        assertEquals("download", event.getAction());
        assertEquals("http://s3-website-eu-west-1.amazonaws.com", event.getMsg().getUrl());
    }

}
