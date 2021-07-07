package org.abondar.experimental.imagerec.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.abondar.experimental.imagerec.data.Event;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({MockitoExtension.class})
public class CrawlerTest {


    @Test
    public void jsonParseTest() throws Exception {
        var mapper = new ObjectMapper();

        var eventBody = "{\"action\": \"download\" ,\"msg\": {\"url\": \"http://s3-website-eu-west-1.amazonaws.com\" }}";

        var event = mapper.readValue(eventBody, Event.class);

        assertEquals("download", event.getAction());
        assertEquals("http://s3-website-eu-west-1.amazonaws.com", event.getMsg().getUrl());
    }

    @Test
    public void crawlTest() throws Exception{
        var page = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>AI as a Service Example Images</title>\n" +
                "    <meta name=\"description\" content=\"Example images for AI as a Service\">\n" +
                "    <meta name=\"author\" content=\"Peter Elger\">\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <h1> Example images for Chapter 2 AI as a Service </h1>\n" +
                "      <img src='cat1.png' alt='cat' width='100'>\n" +
                "      <img src='cat2.jpg' alt='cat' width='100'>\n" +
                "      <img src='cat3.jpg' alt='cat' width='100'>\n" +
                "      <img src='cat4.jpg' alt='cat' width='100'>\n" +
                "      <img src='dog1.jpg' alt='dog' width='100'>\n" +
                "      <img src='dog2.jpg' alt='dog' width='100'>\n" +
                "      <img src='dog3.jpeg' alt='dog' width='100'>\n" +
                "      <img src='dog4.jpg' alt='dog' width='100'>\n" +
                "  </body>\n" +
                "</html>";

        var res = crawlImage(page);


        assertTrue(res.size()>0);
        assertEquals(8,res.size());
        System.out.println(res.get(0).getName());
    }

    private List<File> crawlImage(String page) throws IOException {
        var doc = Jsoup.parse(page);
        var elems = doc.getElementsByTag("img");
        var host = "http://s3-website-eu-west-1.amazonaws.com";
        List<File> res = new ArrayList<>();
        for (org.jsoup.nodes.Element imgElem : elems) {
            var imgUrl = new URL(host+"/"+imgElem.attr("src"));
            res.add(new File(imgUrl.getFile()));
        }

        return res;
    }

}
