package org.abondar.experimental.imagerec.crawler;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

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
                        var domain = createUniqueDomain(event.getMsg().getUrl());
                    }
                } catch (Exception ex) {
                    logger.log(String.format("Error reading message body: %s", ex.getMessage()));
                }

            });
        }

        return null;
    }
}
