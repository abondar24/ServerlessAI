package org.abondar.experimental.todo.api.service;

import org.abondar.experimental.todo.api.data.TodoItem;
import org.junit.jupiter.api.Test;

import static org.abondar.experimental.todo.api.constant.Constants.TABLE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamoUtilTest {

    @Test
    public void buildPutRequestTest() {
         var item = new TodoItem(true,"NOTE");
         var req = DynamoUtil.buildPutItemRequest(item);

         assertEquals(TABLE_NAME,req.getTableName());
         assertEquals(4,req.getItem().size());
    }


}
