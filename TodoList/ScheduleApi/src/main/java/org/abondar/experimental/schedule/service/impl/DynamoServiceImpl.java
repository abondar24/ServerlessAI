package org.abondar.experimental.schedule.service.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import org.abondar.experimental.schedule.api.data.TodoItem;
import org.abondar.experimental.schedule.service.DynamoService;

import java.util.List;
import java.util.stream.Collectors;

public class DynamoServiceImpl implements DynamoService {

    private final DynamoDBMapper dynamoDBMapper;

    public DynamoServiceImpl() {
        var db = AmazonDynamoDBAsyncClientBuilder.standard().build();
        this.dynamoDBMapper = new DynamoDBMapper(db);

    }

    @Override
    public String buildSchedule(String date, String speakDate) {
        var scan =dynamoDBMapper.scan(TodoItem.class,new DynamoDBScanExpression());
        return buildSpeech(date,speakDate,scan);
    }

    private String buildSpeech(String date, String speakDate, List<TodoItem> scan){
        var sb = new StringBuilder();
        sb.append(String.format("<s>Your schedule for  %s</s>",speakDate));

        var scanRes= scan.stream()
                .filter(todoItem -> todoItem.getDueDate().equals(date))
                .collect(Collectors.toList());


        if ( scanRes.size() ==0){
            sb.append("<s>You have no scheduled actions</s>");
        }  else {
            scanRes.forEach(todoItem -> {
                sb.append(String.format("<s>%s</s>",todoItem.getAction()));
                if (!todoItem.getNote().isBlank()){
                    sb.append(String.format("<s>%s</s>",todoItem.getNote()));
                }
            });
        }

        var speech = sb.toString();
        return String.format("<speak>%s</speak>",speech);
    };
}
