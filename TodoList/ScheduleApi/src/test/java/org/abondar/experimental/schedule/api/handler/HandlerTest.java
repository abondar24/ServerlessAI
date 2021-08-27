package org.abondar.experimental.schedule.api.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.schedule.api.data.TaskResult;
import org.abondar.experimental.schedule.api.service.DynamoServiceTestImpl;
import org.abondar.experimental.schedule.api.service.PollyServiceTestImpl;
import org.abondar.experimental.schedule.service.DynamoService;
import org.abondar.experimental.schedule.service.PollyService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class HandlerTest {

    private static ObjectMapper mapper;

    private static DynamoService dynamoService;

    private static PollyService pollyService;

    private TestContext context;

    @BeforeAll
    public static void init() {
        mapper = new ObjectMapper();
        dynamoService = new DynamoServiceTestImpl();
        pollyService = new PollyServiceTestImpl();
    }

    @BeforeEach
    public void initContext() {
        this.context = new TestContext();
    }

    @Test
    public void dayHandlerTest() throws Exception {
        var request = new APIGatewayProxyRequestEvent();
        var handler = new DayHandler();

        FieldSetter.setField(handler, handler.getClass()
                .getSuperclass().getDeclaredField("pollyService"), pollyService);
        FieldSetter.setField(handler, handler.getClass()
                .getDeclaredField("dynamoService"), dynamoService);

        var response = handler.handleRequest(request, context);
        var result = mapper.readValue(response.getBody(), TaskResult.class);

        assertEquals(200, response.getStatusCode());
        assertEquals("id", result.getTaskId());
        assertEquals("status", result.getTaskStatus());
        assertNull(result.getSignedUrl());
    }


    @Test
    public void pollHandlerTest() throws Exception {
        var request = new APIGatewayProxyRequestEvent();
        request.setPathParameters(Map.of("id", "someId"));
        var handler = new PollHandler();

        FieldSetter.setField(handler, handler.getClass()
                .getSuperclass().getDeclaredField("pollyService"), pollyService);

        var response = handler.handleRequest(request, context);
        var result = mapper.readValue(response.getBody(), TaskResult.class);

        assertEquals(200, response.getStatusCode());
        assertEquals("id", result.getTaskId());
        assertEquals("url", result.getSignedUrl());

    }
}
