package org.abondar.experimental.schedule.api.service;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import org.abondar.experimental.schedule.api.data.TodoItem;
import org.abondar.experimental.schedule.service.DynamoService;

import java.util.List;
import java.util.stream.Collectors;

public class DynamoServiceTestImpl implements DynamoService {

    private final List<TodoItem> scan;

    public DynamoServiceTestImpl(List<TodoItem> scan) {
        this.scan = scan;
    }

    @Override
    public String buildSchedule(String date, String speakDate) {
        return buildSpeech(date,speakDate);
    }


    private String buildSpeech(String date, String speakDate){
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
