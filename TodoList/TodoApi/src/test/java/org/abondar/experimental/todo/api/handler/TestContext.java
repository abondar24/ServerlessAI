package org.abondar.experimental.todo.api.handler;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class TestContext implements Context {
    @Override
    public String getAwsRequestId() {
        return "test-id";
    }

    @Override
    public String getLogGroupName() {
        return "/aws/lambda/"+getFunctionName();
    }

    @Override
    public String getLogStreamName() {
        return "test-log";
    }

    @Override
    public String getFunctionName() {
        return "JavaDemo";
    }

    @Override
    public String getFunctionVersion() {
        return new String("$LATEST");
    }

    @Override
    public String getInvokedFunctionArn() {
        return null;
    }

    @Override
    public CognitoIdentity getIdentity() {
        return null;
    }

    @Override
    public ClientContext getClientContext() {
        return null;
    }

    @Override
    public int getRemainingTimeInMillis() {
        return 300000000;
    }

    @Override
    public int getMemoryLimitInMB() {
        return 256;
    }

    @Override
    public LambdaLogger getLogger() {
        return new TestLogger();
    }
}
