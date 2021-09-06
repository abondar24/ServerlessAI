package org.abondar.experimental.identity.analysis.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import lombok.val;
import org.abondar.experimental.identity.data.AnalyseResponse;
import software.amazon.awssdk.regions.Region;

import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.AnalyzeDocumentRequest;
import software.amazon.awssdk.services.textract.model.Block;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.Document;
import software.amazon.awssdk.services.textract.model.FeatureType;
import software.amazon.awssdk.services.textract.model.S3Object;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.abondar.experimental.identity.analysis.handler.BlockFields.DateOfBirth;
import static org.abondar.experimental.identity.analysis.handler.BlockFields.DateOfExpiration;
import static org.abondar.experimental.identity.analysis.handler.BlockFields.DateOfIssue;
import static org.abondar.experimental.identity.analysis.handler.BlockFields.GivenNames;
import static org.abondar.experimental.identity.analysis.handler.BlockFields.Nationality;
import static org.abondar.experimental.identity.analysis.handler.BlockFields.PassportNum;
import static org.abondar.experimental.identity.analysis.handler.BlockFields.PlaceOfBirth;
import static org.abondar.experimental.identity.analysis.handler.BlockFields.Surname;

public class AnalyseHandler extends IdentityAnalysisHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final TextractClient txt = TextractClient.builder()
            .region(Region.EU_WEST_1)
            .build();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var logger = context.getLogger();

        var key = getObjectKey(getId(input));
        logger.log(String.format("Analysing document with key %s", key));

        var req = buildAnalyzeRequest(key);
        var res = txt.analyzeDocument(req);
        if (res!=null){
            var ar = buildAnalyseResponse(res.blocks());
            return buildResponse(ar);
        } else {
            return buildResponse("analysis failed");
        }


    }
    private AnalyseResponse buildAnalyseResponse(List<Block> blocks) {
        var resp = AnalyseResponse.builder();

        var filtered = blocks
                .stream()
                .filter(b -> b.blockType()== BlockType.LINE)
                .collect(Collectors.toList());


        for (var i=0;i<filtered.size();i++){
            var block = filtered.get(i);
            var text = block.text();
            if (!checkField(text,PassportNum).isEmpty() ){
                resp.documentNumber(filtered.get(i+1).text());
            }

            if (!checkField(text,Nationality).isEmpty()){
                resp.nationality(filtered.get(i+1).text());
            }

            if (!checkField(text,DateOfBirth).isEmpty()){
                resp.dateOfBirth(filtered.get(i+1).text());
            }

            if (!checkField(text,PlaceOfBirth).isEmpty()){
                if (filtered.get(i+1).text()==null){
                    resp.placeOfBirth(filtered.get(i+2).text());
                } else {
                    resp.placeOfBirth(filtered.get(i+1).text());
                }

            }

            if (!checkField(text,DateOfExpiration).isEmpty()){
                resp.expirationDate(filtered.get(i+1).text());
            }

            if (!checkField(text,DateOfIssue).isEmpty()){
                if (filtered.get(i+1).text()==null){
                    resp.placeOfBirth(filtered.get(i+2).text());
                } else {
                    resp.placeOfBirth(filtered.get(i+1).text());
                }
            }

            if (!checkField(text,GivenNames).isEmpty()){
                if (filtered.get(i+1).text()==null){
                    resp.placeOfBirth(filtered.get(i+2).text());
                } else {
                    resp.placeOfBirth(filtered.get(i+1).text());
                }
            }

            if (!checkField(text,Surname).isEmpty()){
                if (filtered.get(i+1).text()==null){
                    resp.placeOfBirth(filtered.get(i+2).text());
                } else {
                    resp.placeOfBirth(filtered.get(i+1).text());
                }
            }
        }

        return resp.build();
    }

    private String checkField(String text,BlockFields blockField){
        var pt= Pattern.compile(blockField.getVal(),Pattern.CASE_INSENSITIVE);

        var matcher = pt.matcher(text);

        var match  = "";

        while (matcher.find()){
          match = matcher.group();
        }

        return match;
    }

    private AnalyzeDocumentRequest buildAnalyzeRequest(String key) {
        var s3Object = S3Object.builder()
                .bucket(BUCKET)
                .name(key)
                .build();

        var doc = Document.builder()
                .s3Object(s3Object)
                .build();

       return AnalyzeDocumentRequest.builder()
                .document(doc)
                .featureTypes(FeatureType.TABLES,FeatureType.FORMS)
                .build();
    }
}
