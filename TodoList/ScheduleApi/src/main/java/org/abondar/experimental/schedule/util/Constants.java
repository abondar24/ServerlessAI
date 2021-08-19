package org.abondar.experimental.schedule.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Constants {
    public static final String TABLE_NAME = "TodoList";

   public static final Map<String, String> HEADERS = Map.of("Access-Control-Allow-Headers", "Content-Type",
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Methods", "OPTIONS,POST,PUT,GET,DELETE",
            "Access-Control-Allow-Credentials", "true",
            "Content-Type", "application/json"
    );

    public static final String BUCKET = "td-str";

    private Constants(){}
}
