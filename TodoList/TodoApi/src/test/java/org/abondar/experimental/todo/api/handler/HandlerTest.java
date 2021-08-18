package org.abondar.experimental.todo.api.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.todo.api.data.TodoItem;
import org.abondar.experimental.todo.api.service.DynamoService;
import org.abondar.experimental.todo.api.service.DynamoServiceTestImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;

import java.util.Map;

import static org.abondar.experimental.todo.api.constant.Constants.FRONTEND_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HandlerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private TestContext context;

    private static DynamoService service;

    @BeforeAll
    public static void init(){
        service = new DynamoServiceTestImpl();
    }

    @BeforeEach
    public void initContext() {
        this.context = new TestContext();
    }

    @Test
    public void createHandlerTest() throws Exception {
        var item = new TodoItem(false, "note");
        var requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody(mapper.writeValueAsString(item));

        var handler = new CreateHandler();
        FieldSetter.setField(handler, handler.getClass()
                .getSuperclass().getDeclaredField("service"), service);

        var result = handler.handleRequest(requestEvent, context);
        var resp = mapper.readValue(result.getBody(), TodoItem.class);

        assertEquals(200, result.getStatusCode());
        assertEquals(5, result.getHeaders().size());
        assertTrue(result.getHeaders().containsKey("Access-Control-Allow-Origin"));
        assertEquals(FRONTEND_URL, result.getHeaders().get("Access-Control-Allow-Origin"));
        assertEquals(item.getNote(), resp.getNote());
    }

    @Test
    public void updateHandlerTest() throws Exception {
        var item = new TodoItem(false, "note");
        var requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setBody(mapper.writeValueAsString(item));

        var handler = new UpdateHandler();
        FieldSetter.setField(handler, handler.getClass()
                .getSuperclass().getDeclaredField("service"), service);

        var result = handler.handleRequest(requestEvent, context);
        var resp = mapper.readValue(result.getBody(), TodoItem.class);

        assertEquals(200, result.getStatusCode());
        assertEquals(item.getNote(), resp.getNote());
    }


    @Test
    public void readHandlerTest() throws Exception {
        var requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setPathParameters(Map.of("id","someId"));

        var handler = new ReadHandler();
        FieldSetter.setField(handler, handler.getClass()
                .getSuperclass().getDeclaredField("service"), service);


        var result = handler.handleRequest(requestEvent, context);
        var resp = mapper.readValue(result.getBody(), TodoItem.class);

        assertEquals(200, result.getStatusCode());
        assertNotNull(resp);
    }


    @Test
    public void listHandlerTest() throws Exception {
        var requestEvent = new APIGatewayProxyRequestEvent();
        var handler = new ListHandler();
        FieldSetter.setField(handler, handler.getClass()
                .getSuperclass().getDeclaredField("service"), service);

        var result = handler.handleRequest(requestEvent, context);

        assertEquals(200, result.getStatusCode());

    }

    @Test
    public void deleteHandlerTest() throws Exception {
        var requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setPathParameters(Map.of("id","someId"));

        var handler = new DeleteHandler();
        FieldSetter.setField(handler, handler.getClass()
                .getSuperclass().getDeclaredField("service"), service);

        var result = handler.handleRequest(requestEvent, context);

        assertEquals(200, result.getStatusCode());

    }
}
