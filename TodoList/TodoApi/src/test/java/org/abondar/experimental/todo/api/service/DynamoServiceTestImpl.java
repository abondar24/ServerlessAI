package org.abondar.experimental.todo.api.service;

import org.abondar.experimental.todo.api.data.TodoItem;

import java.util.List;
import java.util.Optional;

public class DynamoServiceTestImpl implements DynamoService{
    @Override
    public TodoItem createItem(TodoItem item) {
        return item;
    }

    @Override
    public void updateItem(TodoItem item) {

    }

    @Override
    public Optional<TodoItem> readItem(String id) {
        return Optional.of(new TodoItem());
    }

    @Override
    public List<TodoItem> listItems() {
        return List.of(new TodoItem());
    }

    @Override
    public void deleteItem(String id) {

    }
}
