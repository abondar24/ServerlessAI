package org.abondar.experimental.feedback.trainer;

import software.amazon.awssdk.services.comprehend.ComprehendAsyncClient;
import software.amazon.awssdk.services.comprehend.model.LanguageCode;
import software.amazon.awssdk.services.comprehend.model.Tag;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.abondar.experimental.feedback.common.util.Constant.CLASSIFIER;
import static org.abondar.experimental.feedback.common.util.Constant.CLASSIFIER_ROLE_ARN;
import static org.abondar.experimental.feedback.common.util.Constant.TRAIN_BUCKET;


public class FeedbackTrainer {

    private final ComprehendAsyncClient client;


    public FeedbackTrainer() {
        client = ComprehendAsyncClient.builder()
                .build();
    }

    public String createDocumentClassifier() {
        System.out.println("creating classifier");
        var resp = client.createDocumentClassifier(builder -> builder.languageCode(LanguageCode.EN)
                .dataAccessRoleArn(CLASSIFIER_ROLE_ARN)
                .documentClassifierName(CLASSIFIER)
                .inputDataConfig(bld -> bld.s3Uri("s3://" + TRAIN_BUCKET))
                .build());

        var arn = "";

        try {
            arn = resp.get().documentClassifierArn();
        } catch (InterruptedException | ExecutionException ex) {
            System.err.println(ex.getMessage());
        }

        return arn;
    }


    public void createClassifierEndpoint(String classifierArn) {
        System.out.println("Creating classifier endpoint");

        var res = client.createEndpoint(builder -> builder.endpointName(CLASSIFIER)
                .modelArn(classifierArn)
                .desiredInferenceUnits(1)
                .tags(List.of(
                        Tag.builder()
                                .key("department")
                                .value("book")
                                .build()))
                .build());

        try {
            System.out.println(res.get().endpointArn());
        } catch (InterruptedException | ExecutionException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
