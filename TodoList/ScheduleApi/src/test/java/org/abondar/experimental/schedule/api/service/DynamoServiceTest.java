package org.abondar.experimental.schedule.api.service;

import org.abondar.experimental.schedule.api.data.TodoItem;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamoServiceTest {


    @Test
    public void buildSpeechTest() {
        var date = getDate();

        var item = new TodoItem();
        item.setDueDate(date);
        item.setAction("Some Action");
        item.setNote("Some Note");

        List<TodoItem> itemList = List.of(item);

        var service = new DynamoServiceTestImpl(itemList);
        var res = service.buildSchedule(date,date);
        System.out.println(res);
        assertFalse(res.isEmpty());
        assertTrue(res.contains(item.getAction()));
        assertTrue(res.contains(item.getNote()));

    }

    @Test
    public void buildSpeechNoDateTest() {
        var date = getDate();

        var item = new TodoItem();
        item.setDueDate("");
        item.setAction("Some Action");
        item.setNote("Some Note");

        List<TodoItem> itemList = List.of(item);

        var service = new DynamoServiceTestImpl(itemList);
        var res = service.buildSchedule(date,date);
        System.out.println(res);
        assertFalse(res.isEmpty());
        assertTrue(res.contains("You have no scheduled actions"));

    }

    @Test
    public void buildSpeechNoNoteTest() {
        var date = getDate();

        var item = new TodoItem();
        item.setDueDate(date);
        item.setAction("Some Action");
        item.setNote("");

        List<TodoItem> itemList = List.of(item);

        var service = new DynamoServiceTestImpl(itemList);
        var res = service.buildSchedule(date,date);
        System.out.println(res);
        assertFalse(res.isEmpty());
        assertTrue(res.contains(item.getAction()));

    }

    private String getDate() {
        var date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return dateFormat.format(date);
    }

}
