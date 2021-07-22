package org.abondar.experimental.todo.api.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.abondar.experimental.todo.api.data.TodoItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class DynamoServiceImpl implements DynamoService {


    private final DynamoDBMapper dynamoDBMapper;

    public DynamoServiceImpl() {
        var db = AmazonDynamoDBAsyncClientBuilder.standard().build();
        dynamoDBMapper = new DynamoDBMapper(db);
    }

    public DynamoServiceImpl(AmazonDynamoDB db){
        dynamoDBMapper = new DynamoDBMapper(db);
    }


    @Override
    public TodoItem createItem(TodoItem item) throws AmazonServiceException {
        var id = UUID.randomUUID().toString();
        item.setId(id);

        var dueDate = createDate();
        item.setDueDate(dueDate);

        dynamoDBMapper.save(item);
        return item;
    }

    private String createDate() {
        var date = Calendar.getInstance().getTime();
        var format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return format.format(date);
    }


    @Override
    public boolean updateItem(TodoItem item) {
        var found = readItem(item.getId());
        if (found.isEmpty()){
            return false;
        }
        dynamoDBMapper.save(item);
        return true;
    }


    @Override
    public Optional<TodoItem> readItem(String id) {
           var item = dynamoDBMapper.load(TodoItem.class,id);
           if (item==null){
               return Optional.empty();
           } else {
               return Optional.of(item);
           }
    }


    @Override
    public List<TodoItem> listItems() {
        var scan =dynamoDBMapper.scan(TodoItem.class,new DynamoDBScanExpression());
        return scan.stream()
                .parallel()
                .collect(Collectors.toList());
    }


    @Override
    public void deleteItem(String id) {
        var item = dynamoDBMapper.load(TodoItem.class,id);
        dynamoDBMapper.delete(item);
    }


}
