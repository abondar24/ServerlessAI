package org.abondar.experimental.imagerec.analyzer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

public class Handler implements RequestHandler<SQSEvent, String> {
    @Override
    public String handleRequest(SQSEvent input, Context context) {
        return null;
    }
}
