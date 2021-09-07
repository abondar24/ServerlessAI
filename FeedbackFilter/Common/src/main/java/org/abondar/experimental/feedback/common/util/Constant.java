package org.abondar.experimental.feedback.common.util;

import java.util.Map;

public class Constant {

    public static final Map<String, String> HEADERS = Map.of("Access-Control-Allow-Headers", "Content-Type",
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Methods", "OPTIONS,POST,PUT,GET,DELETE",
            "Access-Control-Allow-Credentials", "true",
            "Content-Type", "application/json"
    );

    protected static final String BUCKET = "";



    private Constant(){}
}