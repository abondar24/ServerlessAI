package org.abondar.experimental.analysis.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClient;
import com.amazonaws.services.textract.model.AnalyzeDocumentRequest;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.Document;
import com.amazonaws.services.textract.model.S3Object;
import org.abondar.experimental.analysis.data.AnalyseResponse;
import org.abondar.experimental.analysis.data.BlockFields;

import java.util.List;

public class AnalyseHandler extends IdentityAnalysisHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final AmazonTextract txt = AmazonTextractClient.builder()
            .build();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var logger = context.getLogger();

        var key = getObjectKey(getId(input));
        logger.log(String.format("Analysing document with key %s", key));

        var req = buildAnalyzeRequest(key);
        var res = txt.analyzeDocument(req);

        var ar = buildAnalyseResponse(res.getBlocks());
        return buildResponse(ar);
    }

    private AnalyseResponse buildAnalyseResponse(List<Block> blocks) {
        var resp = AnalyseResponse.builder();

        blocks
                .stream()
                .filter(b -> b.getBlockType().equals("LINE") && b.getConfidence() > 75.0)
                .forEach(b -> {
                    var text = b.getText();

                    if (text.matches("^d+$") && text.length()>5){
                        resp.documentNumber(text);
                    }

                    if (text.contains(BlockFields.Nationality.getVal())){
                        resp.nationality(b.getText());
                    }

                    if (text.contains(BlockFields.DateOfBirth.getVal())){
                        resp.dateOfBirth(b.getText());
                    }

                    if (text.contains(BlockFields.PlaceOfBirth.getVal())){
                        resp.placeOfBirth(b.getText());
                    }

                    if (text.contains(BlockFields.DateOfExpiration.getVal())){
                        resp.expirationDate(b.getText());
                    }

                    if (text.contains(BlockFields.DateOfIssue.getVal())){
                        resp.issueDate(b.getText());
                    }

                    if (text.contains(BlockFields.GivenNames.getVal())){
                        resp.givenNames(b.getText());
                    }

                    if (text.contains(BlockFields.Surname.getVal())){
                        resp.surname(b.getText());
                    }
                });

        return resp.build();
    }

    private AnalyzeDocumentRequest buildAnalyzeRequest(String key) {
        var req = new AnalyzeDocumentRequest();

        var s3Object = new S3Object();
        s3Object = s3Object
                .withName(key)
                .withBucket(BUCKET);

        var doc = new Document();
        doc.setS3Object(s3Object);

        req.setDocument(doc);
        req.setFeatureTypes(List.of("TABLES", "FORMS"));
        return req;
    }
}
