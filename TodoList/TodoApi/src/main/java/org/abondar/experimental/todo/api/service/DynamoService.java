package org.abondar.experimental.todo.api.service;

import org.abondar.experimental.todo.api.data.TodoItem;

import java.util.List;
import java.util.Optional;

public interface DynamoService {

    TodoItem createItem(TodoItem item);

    void updateItem(TodoItem item);

    Optional<TodoItem> readItem(String id);

    List<TodoItem> listItems();

    void deleteItem(String id);

}
