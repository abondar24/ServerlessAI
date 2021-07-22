package org.abondar.experimental.todo.api.constant;

import java.util.Map;

public class Constants {
    public static final String FRONTEND_URL = "http://td-frontend.s3-website-eu-west-1.amazonaws.com";

    public static final Map<String,String> HEADERS = Map.of("Access-Control-Allow-Headers" , "Content-Type",
            "Access-Control-Allow-Origin", FRONTEND_URL,
            "Access-Control-Allow-Methods", "OPTIONS,POST,PUT,GET,DELETE",
            "Access-Control-Allow-Credentials", "true",
            "Content-Type", "application/json"
            );

    public static final String TABLE_NAME = "TodoList";

    public static final String ID_PARAM = "id";


    private Constants() {
    }
}
