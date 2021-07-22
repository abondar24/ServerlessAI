package org.abondar.experimental.todo.api.constant;

public class Errors {

    public static final String MSG_FORMAT = "{\"Error\": \"%s\"}";
    public static final String MALFORMED_BODY_ERROR = "Malformed request or response body";

    public static final String TABLE_NOT_FOUND = "Requested table not found";

    public static final String AWS_NOT_AVAILABLE = "AWS infrastructure not available";

    public static final String ITEM_NOT_FOUND = "Item not found";

    private Errors(){}
}
