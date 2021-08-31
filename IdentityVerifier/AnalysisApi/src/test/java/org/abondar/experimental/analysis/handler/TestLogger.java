package org.abondar.experimental.analysis.handler;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogger implements LambdaLogger {

    private static final Logger logger = LoggerFactory.getLogger(TestLogger.class);

    @Override
    public void log(String message) {
        logger.info(message);
    }

    @Override
    public void log(byte[] message) {
        logger.info(new String(message));
    }
}
