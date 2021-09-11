package org.abondar.experimental.feedback.common.util;

import java.util.Map;

public class Constant {

    public static final Map<String, String> HEADERS = Map.of("Access-Control-Allow-Headers", "Content-Type",
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Methods", "OPTIONS,POST,PUT,GET,DELETE",
            "Access-Control-Allow-Credentials", "true",
            "Content-Type", "application/json"
    );

    public static final String TRAIN_BUCKET = "feedback-train";

    public static final String RESULT_BUCKET = "feedback-result";

    public static final String TRANSLATE_STREAM = "feedback-str";

    public static final String SENTIMENT_STREAM = "feedback-sent";

    public static final String CLASSIFY_STREAM = "feedback-cls";

    public static final String CLASSIFIER = "feedback-filter";

    public static final String CLASSIFIER_ENDPOINT_ARN = "arn:aws:comprehend:eu-west-1:203212890819:document-classifier-endpoint/"+CLASSIFIER;

    public static final String CLASSIFIER_ROLE_ARN = "arn:aws:iam::203212890819:role/classifier_role";

    public static final Float  CLASS_SCORE = 0.85f;

    private Constant(){}
}
