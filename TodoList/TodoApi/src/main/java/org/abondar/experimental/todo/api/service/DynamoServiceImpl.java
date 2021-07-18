package org.abondar.experimental.todo.api.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.abondar.experimental.todo.api.constant.Constants.TABLE_NAME;

public class DynamoServiceImpl implements DynamoService {


    private final AmazonDynamoDB db;

    public DynamoServiceImpl() {
        db = AmazonDynamoDBAsyncClientBuilder.defaultClient();
    }

    @Override
    public void createItem(TodoItem item) throws AmazonServiceException {
        var id = UUID.randomUUID().toString();
        item.setId(id);

        var dueDate = createDate();
        item.setDueDate(dueDate);

        var request = buildPutItemRequest(item);
        db.putItem(request);

    }

    private String createDate() {
        var date = Calendar.getInstance().getTime();
        var format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return format.format(date);
    }

    private PutItemRequest buildPutItemRequest(TodoItem item) {
        var request = new PutItemRequest();
        request.setTableName(TABLE_NAME);

        Map<String, AttributeValue> itemVals = Map.of(
                ItemFields.id.toString(), new AttributeValue(item.getId()),
                ItemFields.dueDate.toString(), new AttributeValue(item.getDueDate()),
                ItemFields.checked.toString(), new AttributeValue(String.valueOf(item.getChecked())),
                ItemFields.note.toString(), new AttributeValue(item.getNote())
        );

        request.setItem(itemVals);

        return request;
    }


    @Override
    public void updateItem(TodoItem item) {
        var request = buildUpdateItemRequest(item);
        db.updateItem(request);
    }

    private UpdateItemRequest buildUpdateItemRequest(TodoItem item){
        var request  = new UpdateItemRequest();
        request.setTableName(TABLE_NAME);

        request.addAttributeUpdatesEntry(ItemFields.id.toString(),
                new AttributeValueUpdate(new AttributeValue(item.getId()), AttributeAction.PUT));
        request.addAttributeUpdatesEntry(ItemFields.checked.toString(),
                new AttributeValueUpdate(new AttributeValue(String.valueOf(item.getChecked())), AttributeAction.PUT));
        request.addAttributeUpdatesEntry(ItemFields.note.toString(),
                new AttributeValueUpdate(new AttributeValue(item.getNote()), AttributeAction.PUT));
        request.addAttributeUpdatesEntry(ItemFields.dueDate.toString(),
                new AttributeValueUpdate(new AttributeValue(item.getDueDate()), AttributeAction.PUT));


        return request;
    }

    @Override
    public Optional<TodoItem> readItem(String id) {
        var request = getItemRequest(id);
        var res = db.getItem(request);
        var item = readItemResult(res);
        return Optional.of(item);
    }

    private GetItemRequest getItemRequest(String id) {
        return null;
    }

    private TodoItem readItemResult(GetItemResult result) {
        return null;
    }

    @Override
    public List<TodoItem> listItems() {
        var request = getBatchItemRequest();
        var res = db.batchGetItem(request);
        return readBatchResult(res);
    }

    private BatchGetItemRequest getBatchItemRequest() {
        return null;
    }

    private List<TodoItem> readBatchResult(BatchGetItemResult res) {
        return null;
    }
    

    @Override
    public void deleteItem(String id) {
        var request = deleteItemRequest();
        db.deleteItem(request);
    }

    private DeleteItemRequest deleteItemRequest() {
        return null;
    }

}
