package org.abondar.experimental.todo.api.service;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemRequest;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemResult;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import org.abondar.experimental.todo.api.data.ItemFields;
import org.abondar.experimental.todo.api.data.TodoItem;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.abondar.experimental.todo.api.constant.Constants.TABLE_NAME;

public class DynamoUtil {

    public static String createDate(){
        var date = Calendar.getInstance().getTime();
        var format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return format.format(date);
    }

    public static PutItemRequest buildPutItemRequest(TodoItem item){
        var request  = new PutItemRequest();
        request.setTableName(TABLE_NAME);

        Map<String, AttributeValue> itemVals = Map.of(
                ItemFields.id.toString(),new AttributeValue(item.getId()),
                ItemFields.dueDate.toString(),new AttributeValue(item.getDueDate()),
                ItemFields.checked.toString(),new AttributeValue(String.valueOf(item.getChecked())),
                ItemFields.note.toString(),new AttributeValue(item.getNote())
        );

        request.setItem(itemVals);

        return request;
    }

    public static UpdateItemRequest buildUpdateItemRequest(TodoItem item){
        var request  = new UpdateItemRequest();
        request.setTableName(TABLE_NAME);


        request.addAttributeUpdatesEntry(ItemFields.id.toString(),new AttributeValueUpdate(item.getId()));

        return request;
    }

    public static GetItemRequest getItemRequest(String id) {
         return null;
    }

    public static TodoItem readItemResult(GetItemResult result) {
        return null;
    }

    public static BatchGetItemRequest getBatchItemRequest() {
        return null;
    }

    public static List<TodoItem> readBatchResult(BatchGetItemResult res) {
        return null;
    }

    public static DeleteItemRequest deleteItemRequest() {
        return null;
    }

    private DynamoUtil(){}



}
