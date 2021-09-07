package org.abondar.experimental.feedback.common.util;

public class Error {

    public static final String ERR_MSG_FORMAT = "{\"Error\": \"%s\"}";

    public static final String MALFORMED_BODY_ERROR = "Malformed request or response body";

    public static final String AWS_NOT_AVAILABLE = "AWS infrastructure not available";

    public static final String KINESIS_RESP_FAILED = "Kinesis record put failed";

    private Error(){}
}
