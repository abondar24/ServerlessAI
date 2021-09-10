package org.abondar.experimental.feedback.common.util;

public class Error {

    public static final String ERR_MSG_FORMAT = "{\"Error\": \"%s\"}";

    public static final String AWS_NOT_AVAILABLE = "AWS infrastructure not available";

    public static final String KINESIS_RESP_FAILED = "Kinesis record put failed";

    public static final String WRONG_TRAINER_KEY = "Enter key -e or -c to continue";

    public static final String WRONG_CLIENT_KEY = "Enter key -u or -c to continue";

    public static final String WRONG_NUMBER_OF_ARGUMENTS = "Wrong number of arguments";

    public static final String WRONG_INPUT = "Unsupported input";
    private Error(){}
}
