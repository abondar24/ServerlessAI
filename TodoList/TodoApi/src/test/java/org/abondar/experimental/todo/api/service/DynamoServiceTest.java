package org.abondar.experimental.todo.api.service;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import org.abondar.experimental.todo.api.data.TodoItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.abondar.experimental.todo.api.constant.Constants.TABLE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DynamoServiceTest {


    private static DynamoService service;

    private static AmazonDynamoDB db;

    @BeforeAll
    public static void initClient() {
        var conf = new AwsClientBuilder
                .EndpointConfiguration("http://localhost:8000", "eu-west-2");

         db = AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(conf)
                .build();


        service = new DynamoServiceImpl(db);
    }

    @BeforeEach
    public void createTable(){
        var req = new CreateTableRequest()
                .withAttributeDefinitions(new AttributeDefinition("id", ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement("id", KeyType.HASH))
                .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
                .withTableName(TABLE_NAME);
        db.createTable(req);
    }

    @AfterEach
    public void dropTable(){
        db.deleteTable(TABLE_NAME);
    }

    @Test
    public void createTest() {
        var item = new TodoItem(false, "note");

        var res = service.createItem(item);
        assertNotNull(res.getId());
        assertNotNull(res.getDueDate());

    }

    @Test
    public void readTest() {
        var item = new TodoItem(false, "note");
        item = service.createItem(item);

        var res = service.readItem(item.getId());
        assertTrue(res.isPresent());

        var found = res.get();
        assertEquals(item.getId(), found.getId());
    }

    @Test
    public void readTestNoItem() {
        var res = service.readItem("id");
        assertFalse(res.isPresent());

    }

    @Test
    public void updateTest() {
        var item = new TodoItem(false, "note");
        item = service.createItem(item);

        item.setNote("newNote");
        var updateItem =service.updateItem(item);
        assertTrue(updateItem);

        var res = service.readItem(item.getId());
        assertTrue(res.isPresent());

        var found = res.get();
        assertEquals(item.getNote(), found.getNote());
    }

    @Test
    public void deleteTest() {
        var item = new TodoItem(false, "note");
        item = service.createItem(item);

        service.deleteItem(item.getId());
        var found = service.readItem(item.getId());
        assertTrue(found.isEmpty());
    }

    @Test
    public void deleteTestNoItem() {
        assertThrows(NullPointerException.class,()->service.deleteItem("id"));
    }

    @Test
    public void listTest() {
        var item = new TodoItem(false, "note");
        service.createItem(item);

        var res = service.listItems();
        System.out.println(res.size());
        assertEquals(1, res.size());
    }
}
