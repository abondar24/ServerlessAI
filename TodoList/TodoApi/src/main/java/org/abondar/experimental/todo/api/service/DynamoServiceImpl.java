package org.abondar.experimental.todo.api.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.abondar.experimental.todo.api.data.TodoItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.abondar.experimental.todo.api.constant.Constants.TABLE_NAME;

public class DynamoServiceImpl implements DynamoService{


    private AmazonDynamoDB db;
    public DynamoServiceImpl(){
      db = AmazonDynamoDBAsyncClientBuilder.defaultClient();
    }

    @Override
    public void createItem(TodoItem item) throws AmazonServiceException {
       var id = UUID.randomUUID().toString();
       item.setId(id);

       var dueDate = DynamoUtil.createDate();
       item.setDueDate(dueDate);

       var request = DynamoUtil.buildPutItemRequest(item);
       db.putItem(request);

    }


    @Override
    public void updateItem(TodoItem item) {
        var request = DynamoUtil.buildUpdateItemRequest(item);
        db.updateItem(request);
    }

    @Override
    public Optional<TodoItem> readItem(String id) {
        var request = DynamoUtil.getItemRequest(id);
        var res = db.getItem(request);
        var item = DynamoUtil.readItemResult(res);
        return Optional.of(item);
    }

    @Override
    public List<TodoItem> listItems() {
        var request = DynamoUtil.getBatchItemRequest();
        var res = db.batchGetItem(request);
        return DynamoUtil.readBatchResult(res);
    }

    @Override
    public void deleteItem(String id) {
        var request = DynamoUtil.deleteItemRequest();
        db.deleteItem(request);
    }
}
