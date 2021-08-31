package org.abondar.experimental.analysis.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.Map;

public class IdentityAnalysisHandler {
    private static final Map<String, String> HEADERS = Map.of("Access-Control-Allow-Headers", "Content-Type",
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Methods", "OPTIONS,POST,PUT,GET,DELETE",
            "Access-Control-Allow-Credentials", "true",
            "Content-Type", "application/json"
    );

    protected static final String BUCKET = "anl-bucket";

    private final ObjectMapper mapper;


    public IdentityAnalysisHandler() {
        this.mapper = new ObjectMapper();
        }

    protected <T> APIGatewayProxyResponseEvent buildResponse(T body){
        var resp = new APIGatewayProxyResponseEvent();
        resp.setHeaders(HEADERS);
        resp.setIsBase64Encoded(true);


        try {
            var json = mapper.writeValueAsString(body);
            resp.setBody(json);
            resp.setStatusCode(200);
        } catch (IOException ex){
            resp.setBody(ex.getMessage());
            resp.setStatusCode(500);
        }


        return resp;
    }


    protected String getId(APIGatewayProxyRequestEvent input) {
        var params = input.getPathParameters();
        return params.get("id");
    }

    protected String getObjectKey(String id){
       return String.format("in/%s.jpg",id);
    }

}
