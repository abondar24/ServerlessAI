package org.abondar.experimental.imagerec.crawler;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
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

  }
